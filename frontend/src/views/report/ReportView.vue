<template>
  <div class="page-grid">
    <section class="panel">
      <div class="panel__head">
        <h3>报告中心</h3>
        <div class="inline-actions">
          <el-button :loading="loadingWeekly" @click="generateWeekly">生成周报</el-button>
          <el-button type="primary" :loading="loadingMonthly" @click="generateMonthly">生成月报</el-button>
        </div>
      </div>
      <EmptyBlock v-if="!records.length" description="暂无报告数据" />
      <el-table v-else :data="records" stripe>
        <el-table-column prop="title" label="报告标题" />
        <el-table-column prop="reportType" label="周期" />
        <el-table-column prop="createdAt" label="生成时间" />
        <el-table-column label="操作" width="120">
          <template #default="{ row }">
            <el-button link type="primary" @click="currentReport = row">查看</el-button>
          </template>
        </el-table-column>
      </el-table>
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
    <ReportDetailDrawer :visible="Boolean(currentReport)" :report="currentReport" @close="currentReport = null" />
  </div>
</template>

<script setup lang="ts">
import { onMounted, reactive, ref } from 'vue'
import { ElMessage } from 'element-plus'
import { fetchReportPageApi, generateMonthlyReportApi, generateWeeklyReportApi } from '@/api'
import EmptyBlock from '@/components/common/EmptyBlock.vue'
import ReportDetailDrawer from '@/components/report/ReportDetailDrawer.vue'
import type { ReportRecord } from '@/types/api'

const records = ref<ReportRecord[]>([])
const total = ref(0)
const currentReport = ref<ReportRecord | null>(null)
const loadingWeekly = ref(false)
const loadingMonthly = ref(false)
const query = reactive({
  page: 1,
  size: 10,
})

async function loadData() {
  const data = await fetchReportPageApi(query)
  records.value = data.records
  total.value = data.total
}

async function generateWeekly() {
  loadingWeekly.value = true
  try {
    await generateWeeklyReportApi()
    ElMessage.success('周报已生成')
    await loadData()
  } finally {
    loadingWeekly.value = false
  }
}

async function generateMonthly() {
  loadingMonthly.value = true
  try {
    const now = new Date()
    const month = `${now.getFullYear()}-${`${now.getMonth() + 1}`.padStart(2, '0')}`
    await generateMonthlyReportApi(month)
    ElMessage.success('月报已生成')
    await loadData()
  } finally {
    loadingMonthly.value = false
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
