<template>
  <div class="page-grid">
    <section class="panel">
      <div class="panel__head">
        <h3>个人档案</h3>
        <el-tag round>Profile</el-tag>
      </div>
      <el-descriptions :column="2" border>
        <el-descriptions-item label="昵称">{{ userStore.profile?.nickname || '-' }}</el-descriptions-item>
        <el-descriptions-item label="用户名">{{ userStore.profile?.username || '-' }}</el-descriptions-item>
        <el-descriptions-item label="手机号">{{ userStore.profile?.phone || '-' }}</el-descriptions-item>
        <el-descriptions-item label="邮箱">{{ userStore.profile?.email || '-' }}</el-descriptions-item>
        <el-descriptions-item label="角色">{{ userStore.profile?.roles.join(', ') || '-' }}</el-descriptions-item>
        <el-descriptions-item label="日目标热量">{{ userStore.profile?.profile?.dailyCalorieTarget || '-' }}</el-descriptions-item>
      </el-descriptions>
    </section>
  </div>
</template>

<script setup lang="ts">
import { useUserStore } from '@/stores/user'

const userStore = useUserStore()
</script>
