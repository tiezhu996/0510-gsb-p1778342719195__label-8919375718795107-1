<template>
  <div class="min-h-screen bg-slate-50">
    <NavBar :title="fruit?.name || '商品详情'" :show-back="true" />

    <main v-if="fruit" class="max-w-4xl mx-auto">
      <!-- Product Image -->
      <div class="relative">
        <img
          :src="currentImage"
          :alt="fruit.name"
          class="w-full aspect-square object-cover"
        />
        <!-- Image Thumbnails -->
        <div v-if="fruit.images?.length > 1" class="absolute bottom-4 left-0 right-0 flex justify-center gap-2">
          <button
            v-for="(img, index) in fruit.images"
            :key="index"
            @click="currentImageIndex = index"
            :class="[
              'w-2 h-2 rounded-full transition',
              currentImageIndex === index ? 'bg-white' : 'bg-white/50'
            ]"
          />
        </div>
      </div>

      <!-- Product Info -->
      <div class="px-4 sm:px-6 py-6 space-y-4">
        <div class="bg-white rounded-3xl p-6 shadow-sm">
          <div class="flex items-baseline gap-2">
            <span class="text-2xl font-bold text-primary-500">¥{{ (fruit.price + (selectedSpec?.price || 0)).toFixed(1) }}</span>
            <span v-if="fruit.originalPrice" class="text-sm text-slate-400 line-through">
              ¥{{ (fruit.originalPrice + (selectedSpec?.price || 0)).toFixed(1) }}
            </span>
            <span v-if="discount" class="ml-2 px-2 py-0.5 bg-red-100 text-red-500 text-xs rounded-full">
              {{ discount }}折
            </span>
          </div>
          <h1 class="mt-3 text-lg font-semibold">{{ fruit.name }}</h1>
          <p class="mt-2 text-sm text-slate-500">{{ fruit.description }}</p>
          <div class="mt-4 flex items-center gap-4 text-xs text-slate-400">
            <span>销量 {{ fruit.sales || 0 }}</span>
            <span>库存 {{ fruit.stock || 0 }}</span>
          </div>
        </div>

        <!-- Specs Selection -->
        <div class="bg-white rounded-3xl p-6 shadow-sm">
          <h3 class="text-sm font-medium">规格选择</h3>
          <div class="mt-3 flex flex-wrap gap-2">
            <button
              v-for="spec in fruit.specs || defaultSpecs"
              :key="spec.name"
              @click="!isSpecOutOfStock(spec) && (selectedSpec = spec)"
              :class="[
                'px-4 py-2 rounded-full text-sm transition',
                selectedSpec?.name === spec.name
                  ? 'bg-primary-500 text-white'
                  : isSpecOutOfStock(spec)
                  ? 'bg-slate-100 text-slate-400 cursor-not-allowed opacity-60'
                  : 'bg-slate-100 text-slate-600 hover:bg-slate-200'
              ]"
              :disabled="isSpecOutOfStock(spec)"
            >
              {{ spec.name }} (+¥{{ spec.price || 0 }})
              <span v-if="isSpecOutOfStock(spec)" class="ml-1 text-xs">(暂无库存)</span>
            </button>
          </div>
        </div>

        <!-- Quantity -->
        <div class="bg-white rounded-3xl p-6 shadow-sm">
          <div class="flex items-center justify-between">
            <h3 class="text-sm font-medium">购买数量</h3>
            <div class="flex items-center gap-3">
              <button
                @click="quantity = Math.max(1, quantity - 1)"
                class="w-8 h-8 rounded-full bg-slate-100 flex items-center justify-center hover:bg-slate-200 transition"
                :disabled="quantity <= 1"
              >
                <i class="fa-solid fa-minus text-xs"></i>
              </button>
              <span class="w-10 text-center font-medium">{{ quantity }}</span>
              <button
                @click="quantity = Math.min(99, quantity + 1)"
                class="w-8 h-8 rounded-full bg-slate-100 flex items-center justify-center hover:bg-slate-200 transition"
                :disabled="quantity >= (fruit.stock || 99)"
              >
                <i class="fa-solid fa-plus text-xs"></i>
              </button>
            </div>
          </div>
        </div>

        <!-- Product Details -->
        <div class="bg-white rounded-3xl p-6 shadow-sm">
          <h3 class="text-sm font-medium">商品详情</h3>
          <div class="mt-4 text-sm text-slate-600 space-y-2">
            <p><span class="text-slate-400">产地：</span>{{ fruit.origin || '进口' }}</p>
            <p><span class="text-slate-400">保质期：</span>{{ fruit.shelfLife || '7天' }}</p>
            <p><span class="text-slate-400">储存方式：</span>{{ fruit.storage || '冷藏' }}</p>
          </div>
          <div v-if="fruit.detailImages?.length" class="mt-4 space-y-2">
            <img
              v-for="(img, index) in fruit.detailImages"
              :key="index"
              :src="img"
              class="w-full rounded-xl"
            />
          </div>
        </div>
      </div>
    </main>

    <!-- Bottom Action Bar -->
    <div class="fixed bottom-0 left-0 right-0 bg-white border-t border-slate-100 px-4 py-3 safe-area-pb">
      <div class="max-w-4xl mx-auto flex items-center gap-3">
        <router-link to="/cart" class="flex flex-col items-center text-slate-500">
          <i class="fa-solid fa-cart-shopping text-lg"></i>
          <span class="text-xs mt-1">购物车</span>
        </router-link>
        <button
          @click="handleAddToCart"
          :disabled="adding || isOffline"
          class="flex-1 py-3 rounded-2xl border-2 border-primary-500 text-primary-500 font-medium hover:bg-primary-50 transition disabled:opacity-50"
        >
          加入购物车
        </button>
        <button
          @click="handleBuyNow"
          :disabled="adding || isOffline"
          class="flex-1 py-3 rounded-2xl bg-primary-500 text-white font-medium hover:bg-primary-600 transition disabled:opacity-50"
        >
          立即购买
        </button>
      </div>
    </div>

    <Loading :visible="loading" />
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import NavBar from '@/components/common/NavBar.vue'
import Loading from '@/components/common/Loading.vue'
import { getFruitDetail } from '@/api/fruit'
import { useCartStore } from '@/stores/cart'
import { useUserStore } from '@/stores/user'
import { showToast } from '@/utils/toast'

