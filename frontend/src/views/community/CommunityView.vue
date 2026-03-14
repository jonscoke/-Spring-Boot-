<template>
  <div class="page-grid">
    <section class="panel">
      <div class="panel__head">
        <h3>社区互动</h3>
        <el-button type="primary">发布帖子</el-button>
      </div>
      <div class="post-list">
        <article v-for="post in posts" :key="post.id" class="post-card">
          <div class="post-card__head">
            <strong>{{ post.author }}</strong>
            <span>{{ post.time }}</span>
          </div>
          <p>{{ post.content }}</p>
          <div class="post-card__meta">
            <span>{{ post.likes }} 赞</span>
            <span>{{ post.comments }} 评论</span>
          </div>
        </article>
      </div>
    </section>
  </div>
</template>

<script setup lang="ts">
const posts = [
  { id: 1, author: '晨跑爱好者', time: '5 分钟前', content: '今天 6km，状态不错。', likes: 12, comments: 3 },
  { id: 2, author: '饮食打卡员', time: '20 分钟前', content: '午餐鸡胸肉配西兰花，控制住了。', likes: 8, comments: 2 },
]
</script>
