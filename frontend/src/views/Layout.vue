<template>
  <div class="flex h-screen overflow-hidden">
    <!-- Sidebar -->
    <aside class="w-64 glass-card m-3 mr-0 flex flex-col shrink-0 hidden md:flex">
      <div class="p-5 border-b border-dark-border">
        <router-link to="/" class="flex items-center gap-3">
          <div class="w-9 h-9 rounded-lg bg-gradient-to-br from-primary to-accent flex items-center justify-center text-white font-bold text-sm">DH</div>
          <span class="text-lg font-bold text-white">DataHub</span>
        </router-link>
      </div>

      <nav class="flex-1 p-3 space-y-1 overflow-y-auto">
        <router-link to="/" class="nav-item" active-class="nav-active">
          <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M3 12l2-2m0 0l7-7 7 7M5 10v10a1 1 0 001 1h3m10-11l2 2m-2-2v10a1 1 0 01-1 1h-3m-6 0a1 1 0 001-1v-4a1 1 0 011-1h2a1 1 0 011 1v4a1 1 0 001 1m-6 0h6"/></svg>
          首页
        </router-link>
        <router-link to="/dashboard" class="nav-item" active-class="nav-active">
          <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 19v-6a2 2 0 00-2-2H5a2 2 0 00-2 2v6a2 2 0 002 2h2a2 2 0 002-2zm0 0V9a2 2 0 012-2h2a2 2 0 012 2v10m-6 0a2 2 0 002 2h2a2 2 0 002-2m0 0V5a2 2 0 012-2h2a2 2 0 012 2v14a2 2 0 01-2 2h-2a2 2 0 01-2-2z"/></svg>
          数据仪表盘
        </router-link>
        <router-link to="/assets" class="nav-item" active-class="nav-active">
          <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M20 7l-8-4-8 4m16 0l-8 4m8-4v10l-8 4m0-10L4 7m8 4v10M4 7v10l8 4"/></svg>
          资产市场
        </router-link>

        <div v-if="auth.isProvider" class="pt-3">
          <p class="px-3 text-xs text-slate-500 uppercase tracking-wider font-semibold mb-2">Provider</p>
          <router-link to="/publish" class="nav-item" active-class="nav-active">
            <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 4v16m8-8H4"/></svg>
            发布资产
          </router-link>
        </div>

        <div class="pt-3" v-if="auth.isLoggedIn">
          <p class="px-3 text-xs text-slate-500 uppercase tracking-wider font-semibold mb-2">个人</p>
          <router-link to="/subscriptions" class="nav-item" active-class="nav-active">
            <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 5v2m0 4v2m0 4v2M5 5a2 2 0 00-2 2v3a2 2 0 110 4v3a2 2 0 002 2h14a2 2 0 002-2v-3a2 2 0 110-4V7a2 2 0 00-2-2H5z"/></svg>
            我的订阅
          </router-link>
          <router-link to="/profile" class="nav-item" active-class="nav-active">
            <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z"/></svg>
            个人中心
          </router-link>
        </div>
      </nav>

      <div class="p-3 border-t border-dark-border" v-if="auth.isLoggedIn">
        <router-link to="/profile" class="flex items-center gap-3 p-2 rounded-lg hover:bg-white/5 transition-colors">
          <img :src="auth.user?.avatar" class="w-8 h-8 rounded-full bg-slate-700" alt="" />
          <div class="flex-1 min-w-0">
            <p class="text-sm font-medium text-white truncate">{{ auth.user?.username }}</p>
            <p class="text-xs text-slate-400">{{ auth.user?.role === 'PROVIDER' ? 'Provider' : 'Subscriber' }}</p>
          </div>
        </router-link>
      </div>
      <div class="p-3 border-t border-dark-border" v-else>
        <router-link to="/login" class="btn-primary w-full block text-center text-sm">登录</router-link>
      </div>
    </aside>

    <!-- Mobile nav -->
    <div class="md:hidden fixed bottom-0 left-0 right-0 z-50 glass-card rounded-none border-t border-dark-border">
      <div class="flex justify-around py-2">
        <router-link to="/" class="flex flex-col items-center p-2 text-xs" :class="$route.path==='/'?'text-primary':'text-slate-400'">
          <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M3 12l2-2m0 0l7-7 7 7M5 10v10a1 1 0 001 1h3m10-11l2 2m-2-2v10a1 1 0 01-1 1h-3m-6 0a1 1 0 001-1v-4a1 1 0 011-1h2a1 1 0 011 1v4a1 1 0 001 1m-6 0h6"/></svg>
          首页
        </router-link>
        <router-link to="/dashboard" class="flex flex-col items-center p-2 text-xs" :class="$route.path==='/dashboard'?'text-primary':'text-slate-400'">
          <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 19v-6a2 2 0 00-2-2H5a2 2 0 00-2 2v6a2 2 0 002 2h2a2 2 0 002-2zm0 0V9a2 2 0 012-2h2a2 2 0 012 2v10m-6 0a2 2 0 002 2h2a2 2 0 002-2m0 0V5a2 2 0 012-2h2a2 2 0 012 2v14a2 2 0 01-2 2h-2a2 2 0 01-2-2z"/></svg>
          仪表盘
        </router-link>
        <router-link to="/assets" class="flex flex-col items-center p-2 text-xs" :class="$route.path.startsWith('/assets')?'text-primary':'text-slate-400'">
          <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M20 7l-8-4-8 4m16 0l-8 4m8-4v10l-8 4m0-10L4 7m8 4v10M4 7v10l8 4"/></svg>
          市场
        </router-link>
        <router-link to="/profile" class="flex flex-col items-center p-2 text-xs" :class="$route.path==='/profile'?'text-primary':'text-slate-400'">
          <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z"/></svg>
          我的
        </router-link>
      </div>
    </div>

    <!-- Main Content -->
    <main class="flex-1 overflow-y-auto p-6 md:p-8 pb-20 md:pb-8">
      <router-view />
    </main>
  </div>
</template>

<script setup>
import { useAuthStore } from '../stores/auth'
const auth = useAuthStore()
</script>

<style scoped>
.nav-item {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 10px 12px;
  border-radius: 10px;
  font-size: 14px;
  color: #94A3B8;
  transition: all 0.15s;
  text-decoration: none;
}
.nav-item:hover {
  background: rgba(255,255,255,0.05);
  color: #E2E8F0;
}
.nav-active {
  background: rgba(59,130,246,0.15) !important;
  color: #3B82F6 !important;
  font-weight: 600;
}
</style>
