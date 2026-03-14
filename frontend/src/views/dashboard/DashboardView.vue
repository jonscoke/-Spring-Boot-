<template>
  <div class="page-grid">
    <section class="panel panel--hero">
      <div>
        <span class="panel__eyebrow">Dashboard Overview</span>
        <h2>系统运行骨架已经就位</h2>
        <p>这里预留仪表盘概览卡片、运动趋势、饮食趋势和能量平衡图表接口。</p>
      </div>
      <div class="metric-stack">
        <article class="metric-card">
          <span>今日摄入</span>
          <strong>1,860 kcal</strong>
        </article>
        <article class="metric-card">
          <span>今日消耗</span>
          <strong>540 kcal</strong>
        </article>
        <article class="metric-card">
          <span>近7日活跃</span>
          <strong>6 / 7 天</strong>
        </article>
      </div>
    </section>

    <section class="panel panel--chart">
      <div class="panel__head">
        <h3>运动与饮食趋势</h3>
        <el-tag type="info" round>echarts 已接入</el-tag>
      </div>
      <div ref="chartRef" class="chart-box"></div>
    </section>
  </div>
</template>

<script setup lang="ts">
import { nextTick, onBeforeUnmount, onMounted, ref } from 'vue'
import * as echarts from 'echarts'

const chartRef = ref<HTMLElement | null>(null)
let chart: echarts.ECharts | null = null

function renderChart() {
  if (!chartRef.value) {
    return
  }
  chart = echarts.init(chartRef.value)
  chart.setOption({
    tooltip: { trigger: 'axis' },
    grid: { left: 36, right: 18, top: 24, bottom: 28 },
    legend: { top: 0, textStyle: { color: '#44556f' } },
    xAxis: {
      type: 'category',
      boundaryGap: false,
      data: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun'],
      axisLine: { lineStyle: { color: '#b4c0d0' } },
    },
    yAxis: {
      type: 'value',
      axisLine: { show: false },
      splitLine: { lineStyle: { color: '#e9eef5' } },
    },
    series: [
      {
        name: '运动消耗',
        type: 'line',
        smooth: true,
        data: [320, 420, 510, 380, 460, 530, 490],
        lineStyle: { color: '#0f766e', width: 3 },
        areaStyle: { color: 'rgba(15,118,110,0.12)' },
      },
      {
        name: '饮食摄入',
        type: 'line',
        smooth: true,
        data: [1700, 1820, 1750, 1890, 1780, 1930, 1850],
        lineStyle: { color: '#f97316', width: 3 },
        areaStyle: { color: 'rgba(249,115,22,0.10)' },
      },
    ],
  })
}

function resizeChart() {
  chart?.resize()
}

onMounted(async () => {
  await nextTick()
  renderChart()
  window.addEventListener('resize', resizeChart)
})

onBeforeUnmount(() => {
  window.removeEventListener('resize', resizeChart)
  chart?.dispose()
  chart = null
})
</script>
