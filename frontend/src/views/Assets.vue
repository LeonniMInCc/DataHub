<template>
  <div class="max-w-none space-y-3">
    <div class="flex flex-col gap-2 md:flex-row md:items-center md:justify-between">
      <div>
        <h1 class="text-xl font-bold text-slate-900">资产市场</h1>
        <p class="text-sm text-slate-500">按类型、价格和更新时间浏览可订阅资产。</p>
      </div>
      <router-link v-if="auth.isProvider" to="/publish" class="btn-primary inline-flex items-center gap-2">
        <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 4v16m8-8H4"/></svg>
        发布资产
      </router-link>
    </div>

    <div class="border border-slate-200 bg-white md:grid md:grid-cols-[176px_minmax(0,1fr)]">
      <aside class="hidden border-r border-slate-200 p-3 md:block">
        <div class="text-xs font-semibold uppercase tracking-wider text-amber-600">Market</div>
        <div class="mt-2 text-sm font-semibold text-slate-900">资产索引</div>
        <dl class="mt-3 space-y-2 text-xs">
          <div class="flex justify-between">
            <dt class="text-slate-500">当前页</dt>
            <dd class="font-medium text-slate-900">{{ page + 1 }}</dd>
          </div>
          <div class="flex justify-between">
            <dt class="text-slate-500">总页数</dt>
            <dd class="font-medium text-slate-900">{{ totalPages }}</dd>
          </div>
          <div class="flex justify-between">
            <dt class="text-slate-500">当前结果</dt>
            <dd class="font-medium text-slate-900">{{ assets.length }}</dd>
          </div>
        </dl>
      </aside>

      <section class="min-w-0">
        <!-- Search & Filter -->
        <div class="border-b border-slate-200 p-3">
          <div class="flex flex-col gap-2 md:flex-row md:items-center">
            <div class="relative flex-1">
              <svg class="pointer-events-none absolute left-3 top-1/2 h-4 w-4 -translate-y-1/2 text-slate-400" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z"/></svg>
              <input v-model="keyword" @input="search" class="glass-input h-9 pl-9" placeholder="搜索资产名称、描述、标签..." />
            </div>
            <select v-model="sortBy" class="glass-input h-9 w-full md:w-40">
              <option value="createdDesc">最新发布</option>
              <option value="priceAsc">价格低到高</option>
              <option value="priceDesc">价格高到低</option>
            </select>
          </div>
        </div>

        <div class="border-b border-slate-200 px-3">
          <nav class="relative flex gap-4 overflow-x-auto">
            <button
              v-for="(tab, index) in typeTabs"
              :key="tab.value || 'ALL'"
              :ref="el => setTypeTabRef(el, index)"
              type="button"
              @click="selectAssetType(tab.value)"
              :class="assetType === tab.value ? 'text-amber-600' : 'text-slate-500 hover:text-slate-700'"
              class="whitespace-nowrap px-1 py-2 text-sm font-medium transition-colors duration-300"
            >
              {{ tab.label }}
            </button>
            <span class="pointer-events-none absolute bottom-0 h-0.5 bg-amber-500 transition-all duration-300 ease-out" :style="typeLineStyle"></span>
          </nav>
        </div>

        <div v-if="loading" class="space-y-2 p-3">
          <div v-for="i in 5" :key="i" class="animate-pulse border border-slate-200 bg-white p-3">
            <div class="flex flex-col gap-3 lg:flex-row lg:items-center lg:justify-between">
              <div class="min-w-0 flex-1">
                <div class="flex items-center gap-2">
                  <div class="h-7 w-7 bg-slate-100"></div>
                  <div class="h-5 w-14 bg-amber-50"></div>
                  <div class="h-4 w-1/3 bg-slate-100"></div>
                </div>
                <div class="mt-2 h-4 w-2/3 bg-slate-100"></div>
                <div class="mt-3 flex gap-2">
                  <div class="h-3 w-20 bg-slate-100"></div>
                  <div class="h-3 w-16 bg-slate-100"></div>
                  <div class="h-3 w-24 bg-slate-100"></div>
                </div>
              </div>
              <div class="h-9 w-24 bg-slate-100"></div>
            </div>
          </div>
        </div>

        <div v-else-if="assets.length === 0" class="p-4 text-center">
          <p class="text-sm font-medium text-slate-700">暂无资产</p>
          <p class="mt-1 text-xs text-slate-500">调整搜索条件后重试。</p>
        </div>

        <div v-else class="animate-fade-in-up space-y-2 p-3">
          <div
            v-for="asset in visibleAssets"
            :key="asset.assetId"
            class="group cursor-pointer border border-slate-200 bg-white p-3 transition-all duration-300 hover:-translate-y-0.5 hover:border-amber-300 hover:bg-amber-50/40"
            @click="$router.push(`/assets/${asset.assetId}`)"
          >
            <div class="flex flex-col gap-3 lg:flex-row lg:items-center lg:justify-between">
              <div class="min-w-0 flex-1">
                <div class="flex flex-wrap items-center gap-2">
                  <span
                    class="flex h-7 w-7 shrink-0 items-center justify-center border border-slate-200 bg-slate-50 text-slate-400 transition-all duration-300 group-hover:border-amber-200 group-hover:bg-amber-50 group-hover:text-amber-500"
                    :class="asset.assetType === 'API' ? 'group-hover:scale-110' : 'group-hover:-translate-y-0.5'"
                  >
                    <svg v-if="asset.assetType === 'API'" class="h-4 w-4 transition-transform duration-300" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                      <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M13 10V3L4 14h7v7l9-11h-7z"/>
                    </svg>
                    <svg v-else class="h-4 w-4 transition-transform duration-300" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                      <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 7c0 1.657 3.582 3 8 3s8-1.343 8-3-3.582-3-8-3-8 1.343-8 3z"/>
                      <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 7v5c0 1.657 3.582 3 8 3s8-1.343 8-3V7"/>
                      <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 12v5c0 1.657 3.582 3 8 3s8-1.343 8-3v-5"/>
                    </svg>
                  </span>
                  <span class="rounded-sm border border-amber-200 bg-amber-50 px-2 py-0.5 text-xs font-semibold text-amber-700">
                    {{ asset.assetType === 'API' ? 'API' : '数据集' }}
                  </span>
                  <h3 class="truncate text-sm font-semibold text-slate-900 transition-colors duration-300 group-hover:text-amber-600">{{ asset.title }}</h3>
                  <span v-if="isHotAsset(asset)" class="relative flex h-2 w-2" title="热门资产">
                    <span class="absolute inline-flex h-full w-full animate-ping rounded-full bg-amber-400 opacity-75"></span>
                    <span class="relative inline-flex h-2 w-2 rounded-full bg-amber-500"></span>
                  </span>
                </div>
                <p class="mt-1 line-clamp-1 text-sm text-slate-500">{{ asset.description || '暂无描述' }}</p>

                <div class="mt-2 flex flex-wrap items-center divide-x divide-slate-200 text-xs text-slate-500">
                  <span class="inline-flex items-center gap-1 pr-2">
                    <svg class="h-3 w-3" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M5.121 17.804A9 9 0 1118.88 6.196 9 9 0 015.12 17.804z"/><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 11a3 3 0 11-6 0 3 3 0 016 0z"/></svg>
                    {{ asset.provider?.username || '未知' }}
                  </span>
                  <span class="inline-flex items-center gap-1 px-2">
                    <svg class="h-3 w-3" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 8c-1.657 0-3 .895-3 2s1.343 2 3 2 3 .895 3 2-1.343 2-3 2m0-8V6m0 10v2"/></svg>
                    {{ asset.pricePerMonth > 0 ? '¥' + asset.pricePerMonth + '/月' : '免费' }}
                  </span>
                  <span class="inline-flex items-center gap-1 px-2">
                    <svg class="h-3 w-3" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M8 7V3m8 4V3M5 11h14M5 19h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v10a2 2 0 002 2z"/></svg>
                    {{ formatDate(asset.createdAt) }}
                  </span>
                  <span class="inline-flex items-center gap-1 pl-2">
                    <svg class="h-3 w-3" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 5v2m0 4v2m0 4v2M5 5h14a2 2 0 012 2v3a2 2 0 010 4v3a2 2 0 01-2 2H5a2 2 0 01-2-2v-3a2 2 0 010-4V7a2 2 0 012-2z"/></svg>
                    {{ subscriberCount(asset) }} 订阅
                  </span>
                </div>

                <div v-if="asset.tags" class="mt-2 flex flex-wrap gap-1">
                  <span v-for="tag in asset.tags.split(',')" :key="tag" class="rounded-sm border border-slate-200 bg-slate-50 px-2 py-0.5 text-xs text-slate-500">{{ tag.trim() }}</span>
                </div>
              </div>

              <div class="flex shrink-0 items-center gap-2 lg:justify-end">
                <button v-if="auth.isLoggedIn && !isOwner(asset)" @click.stop="openPaymentModal(asset)" :disabled="subscribing" class="btn-primary text-sm">
                  {{ subscribing && selectedAsset?.assetId === asset.assetId ? '处理中...' : '订阅' }}
                </button>
                <button v-else-if="!auth.isLoggedIn" @click.stop="router.push('/login')" class="btn-primary text-sm">
                  登录后订阅
                </button>
              </div>
            </div>
          </div>
        </div>
      </section>
    </div>

    <!-- Pagination -->
    <div v-if="totalPages > 1" class="flex justify-center gap-2">
      <button @click="page--; loadAssets()" :disabled="page <= 0" class="btn-outline disabled:opacity-40">上一页</button>
      <span class="flex items-center px-3 text-sm text-slate-500">第 {{ page + 1 }} / {{ totalPages }} 页</span>
      <button @click="page++; loadAssets()" :disabled="page >= totalPages - 1" class="btn-outline disabled:opacity-40">下一页</button>
    </div>

    <div v-if="showPaymentModal" class="fixed inset-0 z-50 flex items-center justify-center bg-black/40 px-4">
      <div class="w-full max-w-md rounded-sm border border-slate-200 bg-white">
        <div class="flex items-center justify-between border-b border-slate-200 px-4 py-3">
          <h3 class="text-base font-semibold text-slate-900">订阅支付验证</h3>
          <button @click="closePaymentModal" :disabled="subscribing" class="text-2xl leading-none text-slate-400 hover:text-slate-600">&times;</button>
        </div>
        <div class="px-4 py-4 text-center">
          <div class="mb-3 text-sm font-medium text-slate-700">{{ selectedAsset?.title }}</div>
          <img src="/付款码.jpg" alt="付款码" class="mx-auto h-64 w-64 rounded-sm border border-slate-200 object-contain" />
          <p class="mt-4 text-sm leading-6 text-slate-600">
            请使用手机扫码支付。支付完成后，请点击下方按钮确认付款状态。
          </p>
          <div class="mt-4 flex justify-center gap-2 text-xs">
            <button @click="confirmPayment(true)" :disabled="subscribing" class="rounded-sm border border-green-200 px-3 py-1.5 text-green-700 hover:bg-green-50 disabled:opacity-50">
              模拟支付成功
            </button>
            <button @click="confirmPayment(false)" :disabled="subscribing" class="rounded-sm border border-red-200 px-3 py-1.5 text-red-700 hover:bg-red-50 disabled:opacity-50">
              模拟支付失败
            </button>
          </div>
        </div>
        <div class="flex justify-end gap-2 border-t border-slate-200 px-4 py-3">
          <button @click="closePaymentModal" :disabled="subscribing" class="btn-outline">取消</button>
          <button @click="confirmPayment()" :disabled="subscribing" class="btn-primary">
            {{ subscribing ? '确认中...' : '已完成支付（确认状态）' }}
          </button>
        </div>
      </div>
    </div>

    <div v-if="toast.message" :class="toast.type === 'success' ? 'bg-green-600' : 'bg-red-600'" class="fixed right-4 top-4 z-50 rounded-sm px-3 py-2 text-sm text-white">
      {{ toast.message }}
    </div>
  </div>
