<template>
  <div class="max-w-2xl mx-auto">
    <h1 class="text-2xl font-bold text-white mb-6">发布数据资产</h1>

    <div class="glass-card p-6">
      <form @submit.prevent="handleSubmit" class="space-y-4">
        <div>
          <label class="block text-sm text-slate-300 mb-1.5">资产类型 *</label>
          <div class="grid grid-cols-2 gap-3">
            <button type="button" @click="form.assetType = 'DATASET'"
              :class="form.assetType === 'DATASET' ? 'border-accent bg-accent/10 text-accent' : 'border-dark-border text-slate-400'"
              class="p-3 rounded-lg border transition-all text-center">
              📦 数据集
            </button>
            <button type="button" @click="form.assetType = 'API'"
              :class="form.assetType === 'API' ? 'border-primary bg-primary/10 text-primary' : 'border-dark-border text-slate-400'"
              class="p-3 rounded-lg border transition-all text-center">
              🔌 API
            </button>
          </div>
        </div>

        <div>
          <label class="block text-sm text-slate-300 mb-1.5">标题 *</label>
          <input v-model="form.title" class="glass-input" placeholder="给你的资产起个名字" required maxlength="200" />
        </div>

        <div>
          <label class="block text-sm text-slate-300 mb-1.5">描述</label>
          <textarea v-model="form.description" class="glass-input" rows="4" placeholder="资产描述、用途、数据格式等"></textarea>
        </div>

        <div>
          <label class="block text-sm text-slate-300 mb-1.5">端点 URL</label>
          <input v-model="form.endpointUrl" class="glass-input" placeholder="https://api.example.com/v1/data" />
        </div>

        <div>
          <label class="block text-sm text-slate-300 mb-1.5">价格 (元/月) *</label>
          <input v-model.number="form.pricePerMonth" class="glass-input" type="number" min="0" step="0.01" placeholder="0.00 表示免费" required />
        </div>

        <div>
          <label class="block text-sm text-slate-300 mb-1.5">标签</label>
          <input v-model="form.tags" class="glass-input" placeholder="多个标签用逗号分隔，如：天气,中国,API" />
        </div>

        <p v-if="error" class="text-red-400 text-sm">{{ error }}</p>

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
