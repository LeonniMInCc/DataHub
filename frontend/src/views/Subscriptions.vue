<template>
  <div class="mx-auto max-w-4xl space-y-3">
    <div>
      <h1 class="text-xl font-bold text-slate-900">我的订阅</h1>
      <p class="text-sm text-slate-500">查看订阅周期、状态和额度使用。</p>
    </div>

    <div v-if="loading" class="py-12 text-center text-sm text-slate-500">加载中...</div>

    <div v-else-if="subscriptions.length === 0" class="glass-card p-4 text-center">
      <p class="mb-3 text-sm text-slate-500">暂无订阅</p>
      <router-link to="/assets" class="btn-primary">去浏览资产</router-link>
    </div>

    <div v-else class="divide-y divide-slate-200 border border-slate-200 bg-white">
      <div v-for="sub in subscriptions" :key="sub.subId" class="flex flex-col justify-between gap-3 p-3 md:flex-row md:items-center">
        <div class="flex items-center gap-3 flex-1 min-w-0">
          <div class="flex h-9 w-9 shrink-0 items-center justify-center rounded-sm border border-amber-200 bg-amber-50">
            <span class="text-xs font-bold text-amber-700">{{ sub.asset?.assetType === 'API' ? 'API' : 'DS' }}</span>
          </div>
          <div class="min-w-0">
            <h3 class="truncate font-medium text-slate-900">{{ sub.asset?.title || '未知资产' }}</h3>
            <div class="mt-1 flex flex-wrap gap-3 text-xs text-slate-500">
              <span>{{ sub.startDate }} ~ {{ sub.endDate }}</span>
              <span :class="statusClass(sub.status)">
                {{ statusText(sub.status) }}
              </span>
            </div>
          </div>
        </div>

        <div class="flex items-center gap-4">
          <div class="text-right">
            <div class="text-xs text-slate-400">额度使用</div>
            <div class="text-sm font-medium text-slate-900">{{ sub.quotaUsed }} / {{ sub.quotaLimit }}</div>
            <div class="mt-1 h-1.5 w-24 rounded-sm bg-slate-100">
              <div class="h-full rounded-sm bg-amber-500 transition-all" :style="{ width: Math.min(100, (sub.quotaUsed / sub.quotaLimit) * 100) + '%' }"></div>
            </div>
          </div>
          <button v-if="sub.status === 'ACTIVE'" @click="handleCancel(sub)" class="btn-outline text-red-600 hover:border-red-500 hover:text-red-700 text-sm">
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

function statusText(status) {
  const textMap = {
    PENDING_PAYMENT: '待支付',
    ACTIVE: '活跃',
    FAILED: '支付失败',
    EXPIRED: '已过期',
    CANCELLED: '已取消'
  }
  return textMap[status] || status
}

function statusClass(status) {
  const classMap = {
    PENDING_PAYMENT: 'text-amber-600',
    ACTIVE: 'text-green-600',
    FAILED: 'text-red-600',
    EXPIRED: 'text-amber-600',
    CANCELLED: 'text-gray-500'
  }
  return classMap[status] || 'text-gray-500'
}

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
