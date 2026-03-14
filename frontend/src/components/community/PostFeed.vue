<template>
  <section class="panel">
    <div class="panel__head">
      <h3>帖子流</h3>
      <el-button type="primary" @click="emit('create-post')">发布帖子</el-button>
    </div>

    <EmptyBlock v-if="!posts.length" description="社区还没有帖子" />
    <div v-else class="post-list">
      <article v-for="post in posts" :key="post.id" class="post-card">
        <div class="post-card__head">
          <strong>{{ post.authorNickname }}</strong>
          <span>{{ post.createdAt }}</span>
        </div>
        <p>{{ post.content }}</p>
        <div class="post-card__meta">
          <span>{{ post.likeCount }} 赞</span>
          <span>{{ post.commentCount }} 评论</span>
        </div>
        <div class="inline-actions">
          <el-button size="small" @click="post.liked ? emit('unlike', post.id) : emit('like', post.id)">
            {{ post.liked ? '取消点赞' : '点赞' }}
          </el-button>
          <el-button size="small" @click="openComment(post.id)">评论</el-button>
          <el-button v-if="isAdmin" size="small" type="danger" @click="emit('delete-post', post.id)">删帖</el-button>
        </div>
        <div v-if="draftPostId === post.id" class="comment-box">
          <el-input v-model="draftComment" type="textarea" :rows="2" placeholder="输入评论内容" />
          <div class="inline-actions">
            <el-button type="primary" size="small" @click="submitComment(post.id)">提交评论</el-button>
          </div>
        </div>
        <div v-if="comments[post.id]?.length" class="stack-list">
          <article v-for="comment in comments[post.id]" :key="comment.id" class="stack-card">
            <strong>{{ comment.authorNickname }}</strong>
            <p>{{ comment.content }}</p>
          </article>
        </div>
      </article>
    </div>
  </section>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import EmptyBlock from '@/components/common/EmptyBlock.vue'
import type { CommentRecord, PostRecord } from '@/types/api'

defineProps<{
  posts: PostRecord[]
  comments: Record<number, CommentRecord[]>
  isAdmin: boolean
}>()

const emit = defineEmits<{
  'create-post': []
  like: [postId: number]
  unlike: [postId: number]
  comment: [postId: number, content: string]
  'delete-post': [postId: number]
}>()

const draftPostId = ref<number | null>(null)
const draftComment = ref('')

function openComment(postId: number) {
  draftPostId.value = postId
  draftComment.value = ''
}

function submitComment(postId: number) {
  if (!draftComment.value.trim()) {
    return
  }
  emit('comment', postId, draftComment.value.trim())
  draftComment.value = ''
}
</script>
