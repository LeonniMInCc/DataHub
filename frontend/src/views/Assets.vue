<template>
  <div class="max-w-6xl mx-auto">
    <div class="flex flex-col md:flex-row md:items-center md:justify-between gap-4 mb-6">
      <h1 class="text-2xl font-bold text-white">资产市场</h1>
      <router-link v-if="auth.isProvider" to="/publish" class="btn-primary inline-flex items-center gap-2">
        <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 4v16m8-8H4"/></svg>
        发布资产
      </router-link>
    </div>

    <!-- Search & Filter -->
    <div class="glass-card p-4 mb-6 flex flex-col md:flex-row gap-3">
      <div class="flex-1 relative">
        <svg class="absolute left-3 top-1/2 -translate-y-1/2 w-5 h-5 text-slate-500" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z"/></svg>
        <input v-model="keyword" @input="search" class="glass-input pl-10" placeholder="搜索资产名称、描述、标签..." />
      </div>
      <select v-model="assetType" @change="search" class="glass-input w-full md:w-40">
        <option value="">全部类型</option>
        <option value="DATASET">数据集</option>
        <option value="API">API</option>
      </select>
    </div>

    <!-- Asset Grid -->
    <div v-if="loading" class="text-center py-20 text-slate-400">加载中...</div>

    <div v-else-if="assets.length === 0" class="text-center py-20">
      <div class="text-5xl mb-4">📦</div>
      <p class="text-slate-400 text-lg">暂无资产</p>
    </div>

    <div v-else class="grid md:grid-cols-2 lg:grid-cols-3 gap-4">
      <div v-for="asset in assets" :key="asset.assetId" class="glass-card p-5 hover:border-primary/50 transition-all cursor-pointer group" @click="$router.push(`/assets/${asset.assetId}`)">
        <div class="flex items-start justify-between mb-3">
          <span :class="asset.assetType === 'API' ? 'bg-primary/20 text-primary' : 'bg-accent/20 text-accent'" class="px-2.5 py-0.5 rounded-md text-xs font-semibold">
            {{ asset.assetType }}
          </span>
          <span class="text-primary font-bold text-sm">
            {{ asset.pricePerMonth > 0 ? '¥' + asset.pricePerMonth + '/月' : '免费' }}
          </span>
        </div>
        <h3 class="text-white font-semibold mb-2 group-hover:text-primary transition-colors line-clamp-1">{{ asset.title }}</h3>
        <p class="text-slate-400 text-sm mb-3 line-clamp-2">{{ asset.description || '暂无描述' }}</p>
        <div class="flex items-center justify-between text-xs text-slate-500">
          <span>{{ asset.provider?.username || '未知' }}</span>
          <span>{{ formatDate(asset.createdAt) }}</span>
        </div>
        <div v-if="asset.tags" class="flex flex-wrap gap-1 mt-3">
          <span v-for="tag in asset.tags.split(',')" :key="tag" class="px-2 py-0.5 bg-white/5 rounded text-xs text-slate-400">{{ tag.trim() }}</span>
        </div>
      </div>
    </div>

    <!-- Pagination -->
    <div v-if="totalPages > 1" class="flex justify-center gap-2 mt-8">
      <button @click="page--; loadAssets()" :disabled="page <= 0" class="btn-outline disabled:opacity-40">上一页</button>
      <span class="flex items-center px-4 text-slate-400 text-sm">第 {{ page + 1 }} / {{ totalPages }} 页</span>
      <button @click="page++; loadAssets()" :disabled="page >= totalPages - 1" class="btn-outline disabled:opacity-40">下一页</button>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useAuthStore } from '../stores/auth'
import api from '../api'

const auth = useAuthStore()
const assets = ref([])
const loading = ref(false)
const page = ref(0)
const totalPages = ref(1)
const keyword = ref('')
const assetType = ref('')
let searchTimer = null

function formatDate(d) {
  if (!d) return ''
  return new Date(d).toLocaleDateString('zh-CN')
}

async function loadAssets() {
  loading.value = true
  try {
    const params = { page: page.value, size: 12 }
    if (keyword.value) params.keyword = keyword.value
    if (assetType.value) params.assetType = assetType.value

    const res = await api.get('/assets', { params })
    assets.value = res.data?.content || []
    totalPages.value = res.data?.totalPages || 1
  } catch (e) {
    console.error(e)
  } finally {
    loading.value = false
  }
}

function search() {
  clearTimeout(searchTimer)
  page.value = 0
  searchTimer = setTimeout(loadAssets, 300)
}

onMounted(loadAssets)
</script>
