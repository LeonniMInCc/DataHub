<template>
  <div class="max-w-4xl mx-auto">
    <h1 class="text-2xl font-bold text-white mb-6">个人中心</h1>

    <div v-if="loading" class="text-center py-20 text-slate-400">加载中...</div>

    <div v-else class="grid md:grid-cols-3 gap-6">
      <!-- Profile Card -->
      <div class="glass-card p-6 text-center">
        <img :src="profile.avatar" class="w-20 h-20 rounded-full mx-auto mb-4 bg-slate-700" alt="" />
        <h2 class="text-xl font-bold text-white">{{ profile.username }}</h2>
        <p class="text-slate-400 text-sm mt-1">{{ profile.email }}</p>
        <span :class="profile.role === 'PROVIDER' ? 'bg-primary/20 text-primary' : 'bg-accent/20 text-accent'" class="inline-block px-3 py-1 rounded-full text-xs font-semibold mt-3">
          {{ profile.role === 'PROVIDER' ? '数据提供者' : '数据订阅者' }}
        </span>
        <div class="mt-4 p-3 bg-white/5 rounded-lg text-left">
          <div class="text-xs text-slate-500 mb-1">API Key</div>
          <div class="text-sm text-white font-mono truncate">{{ profile.apiKey || 'N/A' }}</div>
        </div>
        <div class="text-xs text-slate-500 mt-4">
          注册于 {{ formatDate(profile.createdAt) }}
        </div>
      </div>

      <!-- Stats -->
      <div class="md:col-span-2 space-y-4">
        <div class="grid grid-cols-2 gap-4">
          <div class="glass-card p-5">
            <div class="text-slate-400 text-sm mb-1">{{ profile.role === 'PROVIDER' ? '发布资产' : '订阅资产' }}</div>
            <div class="text-3xl font-bold text-white">{{ profile.role === 'PROVIDER' ? profile.publishedAssets : profile.subscribedAssets }}</div>
          </div>
          <div class="glass-card p-5">
            <div class="text-slate-400 text-sm mb-1">账户状态</div>
            <div class="flex items-center gap-2">
              <div class="w-2.5 h-2.5 rounded-full bg-accent"></div>
              <span class="text-white font-semibold">活跃</span>
            </div>
          </div>
        </div>

        <div class="glass-card p-6">
          <h3 class="text-white font-semibold mb-4">账户信息</h3>
          <dl class="space-y-4">
            <div class="flex justify-between py-2 border-b border-dark-border">
              <dt class="text-slate-400">用户 ID</dt>
              <dd class="text-white font-mono text-sm">{{ profile.devId }}</dd>
            </div>
            <div class="flex justify-between py-2 border-b border-dark-border">
              <dt class="text-slate-400">用户名</dt>
              <dd class="text-white">{{ profile.username }}</dd>
            </div>
            <div class="flex justify-between py-2 border-b border-dark-border">
              <dt class="text-slate-400">邮箱</dt>
              <dd class="text-white">{{ profile.email }}</dd>
            </div>
            <div class="flex justify-between py-2 border-b border-dark-border">
              <dt class="text-slate-400">角色</dt>
              <dd class="text-white">{{ profile.role }}</dd>
            </div>
            <div class="flex justify-between py-2">
              <dt class="text-slate-400">注册时间</dt>
              <dd class="text-white">{{ formatDate(profile.createdAt) }}</dd>
            </div>
          </dl>
        </div>

        <div class="flex gap-3">
          <router-link to="/dashboard" class="btn-outline flex-1 text-center">查看仪表盘</router-link>
          <button @click="handleLogout" class="btn-outline text-red-400 hover:border-red-400 flex-1">退出登录</button>
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
