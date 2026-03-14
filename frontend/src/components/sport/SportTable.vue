<template>
  <el-table :data="records" stripe>
    <el-table-column prop="recordDate" label="日期" width="120" />
    <el-table-column prop="sportType" label="类型" width="100" />
    <el-table-column prop="steps" label="步数" width="100" />
    <el-table-column prop="distanceKm" label="距离(km)" width="110" />
    <el-table-column prop="durationMin" label="时长(min)" width="110" />
    <el-table-column prop="caloriesBurned" label="热量(kcal)" width="120" />
    <el-table-column prop="sourceType" label="来源" width="120" />
    <el-table-column label="操作" min-width="160" fixed="right">
      <template #default="{ row }">
        <el-button link type="primary" @click="emit('edit', row)">编辑</el-button>
        <el-button link type="danger" @click="emit('delete', row)">删除</el-button>
      </template>
    </el-table-column>
  </el-table>
</template>

<script setup lang="ts">
import type { SportRecord } from '@/types/api'

defineProps<{
  records: SportRecord[]
}>()

const emit = defineEmits<{
  edit: [row: SportRecord]
  delete: [row: SportRecord]
}>()
</script>
