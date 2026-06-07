<template>
  <div class="min-h-screen flex items-center justify-center p-4">
    <div class="glass-card p-8 w-full max-w-md">
      <div class="text-center mb-8">
        <div class="w-14 h-14 rounded-xl bg-gradient-to-br from-primary to-accent flex items-center justify-center mx-auto mb-4">
          <span class="text-white font-bold text-xl">DH</span>
        </div>
        <h1 class="text-2xl font-bold text-white">登录 DataHub</h1>
        <p class="text-slate-400 mt-2 text-sm">开发者数字资产共享平台</p>
      </div>

      <form @submit.prevent="handleLogin" class="space-y-4">
        <div>
          <label class="block text-sm text-slate-300 mb-1.5">用户名</label>
          <input v-model="form.username" class="glass-input" placeholder="请输入用户名" required />
        </div>
        <div>
          <label class="block text-sm text-slate-300 mb-1.5">密码</label>
          <input v-model="form.password" class="glass-input" type="password" placeholder="请输入密码" required />
        </div>
        <p v-if="error" class="text-red-400 text-sm">{{ error }}</p>
        <button type="submit" class="btn-primary w-full" :disabled="loading">
          {{ loading ? '登录中...' : '登录' }}
        </button>
      </form>

      <p class="text-center text-slate-400 text-sm mt-6">
        还没有账号？
        <router-link to="/register" class="text-primary hover:underline">立即注册</router-link>
      </p>

      <div class="mt-4 p-3 rounded-lg bg-white/5 text-xs text-slate-500">
        <p>测试账号：</p>
        <p>Provider: zhangsan / password123</p>
        <p>Subscriber: sub_a / password123</p>
      </div>
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
const form = reactive({ username: '', password: '' })

async function handleLogin() {
  loading.value = true
  error.value = ''
  try {
    await authStore.login(form)
    router.push('/')
  } catch (e) {
    error.value = e.message
  } finally {
    loading.value = false
  }
}
</script>
