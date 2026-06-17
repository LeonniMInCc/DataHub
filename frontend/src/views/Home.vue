<template>
  <div class="max-w-none space-y-3">
    <section class="glass-card p-4">
      <div class="flex flex-col gap-3 md:flex-row md:items-center md:justify-between">
        <div>
          <p class="mb-1 text-xs font-semibold uppercase tracking-wider text-amber-600">DataHub</p>
          <h1 class="text-xl font-bold text-slate-900 md:text-2xl">数据资产工作台</h1>
          <p class="mt-1 text-sm text-slate-500">发现、订阅和发布 API / 数据集资产。</p>
        </div>
        <div class="flex flex-wrap gap-2">
          <router-link to="/assets" class="btn-primary">浏览资产</router-link>
          <router-link v-if="auth.isProvider" to="/publish" class="btn-accent">发布资产</router-link>
          <router-link v-else to="/register" class="btn-outline">注册账号</router-link>
        </div>
      </div>
    </section>

    <section class="grid grid-cols-2 gap-3 md:grid-cols-4">
      <div class="glass-card p-3">
        <div class="text-xs text-slate-500">注册开发者</div>
        <div class="mt-1 text-2xl font-bold text-amber-600">{{ stats.totalUsers || '1,250' }}+</div>
      </div>
      <div class="glass-card p-3">
        <div class="text-xs text-slate-500">数据资产</div>
        <div class="mt-1 text-2xl font-bold text-slate-900">{{ stats.totalAssets || '580' }}+</div>
      </div>
      <div class="glass-card p-3">
        <div class="text-xs text-slate-500">活跃订阅</div>
        <div class="mt-1 text-2xl font-bold text-slate-900">{{ stats.totalSubscriptions || '3.2K' }}+</div>
      </div>
      <div class="glass-card p-3">
        <div class="text-xs text-slate-500">API 调用</div>
        <div class="mt-1 text-2xl font-bold text-amber-600">{{ stats.totalApiCalls || '1.2M' }}+</div>
      </div>
    </section>

    <section class="grid gap-3 lg:grid-cols-[260px_minmax(0,1fr)]">
      <div class="glass-card">
        <div class="border-b border-slate-200 px-3 py-2 text-sm font-semibold text-slate-900">快速入口</div>
        <div class="divide-y divide-slate-200">
          <router-link to="/assets" class="flex items-center justify-between px-3 py-2 text-sm hover:bg-amber-50">
            <span class="text-slate-700">资产市场</span>
            <span class="text-xs text-amber-600">Market</span>
          </router-link>
          <router-link to="/dashboard" class="flex items-center justify-between px-3 py-2 text-sm hover:bg-amber-50">
            <span class="text-slate-700">数据仪表盘</span>
            <span class="text-xs text-amber-600">Dashboard</span>
          </router-link>
          <router-link v-if="auth.isLoggedIn" to="/subscriptions" class="flex items-center justify-between px-3 py-2 text-sm hover:bg-amber-50">
            <span class="text-slate-700">我的订阅</span>
            <span class="text-xs text-amber-600">Subs</span>
          </router-link>
        </div>
      </div>

      <div class="glass-card">
        <div class="border-b border-slate-200 px-3 py-2 text-sm font-semibold text-slate-900">平台流程</div>
        <div class="grid divide-y divide-slate-200 md:grid-cols-3 md:divide-x md:divide-y-0">
          <div class="p-3">
            <div class="mb-2 inline-flex rounded-sm border border-amber-200 bg-amber-50 px-2 py-0.5 text-xs font-semibold text-amber-700">发布</div>
            <h3 class="text-sm font-semibold text-slate-900">提交资产信息</h3>
            <p class="mt-1 text-xs leading-5 text-slate-500">登记数据集、API 端点、价格和标签。</p>
          </div>
          <div class="p-3">
            <div class="mb-2 inline-flex rounded-sm border border-amber-200 bg-amber-50 px-2 py-0.5 text-xs font-semibold text-amber-700">发现</div>
            <h3 class="text-sm font-semibold text-slate-900">按类型检索</h3>
            <p class="mt-1 text-xs leading-5 text-slate-500">通过关键字、类型和排序快速定位资产。</p>
          </div>
          <div class="p-3">
            <div class="mb-2 inline-flex rounded-sm border border-amber-200 bg-amber-50 px-2 py-0.5 text-xs font-semibold text-amber-700">订阅</div>
            <h3 class="text-sm font-semibold text-slate-900">查看用量状态</h3>
            <p class="mt-1 text-xs leading-5 text-slate-500">在订阅和仪表盘中跟踪额度与调用趋势。</p>
          </div>
        </div>
      </div>
    </section>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useAuthStore } from '../stores/auth'
import api from '../api'

const auth = useAuthStore()
const stats = ref({})

onMounted(async () => {
  try {
    const res = await api.get('/dashboard')
    stats.value = res.data || {}
  } catch (e) { /* fallback to defaults */ }
})
</script>