const route = useRoute()
const router = useRouter()
const cartStore = useCartStore()
const userStore = useUserStore()

const loading = ref(false)
const adding = ref(false)
const fruit = ref(null)
const currentImageIndex = ref(0)
const quantity = ref(1)
const selectedSpec = ref(null)
const isOffline = ref(false) // 商品是否下架

const defaultSpecs = [
  { name: '标准装', price: 0 },
  { name: '家庭装', price: 20 },
  { name: '豪华装', price: 50 }
]

const currentImage = computed(() => {
  if (fruit.value?.images?.length) {
    return fruit.value.images[currentImageIndex.value]
  }
  return fruit.value?.image
})

const discount = computed(() => {
  if (fruit.value?.originalPrice && fruit.value?.price) {
    return Math.round((fruit.value.price / fruit.value.originalPrice) * 10)
  }
  return null
})

// 检查规格是否库存不足
function isSpecOutOfStock(spec) {
  if (!spec) return false
  // 对于从API返回的规格，检查stock字段
  if (spec.stock !== undefined) {
    return spec.stock <= 0
  }
  // 对于默认规格，假设有库存
  return false
}

onMounted(() => {
  fetchDetail()
})

async function fetchDetail() {
  loading.value = true
  isOffline.value = false
  try {
    fruit.value = await getFruitDetail(route.params.id)
    if (fruit.value.specs?.length) {
      selectedSpec.value = fruit.value.specs[0]
    } else {
      selectedSpec.value = defaultSpecs[0]
    }
  } catch (error) {
    // 商品已下架或不存在
    isOffline.value = true
    fruit.value = {
      id: route.params.id,
      name: '智利车厘子',
      price: 99.9,
      originalPrice: 129,
      image: 'https://images.unsplash.com/photo-1528821128474-27f963b062bf?w=600&auto=format&fit=crop&q=80',
      images: [
        'https://images.unsplash.com/photo-1528821128474-27f963b062bf?w=600&auto=format&fit=crop&q=80',
        'https://images.unsplash.com/photo-1559181567-c3190ca9959b?w=600&auto=format&fit=crop&q=80'
      ],
      description: '新鲜直达，果肉饱满，甜度爆表',
      sales: 2341,
      stock: 0, // 下架商品库存为0
      origin: '智利',
      shelfLife: '冷藏7天',
      storage: '0-4°C冷藏保存'
    }
    selectedSpec.value = { ...defaultSpecs[0], stock: 0 }
  } finally {
    loading.value = false
  }
}

async function handleAddToCart() {
  if (!userStore.isLoggedIn) {
    showToast('请先登录', 'warning')
    router.push('/login?redirect=' + route.fullPath)
    return
  }

  // 检查选中规格的库存
  if (isSpecOutOfStock(selectedSpec.value)) {
    showToast('该规格暂无库存', 'error')
    return
  }

  // 检查购买数量是否超过库存
  const specStock = selectedSpec.value?.stock || 0
  if (quantity.value > specStock) {
    showToast(`库存不足，当前库存仅${specStock}件`, 'error')
    return
  }

  adding.value = true
  try {
    await cartStore.addToCart(
      fruit.value.id,
      selectedSpec.value?.id,
      quantity.value
    )
    showToast('已加入购物车', 'success')
  } catch {
    // Error handled by store
  } finally {
    adding.value = false
  }
}

async function handleBuyNow() {
  if (!userStore.isLoggedIn) {
    showToast('请先登录', 'warning')
    router.push('/login?redirect=' + route.fullPath)
    return
  }

  // 检查选中规格的库存
  if (isSpecOutOfStock(selectedSpec.value)) {
    showToast('该规格暂无库存', 'error')
    return
  }

  // 检查购买数量是否超过库存
  const specStock = selectedSpec.value?.stock || 0
  if (quantity.value > specStock) {
    showToast(`库存不足，当前库存仅${specStock}件`, 'error')
    return
  }

  adding.value = true
  try {
    await cartStore.addToCart(
      fruit.value.id,
      selectedSpec.value?.id,
      quantity.value
    )
    router.push('/order/confirm')
  } catch {
    // Error handled by store
  } finally {
    adding.value = false
  }
}
</script>

<style scoped>
.safe-area-pb {
  padding-bottom: max(0.75rem, env(safe-area-inset-bottom));
}
</style>