</template>

<script setup>
import { computed, nextTick, ref, onMounted, onBeforeUpdate } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '../stores/auth'
import api from '../api'

const auth = useAuthStore()
const router = useRouter()
const assets = ref([])
const loading = ref(true)
const page = ref(0)
const totalPages = ref(1)
const keyword = ref('')
const assetType = ref('')
const sortBy = ref('createdDesc')
const selectedAsset = ref(null)
const showPaymentModal = ref(false)
const subscribing = ref(false)
const toast = ref({ message: '', type: 'success' })
let searchTimer = null
let toastTimer = null
const typeTabRefs = ref([])
const typeLineStyle = ref({ width: '0px', transform: 'translateX(0px)' })

const typeTabs = [
  { label: '全部', value: '' },
  { label: 'API', value: 'API' },
  { label: '数据集', value: 'DATASET' }
]

const visibleAssets = computed(() => {
  const list = [...assets.value]
  if (sortBy.value === 'priceAsc') {
    return list.sort((a, b) => Number(a.pricePerMonth || 0) - Number(b.pricePerMonth || 0))
  }
  if (sortBy.value === 'priceDesc') {
    return list.sort((a, b) => Number(b.pricePerMonth || 0) - Number(a.pricePerMonth || 0))
  }
  return list.sort((a, b) => new Date(b.createdAt || 0) - new Date(a.createdAt || 0))
})

