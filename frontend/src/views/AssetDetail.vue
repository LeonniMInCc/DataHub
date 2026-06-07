<template>
  <div class="max-w-4xl mx-auto" v-if="asset">
    <!-- Header -->
    <div class="glass-card p-6 mb-6">
      <div class="flex items-start justify-between flex-wrap gap-4">
        <div class="flex-1">
          <div class="flex items-center gap-3 mb-3">
            <span :class="asset.assetType === 'API' ? 'bg-primary/20 text-primary' : 'bg-accent/20 text-accent'" class="px-3 py-1 rounded-md text-sm font-semibold">
              {{ asset.assetType }}
            </span>
            <span :class="asset.status === 'ACTIVE' ? 'bg-green-500/20 text-green-400' : 'bg-slate-500/20 text-slate-400'" class="px-3 py-1 rounded-md text-sm">
              {{ asset.status === 'ACTIVE' ? '活跃' : '已下线' }}
            </span>
          </div>
          <h1 class="text-2xl font-bold text-white mb-2">{{ asset.title }}</h1>
          <p class="text-slate-400">{{ asset.description }}</p>
        </div>
        <div class="text-right">
          <div class="text-3xl font-bold text-primary mb-1">
            {{ asset.pricePerMonth > 0 ? '¥' + asset.pricePerMonth : '免费' }}
          </div>
          <div class="text-sm text-slate-500">每月</div>
          <button v-if="auth.isLoggedIn && !isOwner" @click="handleSubscribe" :disabled="subscribing" class="btn-primary mt-3 w-full">
            {{ subscribing ? '处理中...' : subscribed ? '已订阅' : '立即订阅' }}
          </button>
          <button v-if="!auth.isLoggedIn" @click="$router.push('/login')" class="btn-primary mt-3 w-full">登录后订阅</button>
        </div>
      </div>
    </div>

    <div class="grid md:grid-cols-3 gap-6">
      <!-- Details -->
      <div class="md:col-span-2 space-y-6">
        <div class="glass-card p-5">
          <h3 class="text-white font-semibold mb-3">资产详情</h3>
          <dl class="space-y-3">
            <div class="flex justify-between text-sm">
              <dt class="text-slate-400">提供者</dt>
              <dd class="text-white">{{ asset.provider?.username || '未知' }}</dd>
            </div>
            <div class="flex justify-between text-sm">
              <dt class="text-slate-400">订阅人数</dt>
              <dd class="text-white">{{ subscriberCount }} 人</dd>
            </div>
            <div class="flex justify-between text-sm">
              <dt class="text-slate-400">创建时间</dt>
              <dd class="text-white">{{ formatDate(asset.createdAt) }}</dd>
            </div>
            <div class="flex justify-between text-sm" v-if="asset.endpointUrl">
              <dt class="text-slate-400">API 端点</dt>
              <dd class="text-primary font-mono text-xs break-all">{{ asset.endpointUrl }}</dd>
            </div>
          </dl>
        </div>

        <div class="glass-card p-5" v-if="asset.assetType === 'API'">
          <h3 class="text-white font-semibold mb-3">调用示例</h3>
          <div class="bg-slate-900 rounded-lg p-4">
            <pre class="text-green-400 text-sm overflow-x-auto"><code>curl -X GET "{{ asset.endpointUrl || 'https://api.datahub.io/v1/example' }}" \
  -H "Authorization: Bearer YOUR_API_KEY" \
  -H "Content-Type: application/json"</code></pre>
          </div>
        </div>

        <div class="glass-card p-5" v-if="asset.tags">
          <h3 class="text-white font-semibold mb-3">标签</h3>
          <div class="flex flex-wrap gap-2">
            <span v-for="tag in asset.tags.split(',')" :key="tag" class="px-3 py-1 bg-white/5 rounded-full text-sm text-slate-300">{{ tag.trim() }}</span>
          </div>
        </div>
      </div>

      <!-- Sidebar -->
      <div class="space-y-4">
        <div class="glass-card p-5 text-center">
          <img :src="asset.provider?.avatar || '/vite.svg'" class="w-16 h-16 rounded-full mx-auto mb-3 bg-slate-700" alt="" />
          <h4 class="text-white font-medium">{{ asset.provider?.username || '未知提供者' }}</h4>
          <p class="text-slate-500 text-sm mt-1">{{ asset.provider?.role === 'PROVIDER' ? '数据提供者' : '用户' }}</p>
        </div>

        <div class="glass-card p-5">
          <h4 class="text-white font-medium mb-3 text-sm">资产统计</h4>
          <div class="space-y-2 text-sm">
            <div class="flex justify-between"><span class="text-slate-400">类型</span><span class="text-white">{{ asset.assetType }}</span></div>
            <div class="flex justify-between"><span class="text-slate-400">状态</span><span class="text-green-400">ACTIVE</span></div>
            <div class="flex justify-between"><span class="text-slate-400">订阅数</span><span class="text-white">{{ subscriberCount }}</span></div>
            <div class="flex justify-between"><span class="text-slate-400">定价</span><span class="text-primary font-semibold">¥{{ asset.pricePerMonth }}/月</span></div>
          </div>
        </div>
      </div>
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

const isOwner = computed(() => {
  if (!auth.isLoggedIn || !asset.value) return false
  return auth.user?.devId === asset.value.providerId
})

function formatDate(d) {
  if (!d) return ''
  return new Date(d).toLocaleDateString('zh-CN', { year: 'numeric', month: 'long', day: 'numeric' })
}

async function handleSubscribe() {
  subscribing.value = true
  try {
    await api.post('/subscriptions', { assetId: asset.value.assetId })
    subscribed.value = true
    subscriberCount.value++
  } catch (e) {
    alert(e.message)
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
