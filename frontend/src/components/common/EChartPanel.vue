<template>
  <div ref="containerRef" class="chart-box"></div>
</template>

<script setup lang="ts">
import { nextTick, onBeforeUnmount, onMounted, ref, watch } from 'vue'
import * as echarts from 'echarts'
import type { EChartsOption } from 'echarts'

const props = defineProps<{
  option: EChartsOption
}>()

const containerRef = ref<HTMLElement | null>(null)
let chart: echarts.ECharts | null = null

function render() {
  if (!containerRef.value) {
    return
  }
  if (!chart) {
    chart = echarts.init(containerRef.value)
  }
  chart.setOption(props.option, true)
}

function handleResize() {
  chart?.resize()
}

watch(
  () => props.option,
  async () => {
    await nextTick()
    render()
  },
  { deep: true },
)

onMounted(async () => {
  await nextTick()
  render()
  window.addEventListener('resize', handleResize)
})

onBeforeUnmount(() => {
  window.removeEventListener('resize', handleResize)
  chart?.dispose()
  chart = null
})
</script>
