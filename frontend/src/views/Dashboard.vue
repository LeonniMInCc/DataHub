<template>
  <div class="max-w-none space-y-3">
    <div>
      <h1 class="text-xl font-bold text-slate-900">数据仪表盘</h1>
      <p class="text-sm text-slate-500">汇总平台资产、订阅和调用数据。</p>
    </div>

    <div class="border border-slate-200 bg-white md:grid md:grid-cols-[176px_minmax(0,1fr)]">
      <aside class="hidden border-r border-slate-200 p-3 md:block">
        <div class="text-xs font-semibold uppercase tracking-wider text-amber-600">Dashboard</div>
        <div class="mt-2 text-sm font-semibold text-slate-900">数据概览</div>
        <dl class="mt-3 space-y-2 text-xs">
          <div class="flex justify-between">
            <dt class="text-slate-500">用户</dt>
            <dd class="font-medium text-slate-900">{{ data.totalUsers }}</dd>
          </div>
          <div class="flex justify-between">
            <dt class="text-slate-500">资产</dt>
            <dd class="font-medium text-slate-900">{{ data.totalAssets }}</dd>
          </div>
          <div class="flex justify-between">
            <dt class="text-slate-500">订阅</dt>
            <dd class="font-medium text-slate-900">{{ data.totalSubscriptions }}</dd>
          </div>
          <div class="flex justify-between">
            <dt class="text-slate-500">调用</dt>
            <dd class="font-medium text-slate-900">{{ data.totalApiCalls }}</dd>
          </div>
        </dl>
      </aside>

      <section class="min-w-0">
        <!-- KPI Cards -->
        <div class="grid grid-cols-2 gap-3 border-b border-slate-200 p-3 lg:grid-cols-4">
          <div class="glass-card p-3">
            <div class="flex items-center justify-between">
              <span class="text-xs text-slate-500">用户总数</span>
              <div class="flex h-7 w-7 items-center justify-center rounded-sm bg-amber-50 text-amber-600">
                <svg class="h-4 w-4" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 4.354a4 4 0 110 5.292M15 21H3v-1a6 6 0 0112 0v1zm0 0h6v-1a6 6 0 00-9-5.197M13 7a4 4 0 11-8 0 4 4 0 018 0z"/></svg>
              </div>
            </div>
            <div class="mt-2 text-2xl font-bold text-slate-900">{{ data.totalUsers }}</div>
            <div class="mt-1 text-xs text-amber-600">+12% 本月增长</div>
          </div>
          <div class="glass-card p-3">
            <div class="flex items-center justify-between">
              <span class="text-xs text-slate-500">资产总数</span>
              <div class="flex h-7 w-7 items-center justify-center rounded-sm bg-amber-50 text-amber-600">
                <svg class="h-4 w-4" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M20 7l-8-4-8 4m16 0l-8 4m8-4v10l-8 4m0-10L4 7m8 4v10M4 7v10l8 4"/></svg>
              </div>
            </div>
            <div class="mt-2 text-2xl font-bold text-slate-900">{{ data.totalAssets }}</div>
            <div class="mt-1 text-xs text-amber-600">+5% 本月新增</div>
          </div>
          <div class="glass-card p-3">
            <div class="flex items-center justify-between">
              <span class="text-xs text-slate-500">订阅总数</span>
              <div class="flex h-7 w-7 items-center justify-center rounded-sm bg-amber-50 text-amber-600">
                <svg class="h-4 w-4" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 5v2m0 4v2m0 4v2M5 5h14a2 2 0 012 2v3a2 2 0 010 4v3a2 2 0 01-2 2H5a2 2 0 01-2-2v-3a2 2 0 010-4V7a2 2 0 012-2z"/></svg>
              </div>
            </div>
            <div class="mt-2 text-2xl font-bold text-slate-900">{{ data.totalSubscriptions }}</div>
            <div class="mt-1 text-xs text-amber-600">+18% 本月增长</div>
          </div>
          <div class="glass-card p-3">
            <div class="flex items-center justify-between">
              <span class="text-xs text-slate-500">API 调用</span>
              <div class="flex h-7 w-7 items-center justify-center rounded-sm bg-amber-50 text-amber-600">
                <svg class="h-4 w-4" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M13 10V3L4 14h7v7l9-11h-7z"/></svg>
              </div>
            </div>
            <div class="mt-2 text-2xl font-bold text-slate-900">{{ data.totalApiCalls }}</div>
            <div class="mt-1 text-xs text-amber-600">+24% 本月增长</div>
          </div>
        </div>

        <div class="border-b border-slate-200 px-3">
          <nav class="relative flex gap-4 overflow-x-auto">
            <button
              v-for="(tab, index) in panelTabs"
              :key="tab.value"
              :ref="el => setPanelTabRef(el, index)"
              type="button"
              @click="selectPanel(tab.value)"
              :class="activePanel === tab.value ? 'text-amber-600' : 'text-slate-500 hover:text-slate-700'"
              class="whitespace-nowrap px-1 py-2 text-sm font-medium transition-colors duration-300"
            >
              {{ tab.label }}
            </button>
            <span class="pointer-events-none absolute bottom-0 h-0.5 bg-amber-500 transition-all duration-300 ease-out" :style="panelLineStyle"></span>
          </nav>
        </div>

        <div class="p-3">
          <div v-show="activePanel === 'types'" class="glass-card">
            <div class="border-b border-slate-200 px-3 py-2 text-sm font-semibold text-slate-900">资产类型分布</div>
            <div ref="pieChart" class="h-[300px]"></div>
          </div>
          <div v-show="activePanel === 'ranking'" class="glass-card">
            <div class="border-b border-slate-200 px-3 py-2 text-sm font-semibold text-slate-900">TOP5 热门资产</div>
            <div ref="barChart" class="h-[300px]"></div>
          </div>
          <div v-show="activePanel === 'calls'" class="glass-card">
            <div class="border-b border-slate-200 px-3 py-2 text-sm font-semibold text-slate-900">最近 7 天调用趋势</div>
            <div ref="lineChart" class="h-[300px]"></div>
          </div>
        </div>
      </section>
    </div>
  </div>
