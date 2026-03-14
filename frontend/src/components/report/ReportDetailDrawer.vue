<template>
  <el-drawer :model-value="visible" size="42%" title="报告详情" @close="emit('close')">
    <template v-if="report">
      <div class="page-grid">
        <section class="panel">
          <div class="panel__head">
            <h3>{{ report.title }}</h3>
            <el-tag round>{{ report.reportType }}</el-tag>
          </div>
          <p>{{ report.reportText }}</p>
        </section>
        <section class="panel">
          <div class="panel__head">
            <h3>结构化摘要</h3>
          </div>
          <pre class="json-block">{{ prettySummary }}</pre>
        </section>
      </div>
    </template>
  </el-drawer>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import type { ReportRecord } from '@/types/api'

const props = defineProps<{
  visible: boolean
  report: ReportRecord | null
}>()

const emit = defineEmits<{
  close: []
}>()

const prettySummary = computed(() => {
  if (!props.report?.summaryJson) {
    return '{}'
  }
  try {
    return JSON.stringify(JSON.parse(props.report.summaryJson), null, 2)
  } catch {
    return props.report.summaryJson
  }
})
</script>
