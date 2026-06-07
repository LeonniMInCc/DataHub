<template>
  <div class="min-h-screen flex items-center justify-center p-4">
    <div class="glass-card p-8 w-full max-w-md">
      <div class="text-center mb-8">
        <div class="w-14 h-14 rounded-xl bg-gradient-to-br from-primary to-accent flex items-center justify-center mx-auto mb-4">
          <span class="text-white font-bold text-xl">DH</span>
        </div>
        <h1 class="text-2xl font-bold text-white">注册 DataHub</h1>
        <p class="text-slate-400 mt-2 text-sm">加入开发者社区，共享数据资产</p>
      </div>

      <form @submit.prevent="handleRegister" class="space-y-4">
        <div>
          <label class="block text-sm text-slate-300 mb-1.5">用户名</label>
          <input v-model="form.username" class="glass-input" placeholder="2-50个字符" required />
        </div>
        <div>
          <label class="block text-sm text-slate-300 mb-1.5">邮箱</label>
          <input v-model="form.email" class="glass-input" type="email" placeholder="your@email.com" required />
        </div>
        <div>
          <label class="block text-sm text-slate-300 mb-1.5">密码</label>
          <input v-model="form.password" class="glass-input" type="password" placeholder="至少6位" required />
        </div>
        <div>
          <label class="block text-sm text-slate-300 mb-1.5">角色</label>
          <div class="grid grid-cols-2 gap-3">
            <button type="button" @click="form.role = 'PROVIDER'"
              :class="form.role === 'PROVIDER' ? 'border-primary bg-primary/10 text-primary' : 'border-dark-border text-slate-400'"
              class="p-3 rounded-lg border transition-all text-center">
              <div class="text-lg mb-1">📦</div>
              <div class="text-sm font-medium">Provider</div>
              <div class="text-xs opacity-70">发布数据资产</div>
            </button>
            <button type="button" @click="form.role = 'SUBSCRIBER'"
              :class="form.role === 'SUBSCRIBER' ? 'border-accent bg-accent/10 text-accent' : 'border-dark-border text-slate-400'"
              class="p-3 rounded-lg border transition-all text-center">
              <div class="text-lg mb-1">🔍</div>
              <div class="text-sm font-medium">Subscriber</div>
              <div class="text-xs opacity-70">浏览订阅数据</div>
            </button>
          </div>
        </div>
        <p v-if="error" class="text-red-400 text-sm">{{ error }}</p>
        <button type="submit" class="btn-primary w-full" :disabled="loading">
          {{ loading ? '注册中...' : '注册' }}
        </button>
      </form>

      <p class="text-center text-slate-400 text-sm mt-6">
        已有账号？
        <router-link to="/login" class="text-primary hover:underline">立即登录</router-link>
      </p>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '../stores/auth'

const router = useRouter()
const authStore = useAuthStore()
const loading = ref(false)
const error = ref('')
const form = reactive({ username: '', email: '', password: '', role: 'SUBSCRIBER' })

async function handleRegister() {
  loading.value = true
  error.value = ''
  try {
    await authStore.register(form)
    router.push('/')
  } catch (e) {
    error.value = e.message
  } finally {
    loading.value = false
  }
}
</script>
