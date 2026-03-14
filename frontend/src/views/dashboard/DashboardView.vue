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
          <strong>{{ formatNumber(stats?.todayIntakeCalories) }} kcal</strong>
        </article>
        <article class="metric-card">
          <span>今日消耗</span>
          <strong>{{ formatNumber(stats?.todayBurnCalories) }} kcal</strong>
        </article>
        <article class="metric-card">
          <span>今日能量差</span>
          <strong>{{ formatNumber(stats?.todayEnergyGap) }} kcal</strong>
        </article>
      </div>
    </section>

    <section class="panel panel--chart">
      <div class="panel__head">
        <h3>运动与饮食趋势</h3>
        <el-tag type="info" round>echarts 已接入</el-tag>
      </div>
      <EmptyBlock v-if="!hasTrendData" description="暂无趋势数据" />
      <EChartPanel v-else :option="trendOption" />
    </section>

    <section class="panel">
      <div class="panel__head">
        <h3>摄入 / 消耗对比</h3>
      </div>
      <EmptyBlock v-if="!stats" description="暂无统计数据" />
      <EChartPanel v-else :option="balanceOption" />
    </section>

    <section class="panel">
      <div class="panel__head">
        <h3>营养占比</h3>
      </div>
      <EmptyBlock v-if="!ratio" description="暂无营养数据" />
      <EChartPanel v-else :option="ratioOption" />
    </section>
  </div>
</template>

<script setup lang="ts">
import { computed, onMounted, ref } from 'vue'
import type { EChartsOption } from 'echarts'
import { fetchDashboardStatsApi, fetchNutritionRatioApi } from '@/api'
import EmptyBlock from '@/components/common/EmptyBlock.vue'
import EChartPanel from '@/components/common/EChartPanel.vue'
import type { DashboardStats, NutritionRatio } from '@/types/api'

const stats = ref<DashboardStats | null>(null)
const ratio = ref<NutritionRatio | null>(null)

const hasTrendData = computed(() =>
  Boolean(stats.value?.sportTrend7d.length || stats.value?.dietTrend7d.length),
)

const trendOption = computed<EChartsOption>(() => ({
    tooltip: { trigger: 'axis' },
    grid: { left: 36, right: 18, top: 24, bottom: 28 },
    legend: { top: 0, textStyle: { color: '#44556f' } },
    xAxis: {
      type: 'category',
      boundaryGap: false,
      data: stats.value?.sportTrend7d.map((item) => item.date.slice(5)) ?? [],
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
        data: stats.value?.sportTrend7d.map((item) => item.caloriesBurned) ?? [],
        lineStyle: { color: '#0f766e', width: 3 },
        areaStyle: { color: 'rgba(15,118,110,0.12)' },
      },
      {
        name: '饮食摄入',
        type: 'line',
        smooth: true,
        data: stats.value?.dietTrend7d.map((item) => item.caloriesIntake) ?? [],
        lineStyle: { color: '#f97316', width: 3 },
        areaStyle: { color: 'rgba(249,115,22,0.10)' },
      },
    ],
  }))

const balanceOption = computed<EChartsOption>(() => ({
  tooltip: { trigger: 'axis' },
  grid: { left: 28, right: 18, top: 20, bottom: 18 },
  xAxis: {
    type: 'category',
    data: ['今日摄入', '今日消耗', '今日差值'],
    axisTick: { show: false },
  },
  yAxis: { type: 'value', splitLine: { lineStyle: { color: '#e9eef5' } } },
  series: [
    {
      type: 'bar',
      barWidth: 42,
      data: [
        stats.value?.todayIntakeCalories ?? 0,
        stats.value?.todayBurnCalories ?? 0,
        stats.value?.todayEnergyGap ?? 0,
      ],
      itemStyle: {
        borderRadius: [12, 12, 0, 0],
        color: (params) => ['#f97316', '#0f766e', '#2563eb'][params.dataIndex],
      },
    },
  ],
}))

const ratioOption = computed<EChartsOption>(() => ({
  tooltip: { trigger: 'item' },
  legend: { bottom: 0 },
  series: [
    {
      type: 'pie',
      radius: ['52%', '75%'],
      itemStyle: { borderRadius: 10, borderColor: '#fff', borderWidth: 4 },
      data: ratio.value
        ? [
            { value: Number((ratio.value.proteinCalorieRatio * 100).toFixed(2)), name: '蛋白质' },
            { value: Number((ratio.value.fatCalorieRatio * 100).toFixed(2)), name: '脂肪' },
            { value: Number((ratio.value.carbCalorieRatio * 100).toFixed(2)), name: '碳水' },
          ]
        : [],
    },
  ],
}))

onMounted(async () => {
  stats.value = await fetchDashboardStatsApi()
  ratio.value = await fetchNutritionRatioApi().catch(() => null)
})

function formatNumber(value?: number) {
  return Number(value ?? 0).toFixed(0)
}
</script>
