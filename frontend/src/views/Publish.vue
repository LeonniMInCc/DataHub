<template>
  <div class="mx-auto max-w-2xl space-y-3">
    <div>
      <h1 class="text-xl font-bold text-slate-900">发布数据资产</h1>
      <p class="text-sm text-slate-500">提交资产类型、端点、价格和标签。</p>
    </div>

    <div class="glass-card p-4">
      <form @submit.prevent="handleSubmit" class="space-y-3">
        <div>
          <label class="mb-1 block text-sm text-slate-700">资产类型 *</label>
          <div class="grid grid-cols-2 gap-2">
            <button type="button" @click="form.assetType = 'DATASET'"
              :class="form.assetType === 'DATASET' ? 'border-amber-400 bg-amber-50 text-amber-700' : 'border-slate-200 text-slate-500'"
              class="rounded-sm border p-3 text-center transition-colors">
              数据集
            </button>
            <button type="button" @click="form.assetType = 'API'"
              :class="form.assetType === 'API' ? 'border-amber-400 bg-amber-50 text-amber-700' : 'border-slate-200 text-slate-500'"
              class="rounded-sm border p-3 text-center transition-colors">
              API
            </button>
          </div>
        </div>

        <div>
          <label class="mb-1 block text-sm text-slate-700">标题 *</label>
          <input v-model="form.title" class="glass-input" placeholder="给你的资产起个名字" required maxlength="200" />
        </div>

        <div>
          <label class="mb-1 block text-sm text-slate-700">描述</label>
          <textarea v-model="form.description" class="glass-input" rows="4" placeholder="资产描述、用途、数据格式等"></textarea>
        </div>

        <div>
          <label class="mb-1 block text-sm text-slate-700">端点 URL</label>
          <input v-model="form.endpointUrl" class="glass-input" placeholder="https://api.example.com/v1/data" />
        </div>

        <div>
          <label class="mb-1 block text-sm text-slate-700">价格 (元/月) *</label>
          <input v-model.number="form.pricePerMonth" class="glass-input" type="number" min="0" step="0.01" placeholder="0.00 表示免费" required />
        </div>

        <div>
          <label class="mb-1 block text-sm text-slate-700">标签</label>
          <input v-model="form.tags" class="glass-input" placeholder="多个标签用逗号分隔，如：天气,中国,API" />
        </div>

        <p v-if="error" class="text-red-600 text-sm">{{ error }}</p>

        <button type="submit" class="btn-primary w-full" :disabled="loading">
          {{ loading ? '发布中...' : '发布资产' }}
        </button>
      </form>
    </div>
  </div>
</template>

<script setup>
import { reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import api from '../api'

const router = useRouter()
const loading = ref(false)
const error = ref('')

const form = reactive({
  assetType: 'DATASET',
  title: '',
  description: '',
  endpointUrl: '',
  pricePerMonth: 0,
  tags: ''
})

async function handleSubmit() {
  loading.value = true
  error.value = ''
  try {
    await api.post('/assets', form)
    router.push('/assets')
  } catch (e) {
    error.value = e.message
  } finally {
    loading.value = false
  }
}
</script>
