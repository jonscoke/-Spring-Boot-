<template>
  <section class="panel">
    <div class="panel__head">
      <h3>Post Feed</h3>
      <el-button type="primary" @click="emit('create-post')">Publish</el-button>
    </div>

    <EmptyBlock v-if="!posts.length" description="No posts yet." />
    <div v-else class="post-list">
      <article v-for="post in posts" :key="post.id" class="post-card">
        <div class="post-card__head">
          <strong>{{ post.authorNickname }}</strong>
          <span>{{ post.createdAt }}</span>
        </div>
        <p>{{ post.content }}</p>
        <div v-if="post.images?.length" class="post-card__gallery">
          <el-image
            v-for="image in post.images"
            :key="image"
            :src="image"
            :preview-src-list="post.images"
            preview-teleported
            fit="cover"
            class="post-card__image"
          />
        </div>
        <div class="post-card__meta">
          <span>{{ post.likeCount }} likes</span>
          <span>{{ post.commentCount }} comments</span>
        </div>
        <div class="inline-actions">
          <el-button size="small" @click="post.liked ? emit('unlike', post.id) : emit('like', post.id)">
            {{ post.liked ? 'Unlike' : 'Like' }}
          </el-button>
          <el-button size="small" @click="openComment(post.id)">Comment</el-button>
          <el-button v-if="isAdmin" size="small" type="danger" @click="emit('delete-post', post.id)">Delete</el-button>
        </div>
        <div v-if="draftPostId === post.id" class="comment-box">
          <el-input v-model="draftComment" type="textarea" :rows="2" placeholder="Write a comment" />
          <div class="inline-actions">
            <el-button type="primary" size="small" @click="submitComment(post.id)">Submit</el-button>
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

<style scoped>
.post-card__gallery {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(92px, 1fr));
  gap: 10px;
  margin: 12px 0;
}

.post-card__image {
  width: 100%;
  height: 92px;
  border-radius: 12px;
  overflow: hidden;
}
</style>
