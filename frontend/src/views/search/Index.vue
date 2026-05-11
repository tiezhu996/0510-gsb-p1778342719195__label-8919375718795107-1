<template>
  <div class="min-h-screen bg-slate-50">
    <NavBar title="搜索" :show-back="true" />

    <main class="max-w-4xl mx-auto px-4 sm:px-6 py-4">
      <!-- Search Input -->
      <div class="flex items-center gap-3 bg-white rounded-2xl px-4 py-3 shadow-sm">
        <i class="fa-solid fa-magnifying-glass text-slate-400"></i>
        <input
          v-model="keyword"
          ref="searchInput"
          class="flex-1 outline-none text-sm"
          placeholder="搜索水果..."
          @keyup.enter="handleSearch"
        />
        <button
          v-if="keyword"
          @click="keyword = ''"
          class="text-slate-400 hover:text-slate-600"
        >
          <i class="fa-solid fa-xmark"></i>
        </button>
        <button
          @click="handleSearch"
          class="px-4 py-1.5 bg-primary-500 text-white text-sm rounded-full hover:bg-primary-600 transition"
        >
          搜索
        </button>
      </div>

      <!-- Search History -->
      <section v-if="!searched && history.length > 0" class="mt-6">
        <div class="flex items-center justify-between">
          <h3 class="text-sm font-medium text-slate-700">搜索历史</h3>
          <button @click="clearHistory" class="text-xs text-slate-400 hover:text-slate-600">
            清空
          </button>
        </div>
        <div class="mt-3 flex flex-wrap gap-2">
          <button
            v-for="(item, index) in history"
            :key="index"
            @click="searchFromHistory(item)"
            class="px-3 py-1.5 bg-white rounded-full text-sm text-slate-600 hover:bg-slate-100 transition"
          >
            {{ item }}
          </button>
        </div>
      </section>

      <!-- Hot Keywords -->
      <section v-if="!searched" class="mt-6">
        <h3 class="text-sm font-medium text-slate-700">热门搜索</h3>
        <div class="mt-3 flex flex-wrap gap-2">
          <button
            v-for="(item, index) in hotKeywords"
            :key="index"
            @click="searchFromHistory(item)"
            class="px-3 py-1.5 bg-white rounded-full text-sm text-slate-600 hover:bg-slate-100 transition"
          >
            {{ item }}
          </button>
        </div>
      </section>

      <!-- Search Results -->
      <section v-if="searched" class="mt-6">
        <div class="flex items-center justify-between mb-4">
          <p class="text-sm text-slate-500">
            找到 <span class="text-primary-500 font-medium">{{ results.length }}</span> 个结果
          </p>
          <div class="flex items-center gap-2 text-sm">
            <button
              v-for="option in sortOptions"
              :key="option.value"
              @click="sortBy = option.value"
              :class="[
                'px-2 py-1 rounded transition',
                sortBy === option.value
                  ? 'text-primary-500 font-medium'
                  : 'text-slate-400 hover:text-slate-600'
              ]"
            >
              {{ option.label }}
            </button>
          </div>
        </div>

        <div class="grid grid-cols-2 sm:grid-cols-3 lg:grid-cols-4 gap-3">
          <FruitCard
            v-for="fruit in sortedResults"
            :key="fruit.id"
            :fruit="fruit"
          />
        </div>

        <Empty v-if="results.length === 0 && !loading" text="未找到相关商品" />
      </section>
    </main>

    <Loading :visible="loading" />
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import NavBar from '@/components/common/NavBar.vue'
import Loading from '@/components/common/Loading.vue'
import Empty from '@/components/common/Empty.vue'
import FruitCard from '@/components/home/FruitCard.vue'
import { getFruitList } from '@/api/fruit'
import { getItem, setItem } from '@/utils/storage'

const route = useRoute()
const searchInput = ref(null)
const loading = ref(false)
const keyword = ref('')
const searched = ref(false)
const results = ref([])
const history = ref([])
const sortBy = ref('default')

const hotKeywords = ['车厘子', '榴莲', '草莓', '芒果', '葡萄', '苹果']

const sortOptions = [
  { label: '综合', value: 'default' },
  { label: '销量', value: 'sales' },
  { label: '价格', value: 'price' }
]

const sortedResults = computed(() => {
  if (!results.value || !Array.isArray(results.value)) {
    return []
  }
  const list = [...results.value]
  switch (sortBy.value) {
    case 'sales':
      return list.sort((a, b) => (b.sales || 0) - (a.sales || 0))
    case 'price':
      return list.sort((a, b) => a.price - b.price)
    default:
      return list
  }
})

onMounted(() => {
  history.value = getItem('searchHistory') || []
  if (route.query.keyword) {
    keyword.value = route.query.keyword
    handleSearch()
  } else {
    searchInput.value?.focus()
  }
})

function searchFromHistory(item) {
  keyword.value = item
  handleSearch()
}

async function handleSearch() {
  if (!keyword.value.trim()) return

  // Save to history
  const newHistory = [keyword.value, ...history.value.filter(h => h !== keyword.value)].slice(0, 10)
  history.value = newHistory
  setItem('searchHistory', newHistory)

  loading.value = true
  searched.value = true
  try {
    const result = await getFruitList({ keyword: keyword.value })
    // Handle paginated response format: { data: { list: [...] } }
    if (result && result.list && Array.isArray(result.list)) {
      results.value = result.list
    } else if (Array.isArray(result)) {
      results.value = result
    } else {
      results.value = []
    }
  } catch {
    results.value = [
      { id: 1, name: '智利车厘子', price: 99.9, originalPrice: 129, mainImage: 'https://images.unsplash.com/photo-1528821128474-27f963b062bf?w=300&auto=format&fit=crop&q=80', sales: 2341 },
      { id: 2, name: '新疆库尔勒香梨', price: 35.9, originalPrice: 45, mainImage: 'https://images.unsplash.com/photo-1514756331096-242fdeb70d4a?w=300&auto=format&fit=crop&q=80', sales: 1567 }
    ]
  } finally {
    loading.value = false
  }
}

function clearHistory() {
  history.value = []
  setItem('searchHistory', [])
}
</script>