</template>

<script setup>
import { computed, ref, reactive, onMounted, nextTick, watch, onBeforeUnmount, onBeforeUpdate } from 'vue'
import * as echarts from 'echarts'
import api from '../api'

const data = reactive({
  totalUsers: 0, totalAssets: 0, totalSubscriptions: 0, totalApiCalls: 0,
  assetTypeDistribution: [], last7DaysCalls: [], top5Assets: []
})

const activePanel = ref('types')
const panelTabs = [
  { label: '类型分布', value: 'types' },
  { label: '热门资产', value: 'ranking' },
  { label: '调用趋势', value: 'calls' }
]

const pieChart = ref(null)
const barChart = ref(null)
const lineChart = ref(null)
const panelTabRefs = ref([])
const panelLineStyle = ref({ width: '0px', transform: 'translateX(0px)' })
const activePanelIndex = computed(() => panelTabs.findIndex(tab => tab.value === activePanel.value))

const chartMotion = {
  animation: true,
  animationDuration: 700,
  animationEasing: 'cubicOut',
  animationDurationUpdate: 450,
  animationEasingUpdate: 'cubicOut'
}

function setChartOption(elRef, option) {
  if (!elRef.value) return
  let chart = echarts.getInstanceByDom(elRef.value)
  if (!chart) chart = echarts.init(elRef.value)
  chart.setOption({ ...chartMotion, ...option })
  chart.resize()
}

function setPanelTabRef(el, index) {
  if (el) panelTabRefs.value[index] = el
}

function updatePanelLine() {
  const activeEl = panelTabRefs.value[activePanelIndex.value]
  if (!activeEl) return
  panelLineStyle.value = {
    width: `${activeEl.offsetWidth}px`,
    transform: `translateX(${activeEl.offsetLeft}px)`
  }
}

