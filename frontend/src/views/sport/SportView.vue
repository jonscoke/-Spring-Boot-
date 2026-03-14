<template>
  <div class="page-grid">
    <section class="panel">
      <div class="panel__head">
        <h3>运动管理</h3>
        <div class="inline-actions">
          <el-button @click="handleSync" :loading="syncing">Mock 同步</el-button>
          <el-button type="primary" @click="openCreate">新增记录</el-button>
        </div>
      </div>

      <EmptyBlock v-if="!records.length" description="暂无运动记录">
        <el-button type="primary" @click="openCreate">新增首条记录</el-button>
      </EmptyBlock>
      <SportTable v-else :records="records" @edit="openEdit" @delete="handleDelete" />

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

    <SportFormDialog
      :visible="dialogVisible"
      :mode="dialogMode"
      :initial-value="currentRecord"
      :submitting="submitting"
      @close="dialogVisible = false"
      @submit="handleSubmit"
    />
  </div>
</template>

<script setup lang="ts">
import { onMounted, reactive, ref } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { createSportApi, deleteSportApi, fetchSportPageApi, syncMockSportApi, updateSportApi } from '@/api'
import EmptyBlock from '@/components/common/EmptyBlock.vue'
import SportFormDialog from '@/components/sport/SportFormDialog.vue'
import SportTable from '@/components/sport/SportTable.vue'
import type { SportRecord } from '@/types/api'

const records = ref<SportRecord[]>([])
const total = ref(0)
const syncing = ref(false)
const submitting = ref(false)
const dialogVisible = ref(false)
const dialogMode = ref<'create' | 'edit'>('create')
const currentRecord = ref<SportRecord | null>(null)
const query = reactive({
  page: 1,
  size: 10,
})

async function loadData() {
  const data = await fetchSportPageApi(query)
  records.value = data.records
  total.value = data.total
}

function openCreate() {
  dialogMode.value = 'create'
  currentRecord.value = {
    sportType: 'RUN',
    steps: 0,
    distanceKm: 0,
    durationMin: 0,
    pace: 0,
    caloriesBurned: 0,
    sourceType: 'MANUAL',
    externalId: null,
    recordDate: new Date().toISOString().slice(0, 10),
  }
  dialogVisible.value = true
}

function openEdit(row: SportRecord) {
  dialogMode.value = 'edit'
  currentRecord.value = { ...row }
  dialogVisible.value = true
}

async function handleSubmit(payload: SportRecord) {
  submitting.value = true
  try {
    if (dialogMode.value === 'create') {
      await createSportApi(payload)
      ElMessage.success('运动记录已新增')
    } else if (currentRecord.value?.id) {
      await updateSportApi(currentRecord.value.id, payload)
      ElMessage.success('运动记录已更新')
    }
    dialogVisible.value = false
    await loadData()
  } finally {
    submitting.value = false
  }
}

async function handleDelete(row: SportRecord) {
  await ElMessageBox.confirm('确定删除这条运动记录吗？', '提示', { type: 'warning' })
  await deleteSportApi(row.id as number)
  ElMessage.success('已删除')
  await loadData()
}

async function handleSync() {
  syncing.value = true
  try {
    const result = await syncMockSportApi()
    ElMessage.success(`同步完成，新增 ${result.insertedCount} 条，重复 ${result.duplicateCount} 条`)
    await loadData()
  } finally {
    syncing.value = false
  }
}

async function handlePageChange(page: number) {
  query.page = page
  await loadData()
}

onMounted(() => {
  void loadData()
})
</script>
