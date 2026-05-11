<template>
  <div class="min-h-screen bg-slate-50">
    <NavBar title="分类" :show-back="false" />

    <main class="max-w-6xl mx-auto px-4 sm:px-6 py-4">
      <div class="flex gap-4">
        <!-- Category Sidebar -->
        <aside class="w-24 sm:w-32 flex-shrink-0">
          <div class="bg-white rounded-2xl overflow-hidden shadow-sm">
            <button
              v-for="cat in categories"
              :key="cat.id"
              @click="currentCategory = cat.id"
              :class="[
                'w-full px-3 py-4 text-sm text-left transition',
                currentCategory === cat.id
                  ? 'bg-primary-500 text-white font-medium'
                  : 'text-slate-600 hover:bg-slate-50'
              ]"
            >
              {{ cat.name }}
            </button>
          </div>
        </aside>

        <!-- Product Grid -->
        <div class="flex-1">
          <!-- Sort Bar -->
          <div class="bg-white rounded-2xl p-3 shadow-sm flex items-center gap-2 text-sm mb-4">
            <button
              v-for="option in sortOptions"
              :key="option.value"
              @click="sortBy = option.value"
              :class="[
                'px-3 py-1.5 rounded-full transition',
                sortBy === option.value
                  ? 'bg-primary-500 text-white'
                  : 'text-slate-500 hover:bg-slate-100'
              ]"
            >
              {{ option.label }}
            </button>
          </div>

          <!-- Products -->
          <div class="grid grid-cols-2 sm:grid-cols-3 lg:grid-cols-4 gap-3">
            <FruitCard
              v-for="fruit in sortedFruits"
              :key="fruit.id"
              :fruit="fruit"
            />
          </div>

          <Empty v-if="fruits.length === 0 && !loading" text="暂无商品" />
        </div>
      </div>
    </main>

    <TabBar />
    <Loading :visible="loading" />
  </div>
</template>

<script setup>
import { ref, computed, watch, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import NavBar from '@/components/common/NavBar.vue'
import TabBar from '@/components/common/TabBar.vue'
import Loading from '@/components/common/Loading.vue'
import Empty from '@/components/common/Empty.vue'
import FruitCard from '@/components/home/FruitCard.vue'
import { getCategoryList, getFruitList } from '@/api/fruit'

const route = useRoute()
const loading = ref(false)
const categories = ref([])
const currentCategory = ref(null)
const fruits = ref([])
const sortBy = ref('default')

const sortOptions = [
  { label: '综合', value: 'default' },
  { label: '销量', value: 'sales' },
  { label: '价格升', value: 'price_asc' },
  { label: '价格降', value: 'price_desc' }
]

const sortedFruits = computed(() => {
  if (!fruits.value || !Array.isArray(fruits.value)) {
    return []
  }
  const list = [...fruits.value]
  switch (sortBy.value) {
    case 'sales':
      return list.sort((a, b) => (b.sales || 0) - (a.sales || 0))
    case 'price_asc':
      return list.sort((a, b) => a.price - b.price)
    case 'price_desc':
      return list.sort((a, b) => b.price - a.price)
    default:
      return list
  }
})

onMounted(async () => {
  await fetchCategories()
  if (route.query.id) {
    currentCategory.value = Number(route.query.id)
  } else if (categories.value.length > 0) {
    currentCategory.value = categories.value[0].id
  }
})

watch(currentCategory, () => {
  if (currentCategory.value) {
    fetchFruits()
  }
})

async function fetchCategories() {
  try {
    categories.value = await getCategoryList()
  } catch {
    categories.value = [
      { id: 1, name: '时令鲜果' },
      { id: 2, name: '进口水果' },
      { id: 3, name: '热带水果' },
      { id: 4, name: '浆果莓类' },
      { id: 5, name: '瓜果类' },
      { id: 6, name: '柑橘类' }
    ]
  }
}

async function fetchFruits() {
  loading.value = true
  try {
    const result = await getFruitList({ categoryId: currentCategory.value })
    // 处理后端返回的分页格式: { data: { list: [...] } }
    if (result && result.list && Array.isArray(result.list)) {
      fruits.value = result.list
    } else if (Array.isArray(result)) {
      fruits.value = result
    } else {
      fruits.value = []
    }
  } catch {
    fruits.value = [
      { id: 1, name: '智利车厘子', price: 99.9, originalPrice: 129, mainImage: 'https://images.unsplash.com/photo-1528821128474-27f963b062bf?w=300&auto=format&fit=crop&q=80', sales: 2341 },
      { id: 2, name: '泰国金枕榴莲', price: 168, originalPrice: 198, mainImage: 'https://images.unsplash.com/photo-1588165171080-c89acfa5ee83?w=300&auto=format&fit=crop&q=80', sales: 1832 },
      { id: 3, name: '日本晴王葡萄', price: 128, originalPrice: 158, mainImage: 'https://images.unsplash.com/photo-1537640538966-79f369143f8f?w=300&auto=format&fit=crop&q=80', sales: 967 },
      { id: 4, name: '新西兰奇异果', price: 49.9, originalPrice: 69, mainImage: 'https://images.unsplash.com/photo-1585059895524-72359e06133a?w=300&auto=format&fit=crop&q=80', sales: 3421 }
    ]
  } finally {
    loading.value = false
  }
}
</script>
