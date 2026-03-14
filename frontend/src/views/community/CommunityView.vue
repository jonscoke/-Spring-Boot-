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

    <el-dialog :model-value="createPostDialogVisible" title="发布帖子" width="520px" @close="createPostDialogVisible = false">
      <el-form ref="postFormRef" :model="postForm" :rules="postRules" label-position="top">
        <el-form-item label="帖子内容" prop="content">
          <el-input v-model="postForm.content" type="textarea" :rows="5" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="createPostDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitPost">发布</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { onMounted, reactive, ref } from 'vue'
import { ElMessage, ElMessageBox, type FormInstance, type FormRules } from 'element-plus'
import {
  adminDeletePostApi,
  createCheckinApi,
  createCommentApi,
  createGoalApi,
  createPostApi,
  deleteGoalApi,
  fetchCheckinPageApi,
  fetchGoalPageApi,
  fetchPostPageApi,
  likePostApi,
  unlikePostApi,
  updateGoalApi,
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
const postForm = reactive({ content: '' })
const postRules: FormRules<typeof postForm> = {
  content: [{ required: true, message: '请输入帖子内容', trigger: 'blur' }],
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
  posts.value = data.records
}

async function handleCreateGoal(payload: Omit<GoalRecord, 'id'>) {
  await createGoalApi(payload)
  ElMessage.success('目标已创建')
  await loadGoals()
}

async function handleUpdateGoal(id: number, payload: Omit<GoalRecord, 'id'>) {
  await updateGoalApi(id, payload)
  ElMessage.success('目标已更新')
  await loadGoals()
}

async function handleDeleteGoal(id: number) {
  await deleteGoalApi(id)
  ElMessage.success('目标已删除')
  await loadGoals()
}

async function handleCreateCheckin(payload: Omit<CheckinRecord, 'id'>) {
  await createCheckinApi(payload)
  ElMessage.success('打卡成功')
  await loadCheckins()
}

async function submitPost() {
  const valid = await postFormRef.value?.validate().catch(() => false)
  if (!valid) {
    return
  }
  await createPostApi({ content: postForm.content })
  ElMessage.success('帖子已发布')
  createPostDialogVisible.value = false
  postForm.content = ''
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
  const comment = await createCommentApi(postId, { content })
  comments.value = {
    ...comments.value,
    [postId]: [...(comments.value[postId] || []), comment],
  }
  ElMessage.success('评论成功')
  await loadPosts()
}

async function handleDeletePost(postId: number) {
  await ElMessageBox.confirm('确认删除该帖子？', '提示', { type: 'warning' })
  await adminDeletePostApi(postId)
  ElMessage.success('帖子已删除')
  await loadPosts()
}

onMounted(() => {
  void Promise.all([loadGoals(), loadCheckins(), loadPosts()])
})
</script>