async function selectPanel(value) {
  activePanel.value = value
  await nextTick()
  updatePanelLine()
}

function initPieChart() {
  setChartOption(pieChart, {
    tooltip: { trigger: 'item' },
    color: ['#F59E0B', '#EAB308', '#FBBF24', '#FDE68A'],
    legend: { bottom: 0, textStyle: { color: '#64748B' } },
    series: [{
      type: 'pie',
      radius: ['42%', '68%'],
      center: ['50%', '45%'],
      itemStyle: { borderRadius: 0, borderColor: '#FFFFFF', borderWidth: 1 },
      label: { show: false },
      data: data.assetTypeDistribution.length > 0
        ? data.assetTypeDistribution.map(d => ({ name: d.name, value: d.value }))
        : [{ name: 'DATASET', value: 8 }, { name: 'API', value: 7 }]
    }]
  })
}

function initBarChart() {
  const names = data.top5Assets.map(a => a.title?.substring(0, 10)) || []
  const values = data.top5Assets.map(a => a.subscriberCount) || []
  setChartOption(barChart, {
    tooltip: { trigger: 'axis', axisPointer: { type: 'line' } },
    grid: { left: '3%', right: '8%', bottom: '3%', containLabel: true },
    xAxis: { type: 'value', axisLabel: { color: '#64748B' }, splitLine: { lineStyle: { color: '#E2E8F0' } } },
    yAxis: { type: 'category', data: names.length ? names : ['资产A', '资产B', '资产C'], axisLabel: { color: '#64748B' } },
    series: [{
      type: 'bar',
      data: values.length ? values : [20, 18, 15],
      itemStyle: { borderRadius: 0, color: '#F59E0B' },
      barWidth: 16
    }]
  })
}

function initLineChart() {
  const dates = data.last7DaysCalls.map(d => d.date?.substring(5)) || []
  const counts = data.last7DaysCalls.map(d => d.count) || []
  setChartOption(lineChart, {
    tooltip: { trigger: 'axis' },
    grid: { left: '3%', right: '4%', bottom: '3%', containLabel: true },
    xAxis: { type: 'category', data: dates.length ? dates : ['06-01','06-02','06-03','06-04','06-05','06-06','06-07'], axisLabel: { color: '#64748B' } },
    yAxis: { type: 'value', axisLabel: { color: '#64748B' }, splitLine: { lineStyle: { color: '#E2E8F0' } } },
    series: [{
      type: 'line',
      data: counts.length ? counts : [45, 52, 38, 67, 41, 55, 48],
      smooth: false,
      lineStyle: { color: '#F59E0B', width: 2 },
      itemStyle: { color: '#F59E0B' },
      areaStyle: { color: 'rgba(245,158,11,0.12)' }
    }]
  })
}

function initActiveChart() {
  if (activePanel.value === 'types') initPieChart()
  if (activePanel.value === 'ranking') initBarChart()
  if (activePanel.value === 'calls') initLineChart()
}

function resizeCharts() {
  ;[pieChart.value, barChart.value, lineChart.value].forEach(el => {
    if (el) echarts.getInstanceByDom(el)?.resize()
  })
}

watch(activePanel, async () => {
  await nextTick()
  updatePanelLine()
  initActiveChart()
})

onBeforeUpdate(() => {
  panelTabRefs.value = []
})

onMounted(async () => {
  try {
    const res = await api.get('/dashboard')
    if (res.data) {
      Object.assign(data, res.data)
    }
  } catch (e) { /* use defaults */ }

  await nextTick()
  updatePanelLine()
  initActiveChart()
  window.addEventListener('resize', resizeCharts)
})

onBeforeUnmount(() => {
  window.removeEventListener('resize', resizeCharts)
  ;[pieChart.value, barChart.value, lineChart.value].forEach(el => {
    if (el) echarts.getInstanceByDom(el)?.dispose()
  })
})
</script>
