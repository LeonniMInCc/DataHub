<template>
  <div class="flex min-h-screen items-center justify-center bg-slate-50 p-3">
    <div class="glass-card w-full max-w-sm p-4">
      <div class="mb-5 text-center">
        <div class="mx-auto mb-3 flex h-10 w-10 items-center justify-center rounded-sm bg-amber-500">
          <span class="text-base font-bold text-white">DH</span>
        </div>
        <h1 class="text-xl font-bold text-slate-900">登录 DataHub</h1>
        <p class="mt-1 text-sm text-slate-500">开发者数字资产共享平台</p>
      </div>

      <form @submit.prevent="handleLogin" class="space-y-3">
        <div>
          <label class="mb-1 block text-sm text-slate-700">用户名</label>
          <input v-model="form.username" class="glass-input" placeholder="请输入用户名" required />
        </div>
        <div>
          <label class="mb-1 block text-sm text-slate-700">密码</label>
          <input v-model="form.password" class="glass-input" type="password" placeholder="请输入密码" required />
        </div>
        <p v-if="error" class="text-red-600 text-sm">{{ error }}</p>
        <button type="submit" class="btn-primary w-full" :disabled="loading">
          {{ loading ? '登录中...' : '登录' }}
        </button>
      </form>

      <p class="mt-4 text-center text-sm text-slate-500">
        还没有账号？
        <router-link to="/register" class="text-primary hover:underline">立即注册</router-link>
      </p>

      <div class="mt-4 rounded-sm border border-amber-200 bg-amber-50 p-3 text-xs text-slate-500">
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
