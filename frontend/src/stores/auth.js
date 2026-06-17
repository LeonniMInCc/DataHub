import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import api from '../api'
import { AUTH_EXPIRED_EVENT, clearAuthSession } from '../utils/authSession'

export const useAuthStore = defineStore('auth', () => {
  const user = ref(JSON.parse(localStorage.getItem('user') || 'null'))
  const token = ref(localStorage.getItem('token') || '')

  const isLoggedIn = computed(() => !!token.value)
  const isProvider = computed(() => user.value?.role === 'PROVIDER')

  async function register(data) {
    const res = await api.post('/auth/register', data)
    return handleAuthResponse(res)
  }

  async function login(data) {
    const res = await api.post('/auth/login', data)
    return handleAuthResponse(res)
  }

  function handleAuthResponse(res) {
    const d = res.data
    user.value = d
    token.value = d.token
    localStorage.setItem('token', d.token)
    localStorage.setItem('user', JSON.stringify(d))
    return d
  }

  function logout() {
    user.value = null
    token.value = ''
    clearAuthSession()
  }

  if (typeof window !== 'undefined') {
    window.addEventListener(AUTH_EXPIRED_EVENT, () => {
      user.value = null
      token.value = ''
    })
  }

  return { user, token, isLoggedIn, isProvider, register, login, logout }
})