const hotAssetIds = computed(() => {
  return [...assets.value]
    .filter(asset => subscriberCount(asset) > 0)
    .sort((a, b) => subscriberCount(b) - subscriberCount(a))
    .slice(0, 3)
    .map(asset => asset.assetId)
})

const activeTypeIndex = computed(() => typeTabs.findIndex(tab => tab.value === assetType.value))

function formatDate(d) {
  if (!d) return ''
  return new Date(d).toLocaleDateString('zh-CN')
}

function isOwner(asset) {
  return auth.isLoggedIn && auth.user?.devId === asset.providerId
}

function subscriberCount(asset) {
  return asset.subscriberCount ?? asset.subscriptionsCount ?? asset.subscriptionCount ?? 0
}

function isHotAsset(asset) {
  return hotAssetIds.value.includes(asset.assetId)
}

function setTypeTabRef(el, index) {
  if (el) typeTabRefs.value[index] = el
}

function updateTypeLine() {
  const activeEl = typeTabRefs.value[activeTypeIndex.value]
  if (!activeEl) return
  typeLineStyle.value = {
    width: `${activeEl.offsetWidth}px`,
    transform: `translateX(${activeEl.offsetLeft}px)`
  }
}

async function selectAssetType(value) {
  if (assetType.value === value) return
  assetType.value = value
  await nextTick()
  updateTypeLine()
  search()
}

