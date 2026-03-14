<template>
  <div class="page-grid">
    <section class="panel">
      <div class="panel__head">
        <h3>报告中心</h3>
        <div class="inline-actions">
          <el-button>生成周报</el-button>
          <el-button type="primary">生成月报</el-button>
        </div>
      </div>
      <el-table :data="rows" stripe>
        <el-table-column prop="title" label="报告标题" />
        <el-table-column prop="period" label="周期" />
        <el-table-column prop="createdAt" label="生成时间" />
      </el-table>
    </section>
  </div>
</template>

<script setup lang="ts">
const rows = [
  { title: 'Weekly Report 2026-03-09 ~ 2026-03-15', period: '周报', createdAt: '2026-03-14 10:20' },
  { title: 'Monthly Report 2026-03', period: '月报', createdAt: '2026-03-14 10:30' },
]
</script>
