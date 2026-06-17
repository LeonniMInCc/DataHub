<template>
  <div class="mx-auto max-w-4xl space-y-3">
    <div>
      <h1 class="text-xl font-bold text-slate-900">个人中心</h1>
      <p class="text-sm text-slate-500">账户身份、API Key 和资产状态。</p>
    </div>

    <div v-if="loading" class="py-12 text-center text-sm text-slate-500">加载中...</div>

    <div v-else class="grid gap-3 md:grid-cols-3">
      <!-- Profile Card -->
      <div class="glass-card p-4 text-center">
        <img :src="profile.avatar" class="mx-auto mb-3 h-16 w-16 rounded-sm bg-slate-100" alt="" />
        <h2 class="text-lg font-bold text-slate-900">{{ profile.username }}</h2>
        <p class="mt-1 text-sm text-slate-500">{{ profile.email }}</p>
        <span class="mt-3 inline-block rounded-sm border border-amber-200 bg-amber-50 px-2 py-0.5 text-xs font-semibold text-amber-700">
          {{ profile.role === 'PROVIDER' ? '数据提供者' : '数据订阅者' }}
        </span>
        <div class="mt-4 rounded-sm border border-amber-200 bg-amber-50 p-3 text-left">
          <div class="mb-1 text-xs text-slate-500">API Key</div>
          <div class="truncate font-mono text-sm text-slate-900">{{ profile.apiKey || 'N/A' }}</div>
        </div>
        <div class="mt-4 text-xs text-slate-400">
          注册于 {{ formatDate(profile.createdAt) }}
        </div>
      </div>

      <!-- Stats -->
      <div class="space-y-3 md:col-span-2">
        <div class="grid grid-cols-2 gap-3">
          <div class="glass-card p-3">
            <div class="mb-1 text-sm text-slate-500">{{ profile.role === 'PROVIDER' ? '发布资产' : '订阅资产' }}</div>
            <div class="text-2xl font-bold text-slate-900">{{ profile.role === 'PROVIDER' ? profile.publishedAssets : profile.subscribedAssets }}</div>
          </div>
          <div class="glass-card p-3">
            <div class="mb-1 text-sm text-slate-500">账户状态</div>
            <div class="flex items-center gap-2">
              <div class="h-2.5 w-2.5 rounded-sm bg-amber-500"></div>
              <span class="font-semibold text-slate-900">活跃</span>
            </div>
          </div>
        </div>

        <div class="glass-card p-4">
          <h3 class="mb-3 font-semibold text-slate-900">账户信息</h3>
          <dl class="divide-y divide-slate-200">
            <div class="flex justify-between py-2 text-sm">
              <dt class="text-slate-500">用户 ID</dt>
              <dd class="font-mono text-slate-900">{{ profile.devId }}</dd>
            </div>
            <div class="flex justify-between py-2 text-sm">
              <dt class="text-slate-500">用户名</dt>
              <dd class="text-slate-900">{{ profile.username }}</dd>
            </div>
            <div class="flex justify-between py-2 text-sm">
              <dt class="text-slate-500">邮箱</dt>
              <dd class="text-slate-900">{{ profile.email }}</dd>
            </div>
            <div class="flex justify-between py-2 text-sm">
              <dt class="text-slate-500">角色</dt>
              <dd class="text-slate-900">{{ profile.role }}</dd>
            </div>
            <div class="flex justify-between py-2 text-sm">
              <dt class="text-slate-500">注册时间</dt>
              <dd class="text-slate-900">{{ formatDate(profile.createdAt) }}</dd>
            </div>
          </dl>
        </div>

        <div class="flex gap-2">
          <router-link to="/dashboard" class="btn-outline flex-1 text-center">查看仪表盘</router-link>
          <button @click="handleLogout" class="btn-outline text-red-600 hover:border-red-500 flex-1">退出登录</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '../stores/auth'
import api from '../api'

const router = useRouter()
const auth = useAuthStore()
const profile = ref({})
const loading = ref(true)

function formatDate(d) {
  if (!d) return ''
  return new Date(d).toLocaleDateString('zh-CN')
}

function handleLogout() {
  auth.logout()
  router.push('/login')
}

onMounted(async () => {
  try {
    const res = await api.get('/user/profile')
    profile.value = res.data || {}
  } catch (e) {
    console.error(e)
  } finally {
    loading.value = false
  }
})
</script>
