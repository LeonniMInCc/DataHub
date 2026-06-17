import axios from 'axios'
import { clearAuthSession } from '../utils/authSession'

const api = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL || '/api',
  timeout: 10000,
  headers: { 'Content-Type': 'application/json' }
})

api.interceptors.request.use(config => {
  const token = localStorage.getItem('token')
  if (token) {
    config.headers.Authorization = `Bearer ${token}`
  }
  return config
})

api.interceptors.response.use(
  response => response.data,
  error => {
    const msg = error.response?.data?.message || '请求失败'
    const authExpired = error.response?.status === 401
      || msg.includes('用户不存在')
      || msg.includes('已失效')

    if (authExpired) {
      clearAuthSession()
      window.location.hash = '/login'
    }
    return Promise.reject(new Error(msg))
  }
)

export default api
