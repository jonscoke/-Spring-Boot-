<template>
  <div class="page-grid">
    <section class="panel">
      <div class="panel__head">
        <h3>饮食管理</h3>
        <el-button type="primary" @click="dialogVisible = true">新增饮食</el-button>
      </div>

      <div class="panel__head">
        <h3>快速食物检索</h3>
      </div>
      <FoodSearchList @select="handlePreviewFood" />

      <div class="panel__head">
        <h3>历史列表</h3>
      </div>
      <EmptyBlock v-if="!records.length" description="暂无饮食记录" />
      <DietHistoryTable v-else :records="records" />

      <div class="table-footer">
        <el-pagination
          background
          layout="prev, pager, next"
          :total="total"
          :page-size="query.size"
          :current-page="query.page"
          @current-change="handlePageChange"
        />
      </div>
    </section>

    <DietCreateDialog
      :visible="dialogVisible"
      :submitting="submitting"
      @close="dialogVisible = false"
      @submit="handleSubmit"
    />
  </div>
</template>

<script setup lang="ts">
import { onMounted, reactive, ref } from 'vue'
import { ElMessage } from 'element-plus'
import { createDietApi, fetchDietPageApi } from '@/api'
import EmptyBlock from '@/components/common/EmptyBlock.vue'
import DietCreateDialog from '@/components/diet/DietCreateDialog.vue'
import DietHistoryTable from '@/components/diet/DietHistoryTable.vue'
import FoodSearchList from '@/components/diet/FoodSearchList.vue'
import type { DietRecordVO, FoodItem } from '@/types/api'

const records = ref<DietRecordVO[]>([])
const total = ref(0)
const dialogVisible = ref(false)
const submitting = ref(false)
const query = reactive({
  page: 1,
  size: 10,
})

async function loadData() {
  const data = await fetchDietPageApi(query)
  records.value = data.records
  total.value = data.total
}

async function handleSubmit(payload: { mealType: string; recordDate: string; remark?: string; items: { foodItemId: number; intakeGram: number }[] }) {
  submitting.value = true
  try {
    await createDietApi(payload)
    ElMessage.success('饮食记录已新增')
    dialogVisible.value = false
    await loadData()
  } finally {
    submitting.value = false
  }
}

function handlePreviewFood(food: FoodItem) {
  ElMessage.info(`${food.foodName}: ${food.caloriePer100g} kcal / 100g`)
}

async function handlePageChange(page: number) {
  query.page = page
  await loadData()
}

onMounted(() => {
  void loadData()
})
</script>
