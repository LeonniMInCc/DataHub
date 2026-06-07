import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('../views/Login.vue'),
    meta: { guest: true }
  },
  {
    path: '/register',
    name: 'Register',
    component: () => import('../views/Register.vue'),
    meta: { guest: true }
  },
  {
    path: '/',
    component: () => import('../views/Layout.vue'),
    children: [
      { path: '', name: 'Home', component: () => import('../views/Home.vue') },
      { path: 'assets', name: 'Assets', component: () => import('../views/Assets.vue') },
      { path: 'assets/:id', name: 'AssetDetail', component: () => import('../views/AssetDetail.vue') },
      { path: 'publish', name: 'Publish', component: () => import('../views/Publish.vue'), meta: { provider: true } },
      { path: 'subscriptions', name: 'Subscriptions', component: () => import('../views/Subscriptions.vue'), meta: { auth: true } },
      { path: 'dashboard', name: 'Dashboard', component: () => import('../views/Dashboard.vue') },
      { path: 'profile', name: 'Profile', component: () => import('../views/Profile.vue'), meta: { auth: true } },
    ]
  },
  { path: '/:pathMatch(.*)*', redirect: '/' }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('token')

  if (to.meta.auth && !token) {
    return next('/login')
  }

  if (to.meta.provider) {
    const user = JSON.parse(localStorage.getItem('user') || 'null')
    if (!token) return next('/login')
    if (user?.role !== 'PROVIDER') return next('/')
  }

  if (to.meta.guest && token) {
    return next('/')
  }

  next()
})

export default router
