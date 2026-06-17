<template>
  <div class="mx-auto max-w-5xl space-y-3" v-if="asset">
    <!-- Header -->
    <div class="glass-card p-4">
      <div class="flex items-start justify-between flex-wrap gap-4">
        <div class="flex-1">
          <div class="mb-2 flex items-center gap-2">
            <span class="rounded-sm border border-amber-200 bg-amber-50 px-2 py-0.5 text-xs font-semibold text-amber-700">
              {{ asset.assetType === 'API' ? 'API' : '数据集' }}
            </span>
            <span :class="asset.status === 'ACTIVE' ? 'border-green-200 bg-green-50 text-green-700' : 'border-slate-200 bg-slate-50 text-slate-500'" class="rounded-sm border px-2 py-0.5 text-xs">
              {{ asset.status === 'ACTIVE' ? '活跃' : '已下线' }}
            </span>
          </div>
          <h1 class="mb-1 text-xl font-bold text-slate-900">{{ asset.title }}</h1>
          <p class="text-sm text-slate-500">{{ asset.description }}</p>
        </div>
        <div class="text-right">
          <div class="mb-1 text-2xl font-bold text-amber-600">
            {{ asset.pricePerMonth > 0 ? '¥' + asset.pricePerMonth : '免费' }}
          </div>
          <div class="text-xs text-slate-400">每月</div>
          <button v-if="auth.isLoggedIn && !isOwner" @click="openPaymentModal" :disabled="subscribing || subscribed" class="btn-primary mt-2 w-full">
            {{ subscribing ? '处理中...' : subscribed ? '已订阅' : '立即订阅' }}
          </button>
          <button v-if="!auth.isLoggedIn" @click="$router.push('/login')" class="btn-primary mt-2 w-full">登录后订阅</button>
        </div>
      </div>
    </div>

    <div class="grid gap-3 md:grid-cols-3">
      <!-- Details -->
      <div class="space-y-3 md:col-span-2">
        <div class="glass-card p-4">
          <h3 class="mb-3 font-semibold text-slate-900">资产详情</h3>
          <dl class="divide-y divide-slate-200">
            <div class="flex justify-between py-2 text-sm">
              <dt class="text-slate-500">提供者</dt>
              <dd class="text-slate-900">{{ asset.provider?.username || '未知' }}</dd>
            </div>
            <div class="flex justify-between py-2 text-sm">
              <dt class="text-slate-500">订阅人数</dt>
              <dd class="text-slate-900">{{ subscriberCount }} 人</dd>
            </div>
            <div class="flex justify-between py-2 text-sm">
              <dt class="text-slate-500">创建时间</dt>
              <dd class="text-slate-900">{{ formatDate(asset.createdAt) }}</dd>
            </div>
            <div class="flex justify-between gap-4 py-2 text-sm" v-if="asset.endpointUrl">
              <dt class="shrink-0 text-slate-500">API 端点</dt>
              <dd class="break-all font-mono text-xs text-amber-600">{{ asset.endpointUrl }}</dd>
            </div>
          </dl>
        </div>

        <div class="glass-card p-4" v-if="asset.assetType === 'API'">
          <h3 class="mb-3 font-semibold text-slate-900">调用示例</h3>
          <div class="rounded-sm border border-slate-200 bg-slate-50 p-3">
            <pre class="overflow-x-auto text-sm text-green-600"><code>curl -X GET "{{ asset.endpointUrl || 'https://api.datahub.io/v1/example' }}" \
  -H "Authorization: Bearer YOUR_API_KEY" \
  -H "Content-Type: application/json"</code></pre>
          </div>
        </div>

        <div class="glass-card p-4" v-if="asset.tags">
          <h3 class="mb-3 font-semibold text-slate-900">标签</h3>
          <div class="flex flex-wrap gap-1">
            <span v-for="tag in asset.tags.split(',')" :key="tag" class="rounded-sm border border-slate-200 bg-slate-50 px-2 py-0.5 text-xs text-slate-600">{{ tag.trim() }}</span>
          </div>
        </div>
      </div>

      <!-- Sidebar -->
      <div class="space-y-3">
        <div class="glass-card p-4 text-center">
          <img :src="asset.provider?.avatar || '/vite.svg'" class="mx-auto mb-3 h-14 w-14 rounded-sm bg-slate-100" alt="" />
          <h4 class="font-medium text-slate-900">{{ asset.provider?.username || '未知提供者' }}</h4>
          <p class="mt-1 text-sm text-slate-400">{{ asset.provider?.role === 'PROVIDER' ? '数据提供者' : '用户' }}</p>
        </div>

        <div class="glass-card p-4">
          <h4 class="mb-3 text-sm font-medium text-slate-900">资产统计</h4>
          <div class="space-y-2 text-sm">
            <div class="flex justify-between"><span class="text-slate-500">类型</span><span class="text-slate-900">{{ asset.assetType }}</span></div>
            <div class="flex justify-between"><span class="text-slate-500">状态</span><span class="text-green-600">ACTIVE</span></div>
            <div class="flex justify-between"><span class="text-slate-500">订阅数</span><span class="text-slate-900">{{ subscriberCount }}</span></div>
            <div class="flex justify-between"><span class="text-slate-500">定价</span><span class="font-semibold text-amber-600">¥{{ asset.pricePerMonth }}/月</span></div>
          </div>
        </div>
      </div>
    </div>

    <div v-if="showPaymentModal" class="fixed inset-0 z-50 flex items-center justify-center bg-black/40 px-4">
      <div class="w-full max-w-md rounded-sm border border-slate-200 bg-white">
        <div class="flex items-center justify-between border-b border-slate-200 px-4 py-3">
          <h3 class="text-base font-semibold text-slate-900">订阅支付验证</h3>
          <button @click="closePaymentModal" :disabled="subscribing" class="text-2xl leading-none text-slate-400 hover:text-slate-600">&times;</button>
        </div>
        <div class="px-4 py-4 text-center">
          <img src="/付款码.jpg" alt="付款码" class="mx-auto h-64 w-64 rounded-sm border border-slate-200 object-contain" />
          <p class="mt-4 text-sm leading-6 text-slate-600">
            请使用手机扫码支付。支付完成后，请点击下方按钮确认付款状态。
          </p>
          <div class="mt-4 flex justify-center gap-2 text-xs">
            <button @click="confirmPayment(true)" :disabled="subscribing" class="rounded-sm border border-green-200 px-3 py-1.5 text-green-700 hover:bg-green-50 disabled:opacity-50">
              模拟支付成功
            </button>
            <button @click="confirmPayment(false)" :disabled="subscribing" class="rounded-sm border border-red-200 px-3 py-1.5 text-red-700 hover:bg-red-50 disabled:opacity-50">
              模拟支付失败
            </button>
          </div>
        </div>
        <div class="flex justify-end gap-2 border-t border-slate-200 px-4 py-3">
          <button @click="closePaymentModal" :disabled="subscribing" class="btn-outline">取消</button>
          <button @click="confirmPayment()" :disabled="subscribing" class="btn-primary">
            {{ subscribing ? '确认中...' : '已完成支付（确认状态）' }}
          </button>
        </div>
      </div>
    </div>

    <div v-if="toast.message" :class="toast.type === 'success' ? 'bg-green-600' : 'bg-red-600'" class="fixed right-4 top-4 z-50 rounded-sm px-3 py-2 text-sm text-white">
      {{ toast.message }}
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useAuthStore } from '../stores/auth'
import api from '../api'

