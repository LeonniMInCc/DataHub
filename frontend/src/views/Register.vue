<template>
  <div class="flex min-h-screen items-center justify-center bg-slate-50 p-3">
    <div class="glass-card w-full max-w-sm p-4">
      <div class="mb-5 text-center">
        <div class="mx-auto mb-3 flex h-10 w-10 items-center justify-center rounded-sm bg-amber-500">
          <span class="text-base font-bold text-white">DH</span>
        </div>
        <h1 class="text-xl font-bold text-slate-900">注册 DataHub</h1>
        <p class="mt-1 text-sm text-slate-500">加入开发者社区，共享数据资产</p>
      </div>

      <form @submit.prevent="handleRegister" class="space-y-3">
        <div>
          <label class="mb-1 block text-sm text-slate-700">用户名</label>
          <input v-model="form.username" class="glass-input" placeholder="2-50个字符" required />
        </div>
        <div>
          <label class="mb-1 block text-sm text-slate-700">邮箱</label>
          <input v-model="form.email" class="glass-input" type="email" placeholder="your@email.com" required />
        </div>
        <div>
          <label class="mb-1 block text-sm text-slate-700">密码</label>
          <input v-model="form.password" class="glass-input" type="password" placeholder="至少6位" required />
        </div>
        <div>
          <label class="mb-1 block text-sm text-slate-700">角色</label>
          <div class="grid grid-cols-2 gap-2">
            <button type="button" @click="form.role = 'PROVIDER'"
              :class="form.role === 'PROVIDER' ? 'border-amber-400 bg-amber-50 text-amber-700' : 'border-slate-200 text-slate-500'"
              class="rounded-sm border p-3 text-center transition-colors">
              <div class="text-sm font-medium">Provider</div>
              <div class="text-xs opacity-70">发布数据资产</div>
            </button>
            <button type="button" @click="form.role = 'SUBSCRIBER'"
              :class="form.role === 'SUBSCRIBER' ? 'border-amber-400 bg-amber-50 text-amber-700' : 'border-slate-200 text-slate-500'"
              class="rounded-sm border p-3 text-center transition-colors">
              <div class="text-sm font-medium">Subscriber</div>
              <div class="text-xs opacity-70">浏览订阅数据</div>
            </button>
          </div>
        </div>
        <p v-if="error" class="text-red-600 text-sm">{{ error }}</p>
        <button type="submit" class="btn-primary w-full" :disabled="loading">
          {{ loading ? '注册中...' : '注册' }}
        </button>
      </form>

      <p class="mt-4 text-center text-sm text-slate-500">
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
