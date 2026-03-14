<template>
  <div class="empty-block">
    <el-empty :description="description">
      <slot />
    </el-empty>
  </div>
</template>

<script setup lang="ts">
defineProps<{
  description: string
}>()
</script>
