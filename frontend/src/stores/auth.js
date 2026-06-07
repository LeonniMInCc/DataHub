import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import api from '../api'

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
    localStorage.removeItem('token')
    localStorage.removeItem('user')
  }

  return { user, token, isLoggedIn, isProvider, register, login, logout }
})
