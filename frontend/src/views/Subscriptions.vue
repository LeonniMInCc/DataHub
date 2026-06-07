<template>
  <div class="max-w-4xl mx-auto">
    <h1 class="text-2xl font-bold text-white mb-6">我的订阅</h1>

    <div v-if="loading" class="text-center py-20 text-slate-400">加载中...</div>

    <div v-else-if="subscriptions.length === 0" class="glass-card p-12 text-center">
      <div class="text-5xl mb-3">📭</div>
      <p class="text-slate-400 text-lg mb-4">暂无订阅</p>
      <router-link to="/assets" class="btn-primary">去浏览资产</router-link>
    </div>

    <div v-else class="space-y-3">
      <div v-for="sub in subscriptions" :key="sub.subId" class="glass-card p-4 flex flex-col md:flex-row md:items-center justify-between gap-3">
        <div class="flex items-center gap-3 flex-1 min-w-0">
          <div :class="sub.asset?.assetType === 'API' ? 'bg-primary/20' : 'bg-accent/20'" class="w-10 h-10 rounded-lg flex items-center justify-center shrink-0">
            <span class="text-sm font-bold" :class="sub.asset?.assetType === 'API' ? 'text-primary' : 'text-accent'">{{ sub.asset?.assetType === 'API' ? 'API' : 'DS' }}</span>
          </div>
          <div class="min-w-0">
            <h3 class="text-white font-medium truncate">{{ sub.asset?.title || '未知资产' }}</h3>
            <div class="flex gap-3 text-xs text-slate-500 mt-1">
              <span>{{ sub.startDate }} ~ {{ sub.endDate }}</span>
              <span :class="sub.status === 'ACTIVE' ? 'text-green-400' : sub.status === 'EXPIRED' ? 'text-amber-400' : 'text-red-400'">
                {{ sub.status === 'ACTIVE' ? '活跃' : sub.status === 'EXPIRED' ? '已过期' : '已取消' }}
              </span>
            </div>
          </div>
        </div>

        <div class="flex items-center gap-4">
          <div class="text-right">
            <div class="text-xs text-slate-500">额度使用</div>
            <div class="text-sm text-white font-medium">{{ sub.quotaUsed }} / {{ sub.quotaLimit }}</div>
            <div class="w-24 h-1.5 bg-slate-700 rounded-full mt-1">
              <div class="h-full bg-primary rounded-full transition-all" :style="{ width: Math.min(100, (sub.quotaUsed / sub.quotaLimit) * 100) + '%' }"></div>
            </div>
          </div>
          <button v-if="sub.status === 'ACTIVE'" @click="handleCancel(sub)" class="btn-outline text-red-400 hover:border-red-400 hover:text-red-300 text-sm">
            取消订阅
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import api from '../api'

const subscriptions = ref([])
const loading = ref(false)

async function loadSubs() {
  loading.value = true
  try {
    const res = await api.get('/subscriptions/my')
    subscriptions.value = res.data || []
  } catch (e) {
    console.error(e)
  } finally {
    loading.value = false
  }
}

async function handleCancel(sub) {
  if (!confirm('确定要取消订阅吗？')) return
  try {
    await api.delete(`/subscriptions/${sub.subId}`)
    sub.status = 'CANCELLED'
  } catch (e) {
    alert(e.message)
  }
}

onMounted(loadSubs)
</script>
