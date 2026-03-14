<template>
  <div class="page-grid">
    <section class="panel">
      <div class="panel__head">
        <h3>饮食管理</h3>
        <el-button type="primary">新增饮食</el-button>
      </div>
      <el-table :data="rows" stripe>
        <el-table-column prop="date" label="日期" />
        <el-table-column prop="meal" label="餐别" />
        <el-table-column prop="calories" label="总热量" />
        <el-table-column prop="protein" label="蛋白质" />
      </el-table>
    </section>
  </div>
</template>

<script setup lang="ts">
const rows = [
  { date: '2026-03-14', meal: 'LUNCH', calories: '620 kcal', protein: '38 g' },
  { date: '2026-03-14', meal: 'DINNER', calories: '540 kcal', protein: '31 g' },
]
</script>
