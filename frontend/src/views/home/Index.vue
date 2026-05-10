<template>
  <div class="min-h-screen bg-slate-50">
    <NavBar :show-search="true" />

    <main class="max-w-6xl mx-auto px-4 sm:px-6 py-6">
      <!-- Hero Section -->
      <section class="grid lg:grid-cols-[1.2fr_0.8fr] gap-6">
        <Banner :banners="homeData.banners" />

        <div class="rounded-3xl bg-white shadow-sm p-5">
          <div class="flex items-center justify-between">
            <h3 class="font-display text-xl">快速搜索</h3>
            <span class="text-xs text-slate-400">热门关键词</span>
          </div>
          <div
            class="mt-4 flex items-center gap-3 rounded-2xl border border-slate-200 px-4 py-3 focus-within:border-primary-500 cursor-pointer"
            @click="$router.push('/search')"
          >
            <i class="fa-solid fa-magnifying-glass text-slate-400"></i>
            <span class="text-sm text-slate-400">搜索水果名称</span>
          </div>
          <div class="mt-4 flex flex-wrap gap-2">
            <span
              v-for="keyword in hotKeywords"
              :key="keyword"
              class="px-3 py-1 rounded-full bg-slate-100 text-xs cursor-pointer hover:bg-primary-100 hover:text-primary-600 transition"
              @click="$router.push(`/search?keyword=${keyword}`)"
            >
              {{ keyword }}
            </span>
          </div>
          <div class="mt-6 grid grid-cols-3 gap-3 text-center text-xs">
            <div class="rounded-2xl border border-slate-100 py-4">
              <i class="fa-solid fa-truck-fast text-primary-500"></i>
              <p class="mt-2 text-slate-600">1小时内达</p>
            </div>
            <div class="rounded-2xl border border-slate-100 py-4">
              <i class="fa-solid fa-shield-heart text-primary-500"></i>
              <p class="mt-2 text-slate-600">品质保障</p>
            </div>
            <div class="rounded-2xl border border-slate-100 py-4">
              <i class="fa-solid fa-seedling text-primary-500"></i>
              <p class="mt-2 text-slate-600">产地直供</p>
            </div>
          </div>
        </div>
      </section>

      <!-- Categories -->
      <section class="mt-8">
        <div class="flex items-center justify-between">
          <h3 class="font-display text-2xl">水果分类</h3>
          <router-link to="/category" class="text-sm text-primary-500">全部分类</router-link>
        </div>
        <div class="mt-4">
          <CategoryGrid :categories="homeData.categories" />
        </div>
      </section>

      <!-- Hot Fruits -->
      <section class="mt-8">
        <div class="flex items-center justify-between">
          <h3 class="font-display text-2xl">热门推荐</h3>
          <router-link to="/category" class="text-sm text-primary-500">查看更多</router-link>
        </div>
        <div class="mt-4 grid sm:grid-cols-2 lg:grid-cols-4 gap-4">
          <FruitCard v-for="fruit in homeData.hotFruits" :key="fruit.id" :fruit="fruit" />
        </div>
      </section>

      <!-- New Fruits -->
      <section class="mt-8">
        <div class="flex items-center justify-between">
          <h3 class="font-display text-2xl">新鲜上架</h3>
          <router-link to="/category" class="text-sm text-primary-500">查看更多</router-link>
        </div>
        <div class="mt-4 grid sm:grid-cols-2 lg:grid-cols-4 gap-4">
          <FruitCard v-for="fruit in homeData.newFruits" :key="fruit.id" :fruit="fruit" />
        </div>
      </section>
    </main>

    <TabBar />
    <Loading :visible="loading" />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import NavBar from '@/components/common/NavBar.vue'
import TabBar from '@/components/common/TabBar.vue'
import Loading from '@/components/common/Loading.vue'
import Banner from '@/components/home/Banner.vue'
import CategoryGrid from '@/components/home/CategoryGrid.vue'
import FruitCard from '@/components/home/FruitCard.vue'
import { getHomeData } from '@/api/fruit'

const loading = ref(false)
const hotKeywords = ['车厘子', '榴莲', '蓝莓', '草莓']

const homeData = ref({
  banners: [
    { id: 1, image: 'https://images.unsplash.com/photo-1610832958506-aa56368176cf?w=800&auto=format&fit=crop&q=80', title: '新鲜草莓', link: '/fruit/8' },
    { id: 2, image: 'https://images.unsplash.com/photo-1519996529931-28324d5a630e?w=800&auto=format&fit=crop&q=80', title: '智利车厘子', link: '/fruit/1' },
    { id: 3, image: 'https://images.unsplash.com/photo-1546548970-71785318a17b?w=800&auto=format&fit=crop&q=80', title: '热带水果', link: '/category/1' }
  ],
  categories: [
    { id: 1, name: '热带水果' },
    { id: 2, name: '浆果类' },
    { id: 3, name: '柑橘类' },
    { id: 4, name: '核果类' },
    { id: 5, name: '瓜果类' },
    { id: 6, name: '进口水果' }
  ],
  hotFruits: [],
  newFruits: []
})

onMounted(async () => {
  loading.value = true
  try {
    const data = await getHomeData()
    if (data) {
      homeData.value = { ...homeData.value, ...data }
    }
  } catch {
    // 使用假数据
    homeData.value.hotFruits = [
      { id: 1, name: '智利车厘子', description: 'JJ级大果 · 2斤装', price: 99.9, mainImage: 'https://images.unsplash.com/photo-1528821154947-1aa3d1b74941?w=600&auto=format&fit=crop&q=80', isHot: true },
      { id: 2, name: '泰国金枕头榴莲', description: '4-5斤/个', price: 199, mainImage: 'https://images.unsplash.com/photo-1562486683-67d4d5886f99?w=600&auto=format&fit=crop&q=80', isHot: true },
      { id: 4, name: '云南蓝莓', description: '125g×4盒', price: 69.9, mainImage: 'https://images.unsplash.com/photo-1498557850523-fd3d118b962e?w=600&auto=format&fit=crop&q=80', isHot: true, isNew: true },
      { id: 5, name: '海南金煌芒果', description: '甜度高 · 5斤装', price: 35.9, mainImage: 'https://images.unsplash.com/photo-1553279768-865429fa0078?w=600&auto=format&fit=crop&q=80', isNew: true }
    ]
    homeData.value.newFruits = [
      { id: 8, name: '丹东草莓', description: '鲜嫩多汁 · 草莓界天花板', price: 49.9, mainImage: 'https://images.unsplash.com/photo-1464965911861-746a04b4bca6?w=600&auto=format&fit=crop&q=80', isNew: true },
      { id: 6, name: '红心火龙果', description: '清甜爽口 · 营养丰富', price: 12.9, mainImage: 'https://images.unsplash.com/photo-1527325678964-54921661f888?w=600&auto=format&fit=crop&q=80' },
      { id: 7, name: '赣南脐橙', description: '皮薄多汁 · 酸甜适中', price: 19.9, mainImage: 'https://images.unsplash.com/photo-1547514701-42782101795e?w=600&auto=format&fit=crop&q=80' },
      { id: 10, name: '西瓜', description: '皮薄肉甜 · 消暑解渴', price: 15.9, mainImage: 'https://images.unsplash.com/photo-1587049352846-4a222e784d38?w=600&auto=format&fit=crop&q=80' }
    ]
  } finally {
    loading.value = false
  }
})
</script>
