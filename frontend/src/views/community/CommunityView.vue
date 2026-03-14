<template>
  <div class="page-grid">
    <GoalPanel :goals="goals" @create="handleCreateGoal" @update="handleUpdateGoal" @delete="handleDeleteGoal" />
    <CheckinPanel :checkins="checkins" @submit="handleCreateCheckin" />
    <PostFeed
      :posts="posts"
      :comments="comments"
      :is-admin="userStore.isAdmin"
      @create-post="createPostDialogVisible = true"
      @like="handleLike"
      @unlike="handleUnlike"
      @comment="handleComment"
      @delete-post="handleDeletePost"
    />

    <el-dialog :model-value="createPostDialogVisible" title="Publish Post" width="520px" @close="handleDialogClose">
      <el-form ref="postFormRef" :model="postForm" :rules="postRules" label-position="top">
        <el-form-item label="Post Content" prop="content">
          <el-input v-model="postForm.content" type="textarea" :rows="5" maxlength="500" show-word-limit />
        </el-form-item>
        <el-form-item label="Images">
          <el-upload
            v-model:file-list="imageList"
            list-type="picture-card"
            :limit="6"
            :auto-upload="true"
            :http-request="handleUploadImage"
            accept="image/*"
          >
            <el-icon><Plus /></el-icon>
          </el-upload>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="handleDialogClose">Cancel</el-button>
        <el-button type="primary" @click="submitPost">Publish</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { onMounted, reactive, ref } from 'vue'
import { ElMessage, ElMessageBox, type FormInstance, type FormRules, type UploadRequestOptions, type UploadUserFile } from 'element-plus'
import { Plus } from '@element-plus/icons-vue'
import {
  adminDeletePostApi,
  createCheckinApi,
  createCommentApi,
  createGoalApi,
  createPostApi,
  deleteGoalApi,
  fetchCheckinPageApi,
  fetchGoalPageApi,
  fetchPostCommentsApi,
  fetchPostPageApi,
  likePostApi,
  unlikePostApi,
  updateGoalApi,
  uploadPostImageApi,
} from '@/api'
import CheckinPanel from '@/components/community/CheckinPanel.vue'
import GoalPanel from '@/components/community/GoalPanel.vue'
import PostFeed from '@/components/community/PostFeed.vue'
import { useUserStore } from '@/stores/user'
import type { CheckinRecord, CommentRecord, GoalRecord, PostRecord } from '@/types/api'

const userStore = useUserStore()
const goals = ref<GoalRecord[]>([])
const checkins = ref<CheckinRecord[]>([])
const posts = ref<PostRecord[]>([])
const comments = ref<Record<number, CommentRecord[]>>({})
const createPostDialogVisible = ref(false)
const postFormRef = ref<FormInstance>()
const imageList = ref<UploadUserFile[]>([])
const postForm = reactive({ content: '' })
const postRules: FormRules<typeof postForm> = {
  content: [{ required: true, message: 'Please enter post content', trigger: 'blur' }],
}

async function loadGoals() {
  const data = await fetchGoalPageApi({ page: 1, size: 20 })
  goals.value = data.records
}

async function loadCheckins() {
  const data = await fetchCheckinPageApi({ page: 1, size: 10 })
  checkins.value = data.records
}

async function loadPosts() {
  const data = await fetchPostPageApi({ page: 1, size: 20 })
  posts.value = data.records.map((post) => ({
    ...post,
    images: post.images?.map(normalizeImageUrl) ?? [],
  }))
  const commentEntries = await Promise.all(
    data.records.map(async (post) => [post.id, await fetchPostCommentsApi(post.id)] as const),
  )
  comments.value = Object.fromEntries(commentEntries)
}

async function handleCreateGoal(payload: Omit<GoalRecord, 'id'>) {
  await createGoalApi(payload)
  ElMessage.success('Goal created')
  await loadGoals()
}

async function handleUpdateGoal(id: number, payload: Omit<GoalRecord, 'id'>) {
  await updateGoalApi(id, payload)
  ElMessage.success('Goal updated')
  await loadGoals()
}

async function handleDeleteGoal(id: number) {
  await deleteGoalApi(id)
  ElMessage.success('Goal deleted')
  await loadGoals()
}

async function handleCreateCheckin(payload: Omit<CheckinRecord, 'id'>) {
  await createCheckinApi(payload)
  ElMessage.success('Check-in submitted')
  await loadCheckins()
}

async function submitPost() {
  const valid = await postFormRef.value?.validate().catch(() => false)
  if (!valid) {
    return
  }
  await createPostApi({
    content: postForm.content,
    images: imageList.value
      .map((item) => item.url)
      .filter((url): url is string => Boolean(url))
      .map(toRelativeImageUrl),
  })
  ElMessage.success('Post published')
  handleDialogClose()
  await loadPosts()
}

async function handleLike(postId: number) {
  await likePostApi(postId)
  await loadPosts()
}

async function handleUnlike(postId: number) {
  await unlikePostApi(postId)
  await loadPosts()
}

async function handleComment(postId: number, content: string) {
  await createCommentApi(postId, { content })
  comments.value = {
    ...comments.value,
    [postId]: await fetchPostCommentsApi(postId),
  }
  ElMessage.success('Comment posted')
  await loadPosts()
}

async function handleDeletePost(postId: number) {
  await ElMessageBox.confirm('Delete this post?', 'Warning', { type: 'warning' })
  await adminDeletePostApi(postId)
  ElMessage.success('Post deleted')
  await loadPosts()
}

async function handleUploadImage(options: UploadRequestOptions) {
  const result = await uploadPostImageApi(options.file as File)
  const relativeUrl = result.url
  const fullUrl = relativeUrl.startsWith('http') ? relativeUrl : `${import.meta.env.VITE_API_BASE_URL}${relativeUrl}`
  const file = options.file as UploadUserFile
  file.url = fullUrl
  options.onSuccess?.(result)
}

function handleDialogClose() {
  createPostDialogVisible.value = false
  postForm.content = ''
  imageList.value = []
}

function normalizeImageUrl(url: string) {
  return url.startsWith('http') ? url : `${import.meta.env.VITE_API_BASE_URL}${url}`
}

function toRelativeImageUrl(url: string) {
  const baseUrl = String(import.meta.env.VITE_API_BASE_URL || '')
  return baseUrl && url.startsWith(baseUrl) ? url.slice(baseUrl.length) : url
}

onMounted(() => {
  void Promise.all([loadGoals(), loadCheckins(), loadPosts()])
})
</script>