const route = useRoute()
const router = useRouter()
const auth = useAuthStore()

const asset = ref(null)
const subscriberCount = ref(0)
const subscribed = ref(false)
const subscribing = ref(false)
const showPaymentModal = ref(false)
const toast = ref({ message: '', type: 'success' })
let toastTimer = null

const isOwner = computed(() => {
  if (!auth.isLoggedIn || !asset.value) return false
  return auth.user?.devId === asset.value.providerId
})

function formatDate(d) {
  if (!d) return ''
  return new Date(d).toLocaleDateString('zh-CN', { year: 'numeric', month: 'long', day: 'numeric' })
}

function showToast(message, type = 'success') {
  clearTimeout(toastTimer)
  toast.value = { message, type }
  toastTimer = setTimeout(() => {
    toast.value.message = ''
  }, 3000)
}

function openPaymentModal() {
  if (subscribed.value) return
  showPaymentModal.value = true
}

function closePaymentModal() {
  if (subscribing.value) return
  showPaymentModal.value = false
}

async function confirmPayment(mockPaymentSuccess = null) {
  subscribing.value = true
  try {
    const payload = { assetId: asset.value.assetId }
    if (mockPaymentSuccess !== null) {
      payload.mockPaymentSuccess = mockPaymentSuccess
    }
    const res = await api.post('/subscriptions', payload)
    const status = res.data?.status
    showPaymentModal.value = false
    if (res.code === 200 && status === 'ACTIVE') {
      subscribed.value = true
      subscriberCount.value++
      showToast('订阅成功！已开通该数据资产的使用权限。')
    } else {
      showToast(res.message || '付款确认失败，请重新尝试支付！', 'error')
    }
  } catch (e) {
    showPaymentModal.value = false
    showToast(e.message || '付款确认失败，请重新尝试支付！', 'error')
  } finally {
    subscribing.value = false
  }
}

onMounted(async () => {
  const id = route.params.id
  try {
    const res = await api.get(`/assets/${id}`)
    asset.value = res.data?.asset
    subscriberCount.value = res.data?.subscriberCount || 0
  } catch (e) {
    console.error(e)
  }
})
</script>
