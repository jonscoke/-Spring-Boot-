<template>
  <el-table :data="records" stripe>
    <el-table-column prop="record.recordDate" label="日期" width="120" />
    <el-table-column prop="record.mealType" label="餐别" width="100" />
    <el-table-column prop="record.totalCalories" label="总热量" width="120" />
    <el-table-column prop="record.totalProtein" label="蛋白质" width="110" />
    <el-table-column prop="record.totalFat" label="脂肪" width="110" />
    <el-table-column prop="record.totalCarb" label="碳水" width="110" />
    <el-table-column label="食物明细" min-width="260">
      <template #default="{ row }">
        <div class="tag-wrap">
          <el-tag v-for="item in row.items" :key="`${row.record.id}-${item.foodItemId}`" round>
            {{ item.foodName }} {{ item.intakeGram }}g
          </el-tag>
        </div>
      </template>
    </el-table-column>
  </el-table>
</template>

<script setup lang="ts">
import type { DietRecordVO } from '@/types/api'

defineProps<{
  records: DietRecordVO[]
}>()
</script>