function showToast(message, type = 'success') {
  clearTimeout(toastTimer)
  toast.value = { message, type }
  toastTimer = setTimeout(() => {
    toast.value.message = ''
  }, 3000)
}

function openPaymentModal(asset) {
  selectedAsset.value = asset
  showPaymentModal.value = true
}

function closePaymentModal() {
  if (subscribing.value) return
  showPaymentModal.value = false
  selectedAsset.value = null
}

async function confirmPayment(mockPaymentSuccess = null) {
  if (!selectedAsset.value) return
  subscribing.value = true
  try {
    const payload = { assetId: selectedAsset.value.assetId }
    if (mockPaymentSuccess !== null) {
      payload.mockPaymentSuccess = mockPaymentSuccess
    }
    const res = await api.post('/subscriptions', payload)
    const status = res.data?.status
    showPaymentModal.value = false
    if (res.code === 200 && status === 'ACTIVE') {
      showToast('订阅成功！已开通该数据资产的使用权限。')
      await loadAssets()
    } else {
      showToast(res.message || '付款确认失败，请重新尝试支付！', 'error')
    }
  } catch (e) {
    showPaymentModal.value = false
    showToast(e.message || '付款确认失败，请重新尝试支付！', 'error')
  } finally {
    subscribing.value = false
    selectedAsset.value = null
  }
}

async function loadAssets() {
  loading.value = true
  try {
    const params = { page: page.value, size: 12 }
    if (keyword.value) params.keyword = keyword.value
    if (assetType.value) params.assetType = assetType.value

    const res = await api.get('/assets', { params })
    assets.value = res.data?.content || []
    totalPages.value = res.data?.totalPages || 1
  } catch (e) {
    console.error(e)
  } finally {
    loading.value = false
  }
}

function search() {
  clearTimeout(searchTimer)
  page.value = 0
  searchTimer = setTimeout(loadAssets, 300)
}

onBeforeUpdate(() => {
  typeTabRefs.value = []
})

onMounted(async () => {
  await nextTick()
  updateTypeLine()
  await loadAssets()
  await nextTick()
  updateTypeLine()
})
</script>

<style scoped>
.search-input {
  padding-left: 2.75rem;
}
</style>
