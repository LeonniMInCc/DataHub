<template>
  <div class="max-w-6xl mx-auto">
    <!-- Hero -->
    <div class="text-center py-16 md:py-20">
      <h1 class="text-4xl md:text-5xl font-bold text-white mb-4 leading-tight">
        开发者数字资产<br class="md:hidden" /><span class="text-transparent bg-clip-text bg-gradient-to-r from-primary to-accent">共享平台</span>
      </h1>
      <p class="text-slate-400 text-lg max-w-2xl mx-auto mb-8">
        发布、发现和订阅高质量的数据集与API接口，让数据流动起来
      </p>
      <div class="flex gap-4 justify-center">
        <router-link to="/assets" class="btn-primary px-8 py-3 text-lg">浏览资产</router-link>
        <router-link v-if="auth.isProvider" to="/publish" class="btn-accent px-8 py-3 text-lg">发布资产</router-link>
        <router-link v-else to="/register" class="btn-outline px-8 py-3 text-lg">立即注册</router-link>
      </div>
    </div>

    <!-- Stats -->
    <div class="grid grid-cols-2 md:grid-cols-4 gap-4 mb-16">
      <div class="glass-card p-6 text-center">
        <div class="text-3xl font-bold text-primary mb-1">{{ stats.totalUsers || '1,250' }}+</div>
        <div class="text-slate-400 text-sm">注册开发者</div>
      </div>
      <div class="glass-card p-6 text-center">
        <div class="text-3xl font-bold text-accent mb-1">{{ stats.totalAssets || '580' }}+</div>
        <div class="text-slate-400 text-sm">数据资产</div>
      </div>
      <div class="glass-card p-6 text-center">
        <div class="text-3xl font-bold text-purple-400 mb-1">{{ stats.totalSubscriptions || '3.2K' }}+</div>
        <div class="text-slate-400 text-sm">活跃订阅</div>
      </div>
      <div class="glass-card p-6 text-center">
        <div class="text-3xl font-bold text-amber-400 mb-1">{{ stats.totalApiCalls || '1.2M' }}+</div>
        <div class="text-slate-400 text-sm">API 调用</div>
      </div>
    </div>

    <!-- Features -->
    <div class="mb-16">
      <h2 class="text-2xl font-bold text-white text-center mb-8">平台特性</h2>
      <div class="grid md:grid-cols-3 gap-6">
        <div class="glass-card p-6">
          <div class="w-12 h-12 rounded-xl bg-primary/20 flex items-center justify-center mb-4">
            <svg class="w-6 h-6 text-primary" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 7v10c0 2 1 3 3 3h10c2 0 3-1 3-3V7M4 7c0-2 1-3 3-3h10c2 0 3 1 3 3M4 7h16M9 11h6"/></svg>
          </div>
          <h3 class="text-white font-semibold mb-2">数据资产发布</h3>
          <p class="text-slate-400 text-sm">轻松发布数据集和API接口，设置定价，管理订阅用户</p>
        </div>
        <div class="glass-card p-6">
          <div class="w-12 h-12 rounded-xl bg-accent/20 flex items-center justify-center mb-4">
            <svg class="w-6 h-6 text-accent" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z"/></svg>
          </div>
          <h3 class="text-white font-semibold mb-2">智能搜索发现</h3>
          <p class="text-slate-400 text-sm">强大的搜索和分类筛选功能，快速找到你需要的数据资产</p>
        </div>
        <div class="glass-card p-6">
          <div class="w-12 h-12 rounded-xl bg-purple-500/20 flex items-center justify-center mb-4">
            <svg class="w-6 h-6 text-purple-400" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 19v-6a2 2 0 00-2-2H5a2 2 0 00-2 2v6a2 2 0 002 2h2a2 2 0 002-2zm0 0V9a2 2 0 012-2h2a2 2 0 012 2v10m-6 0a2 2 0 002 2h2a2 2 0 002-2m0 0V5a2 2 0 012-2h2a2 2 0 012 2v14a2 2 0 01-2 2h-2a2 2 0 01-2-2z"/></svg>
          </div>
          <h3 class="text-white font-semibold mb-2">调用统计分析</h3>
          <p class="text-slate-400 text-sm">可视化图表展示调用趋势、资产热度，助力数据决策</p>
        </div>
      </div>
    </div>

    <!-- CTA -->
    <div class="glass-card p-8 text-center mb-10 bg-gradient-to-r from-primary/10 to-accent/10">
      <h2 class="text-2xl font-bold text-white mb-3">准备好开始了吗？</h2>
      <p class="text-slate-400 mb-6">加入我们，成为数据经济的一部分</p>
      <div class="flex gap-4 justify-center">
        <router-link to="/register" class="btn-primary px-8">免费注册</router-link>
        <router-link to="/assets" class="btn-outline px-8">浏览资产</router-link>
      </div>
    </div>
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
