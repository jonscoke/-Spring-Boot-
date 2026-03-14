<template>
  <div class="page-grid">
    <section class="panel">
      <div class="panel__head">
        <h3>运动管理</h3>
        <el-button type="primary">新增记录</el-button>
      </div>
      <el-table :data="rows" stripe>
        <el-table-column prop="date" label="日期" />
        <el-table-column prop="type" label="运动类型" />
        <el-table-column prop="duration" label="时长" />
        <el-table-column prop="calories" label="消耗热量" />
      </el-table>
    </section>
  </div>
</template>

<script setup lang="ts">
const rows = [
  { date: '2026-03-14', type: 'RUN', duration: '42 min', calories: '315 kcal' },
  { date: '2026-03-13', type: 'WALK', duration: '55 min', calories: '192 kcal' },
]
</script>
