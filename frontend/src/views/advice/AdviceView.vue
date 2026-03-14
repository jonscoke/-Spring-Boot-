<template>
  <div class="page-grid">
    <section class="panel">
      <div class="panel__head">
        <h3>个性化建议</h3>
        <el-button type="primary" :loading="loading" @click="handleGenerate">生成建议</el-button>
      </div>
      <EmptyBlock v-if="!advice" description="暂无建议记录">
        <el-button type="primary" @click="handleGenerate">立即生成</el-button>
      </EmptyBlock>
      <template v-else>
        <el-alert :title="advice.energyAdvice" type="success" :closable="false" show-icon />
        <el-alert :title="advice.dietAdvice" type="warning" :closable="false" show-icon />
        <el-alert :title="advice.sportAdvice" type="info" :closable="false" show-icon />
        <section class="panel">
          <div class="panel__head">
            <h3>完整建议文本</h3>
          </div>
          <p class="multiline-text">{{ advice.adviceText }}</p>
        </section>
      </template>
    </section>
  </div>
</template>

<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { ElMessage } from 'element-plus'
import { fetchLatestAdviceApi, generateAdviceApi } from '@/api'
import EmptyBlock from '@/components/common/EmptyBlock.vue'
import type { AdviceRecord } from '@/types/api'

const advice = ref<AdviceRecord | null>(null)
const loading = ref(false)

async function loadData() {
  advice.value = await fetchLatestAdviceApi()
}

async function handleGenerate() {
  loading.value = true
  try {
    advice.value = await generateAdviceApi()
    ElMessage.success('建议已生成')
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  void loadData()
})
</script>
