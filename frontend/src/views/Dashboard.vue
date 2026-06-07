<template>
  <div class="max-w-6xl mx-auto">
    <h1 class="text-2xl font-bold text-white mb-6">数据仪表盘</h1>

    <!-- KPI Cards -->
    <div class="grid grid-cols-2 md:grid-cols-4 gap-4 mb-8">
      <div class="glass-card p-5">
        <div class="flex items-center justify-between mb-2">
          <span class="text-slate-400 text-sm">用户总数</span>
          <div class="w-8 h-8 rounded-lg bg-primary/20 flex items-center justify-center">
            <svg class="w-4 h-4 text-primary" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 4.354a4 4 0 110 5.292M15 21H3v-1a6 6 0 0112 0v1zm0 0h6v-1a6 6 0 00-9-5.197M13 7a4 4 0 11-8 0 4 4 0 018 0z"/></svg>
          </div>
        </div>
        <div class="text-3xl font-bold text-white">{{ data.totalUsers }}</div>
        <div class="text-xs text-accent mt-1">+12% 本月增长</div>
      </div>
      <div class="glass-card p-5">
        <div class="flex items-center justify-between mb-2">
          <span class="text-slate-400 text-sm">资产总数</span>
          <div class="w-8 h-8 rounded-lg bg-accent/20 flex items-center justify-center">
            <svg class="w-4 h-4 text-accent" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M20 7l-8-4-8 4m16 0l-8 4m8-4v10l-8 4m0-10L4 7m8 4v10M4 7v10l8 4"/></svg>
          </div>
        </div>
        <div class="text-3xl font-bold text-white">{{ data.totalAssets }}</div>
        <div class="text-xs text-accent mt-1">+5% 本月新增</div>
      </div>
      <div class="glass-card p-5">
        <div class="flex items-center justify-between mb-2">
          <span class="text-slate-400 text-sm">订阅总数</span>
          <div class="w-8 h-8 rounded-lg bg-purple-500/20 flex items-center justify-center">
            <svg class="w-4 h-4 text-purple-400" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 5v2m0 4v2m0 4v2M5 5a2 2 0 00-2 2v3a2 2 0 110 4v3a2 2 0 002 2h14a2 2 0 002-2v-3a2 2 0 110-4V7a2 2 0 00-2-2H5z"/></svg>
          </div>
        </div>
        <div class="text-3xl font-bold text-white">{{ data.totalSubscriptions }}</div>
        <div class="text-xs text-purple-400 mt-1">+18% 本月增长</div>
      </div>
      <div class="glass-card p-5">
        <div class="flex items-center justify-between mb-2">
          <span class="text-slate-400 text-sm">API 调用</span>
          <div class="w-8 h-8 rounded-lg bg-amber-500/20 flex items-center justify-center">
            <svg class="w-4 h-4 text-amber-400" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M13 10V3L4 14h7v7l9-11h-7z"/></svg>
          </div>
        </div>
        <div class="text-3xl font-bold text-white">{{ data.totalApiCalls }}</div>
        <div class="text-xs text-amber-400 mt-1">+24% 本月增长</div>
      </div>
    </div>

    <!-- Charts -->
    <div class="grid md:grid-cols-2 gap-6 mb-6">
      <div class="glass-card p-5">
        <h3 class="text-white font-semibold mb-4">资产类型分布</h3>
        <div ref="pieChart" style="height: 300px;"></div>
      </div>
      <div class="glass-card p-5">
        <h3 class="text-white font-semibold mb-4">TOP5 热门资产</h3>
        <div ref="barChart" style="height: 300px;"></div>
      </div>
    </div>

    <div class="glass-card p-5 mb-6">
      <h3 class="text-white font-semibold mb-4">最近7天调用趋势</h3>
      <div ref="lineChart" style="height: 300px;"></div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, nextTick } from 'vue'
import * as echarts from 'echarts'
import api from '../api'

const data = reactive({
  totalUsers: 0, totalAssets: 0, totalSubscriptions: 0, totalApiCalls: 0,
  assetTypeDistribution: [], last7DaysCalls: [], top5Assets: []
})

const pieChart = ref(null)
const barChart = ref(null)
const lineChart = ref(null)

function initPieChart() {
  if (!pieChart.value) return
  const chart = echarts.init(pieChart.value, 'dark')
  chart.setOption({
    tooltip: { trigger: 'item' },
    legend: { bottom: 0, textStyle: { color: '#94A3B8' } },
    series: [{
      type: 'pie',
      radius: ['40%', '70%'],
      center: ['50%', '45%'],
      itemStyle: { borderRadius: 6, borderColor: '#0F172A', borderWidth: 3 },
      label: { show: false },
      data: data.assetTypeDistribution.length > 0
        ? data.assetTypeDistribution.map(d => ({ name: d.name, value: d.value }))
        : [{ name: 'DATASET', value: 8 }, { name: 'API', value: 7 }]
    }]
  })
  window.addEventListener('resize', () => chart.resize())
}

function initBarChart() {
  if (!barChart.value) return
  const chart = echarts.init(barChart.value, 'dark')
  const names = data.top5Assets.map(a => a.title?.substring(0, 10)) || []
  const values = data.top5Assets.map(a => a.subscriberCount) || []
  chart.setOption({
    tooltip: { trigger: 'axis', axisPointer: { type: 'shadow' } },
    grid: { left: '3%', right: '10%', bottom: '3%', containLabel: true },
    xAxis: { type: 'value', axisLabel: { color: '#94A3B8' } },
    yAxis: { type: 'category', data: names.length ? names : ['资产A', '资产B', '资产C'], axisLabel: { color: '#94A3B8' } },
    series: [{
      type: 'bar',
      data: values.length ? values : [20, 18, 15],
      itemStyle: {
        borderRadius: [0, 6, 6, 0],
        color: new echarts.graphic.LinearGradient(0, 0, 1, 0, [
          { offset: 0, color: '#3B82F6' }, { offset: 1, color: '#10B981' }
        ])
      }
    }]
  })
  window.addEventListener('resize', () => chart.resize())
}

function initLineChart() {
  if (!lineChart.value) return
  const chart = echarts.init(lineChart.value, 'dark')
  const dates = data.last7DaysCalls.map(d => d.date?.substring(5)) || []
  const counts = data.last7DaysCalls.map(d => d.count) || []
  chart.setOption({
    tooltip: { trigger: 'axis' },
    grid: { left: '3%', right: '4%', bottom: '3%', containLabel: true },
    xAxis: { type: 'category', data: dates.length ? dates : ['06-01','06-02','06-03','06-04','06-05','06-06','06-07'], axisLabel: { color: '#94A3B8' } },
    yAxis: { type: 'value', axisLabel: { color: '#94A3B8' } },
    series: [{
      type: 'line',
      data: counts.length ? counts : [45, 52, 38, 67, 41, 55, 48],
      smooth: true,
      lineStyle: { color: '#3B82F6', width: 3 },
      itemStyle: { color: '#3B82F6' },
      areaStyle: { color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [{ offset: 0, color: 'rgba(59,130,246,0.3)' }, { offset: 1, color: 'rgba(59,130,246,0)' }]) }
    }]
  })
  window.addEventListener('resize', () => chart.resize())
}

onMounted(async () => {
  try {
    const res = await api.get('/dashboard')
    if (res.data) {
      Object.assign(data, res.data)
    }
  } catch (e) { /* use defaults */ }

  await nextTick()
  initPieChart()
  initBarChart()
  initLineChart()
})
</script>
