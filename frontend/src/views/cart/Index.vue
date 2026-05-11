<template>
  <div class="min-h-screen bg-slate-50 pb-40">
    <NavBar title="购物车" :show-back="false" />

    <main class="max-w-4xl mx-auto px-4 sm:px-6 py-4">
      <!-- Valid Cart Items -->
      <div v-if="validItems.length > 0" class="space-y-3 mb-6">
        <h3 class="text-sm font-medium text-slate-700">正常商品</h3>
        <div
          v-for="item in validItems"
          :key="item.id"
          class="bg-white rounded-2xl p-4 shadow-sm"
        >
          <div class="flex gap-4">
            <!-- Checkbox -->
            <div class="flex items-center">
              <input
                type="checkbox"
                :checked="item.selected"
                @change="cartStore.toggleSelect(item.id)"
                class="w-5 h-5 rounded text-primary-500 focus:ring-primary-500"
              />
            </div>

            <!-- Image -->
            <router-link :to="`/fruit/${item.fruitId}`" class="flex-shrink-0">
              <img
                :src="item.image"
                :alt="item.name"
                class="w-20 h-20 rounded-xl object-cover"
              />
            </router-link>

            <!-- Info -->
            <div class="flex-1 min-w-0">
              <router-link :to="`/fruit/${item.fruitId}`" class="font-medium text-sm line-clamp-2 hover:text-primary-500">
                {{ item.name }}
              </router-link>
              <p v-if="item.spec" class="mt-1 text-xs text-slate-400">{{ item.spec }}</p>
              <!-- 库存不足提示 -->
              <p v-if="item.stock !== null && item.quantity > item.stock" class="mt-1 text-xs text-red-500">
                库存不足，仅剩 {{ item.stock }} 件
              </p>
              <div class="mt-2 flex items-center justify-between">
                <span class="text-primary-500 font-semibold">¥{{ item.price }}</span>
                <div class="flex items-center gap-2">
                  <button
                    @click="updateQuantity(item.id, item.quantity - 1)"
                    :disabled="item.quantity <= 1"
                    class="w-7 h-7 rounded-full bg-slate-100 flex items-center justify-center hover:bg-slate-200 transition disabled:opacity-50"
                  >
                    <i class="fa-solid fa-minus text-xs"></i>
                  </button>
                  <span class="w-8 text-center text-sm">{{ item.quantity }}</span>
                  <button
                    @click="updateQuantity(item.id, item.quantity + 1)"
                    :disabled="item.quantity >= 99 || (item.stock !== null && item.quantity >= item.stock)"
                    class="w-7 h-7 rounded-full bg-slate-100 flex items-center justify-center hover:bg-slate-200 transition disabled:opacity-50"
                  >
                    <i class="fa-solid fa-plus text-xs"></i>
                  </button>
                </div>
              </div>
            </div>

            <!-- Delete -->
            <button
              @click="handleRemove(item.id)"
              class="text-slate-400 hover:text-red-500 transition"
            >
              <i class="fa-solid fa-trash-can"></i>
            </button>
          </div>
        </div>
      </div>

      <!-- Invalid Cart Items -->
      <div v-if="invalidItems.length > 0" class="space-y-3">
        <h3 class="text-sm font-medium text-slate-500">失效商品</h3>
        <div
          v-for="item in invalidItems"
          :key="item.id"
          class="bg-slate-100 rounded-2xl p-4 shadow-sm opacity-70"
        >
          <div class="flex gap-4">
            <!-- No Checkbox for invalid items -->
            <div class="flex items-center w-5">
              <span class="px-2 py-1 bg-slate-300 text-slate-600 text-xs rounded">已失效</span>
            </div>

            <!-- Image (grayscale) -->
            <div class="flex-shrink-0">
              <img
                :src="item.image"
                :alt="item.name"
                class="w-20 h-20 rounded-xl object-cover grayscale"
              />
            </div>

            <!-- Info -->
            <div class="flex-1 min-w-0">
              <p class="font-medium text-sm text-slate-500 line-clamp-2">
                {{ item.name }}
              </p>
              <p v-if="item.spec" class="mt-1 text-xs text-slate-400">{{ item.spec }}</p>
              <div class="mt-2 flex items-center justify-between">
                <span class="text-slate-400 font-semibold">¥{{ item.price }}</span>
                <span class="text-xs text-slate-400">商品已下架</span>
              </div>
            </div>

            <!-- Delete -->
            <button
              @click="handleRemove(item.id)"
              class="text-slate-400 hover:text-red-500 transition"
            >
              <i class="fa-solid fa-trash-can"></i>
            </button>
          </div>
        </div>
      </div>

      <Empty v-if="cartStore.items.length === 0" text="购物车空空如也" />
    </main>

    <!-- Bottom Bar -->
    <div v-if="cartStore.items.length > 0" class="fixed bottom-16 left-0 right-0 bg-white border-t border-slate-100 px-4 py-3 z-50 safe-area-pb">
      <div class="max-w-4xl mx-auto flex items-center gap-4">
        <label class="flex items-center gap-2 cursor-pointer">
          <input
            type="checkbox"
            :checked="cartStore.isAllSelected"
            @change="cartStore.selectAll(!cartStore.isAllSelected)"
            class="w-5 h-5 rounded text-primary-500 focus:ring-primary-500"
          />
          <span class="text-sm text-slate-600">全选</span>
        </label>
        <div class="flex-1 text-right">
          <span class="text-sm text-slate-500">合计：</span>
          <span class="text-xl font-bold text-primary-500">¥{{ cartStore.totalPrice.toFixed(2) }}</span>
        </div>
        <button
          @click="handleCheckout"
          :disabled="cartStore.selectedCount === 0"
          class="px-6 py-2.5 rounded-2xl bg-primary-500 text-white font-medium hover:bg-primary-600 transition disabled:opacity-50"
        >
          结算({{ cartStore.selectedCount }})
        </button>
      </div>
    </div>

    <TabBar />
    <Loading :visible="cartStore.loading" />

    <!-- 删除确认弹窗 -->
    <ConfirmDialog
      v-model:visible="showDeleteDialog"
      title="删除商品"
      message="确定要删除该商品吗？"
      type="danger"
      confirmText="删除"
      @confirm="confirmRemove"
    />
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRouter } from 'vue-router'
import NavBar from '@/components/common/NavBar.vue'
import TabBar from '@/components/common/TabBar.vue'
import Loading from '@/components/common/Loading.vue'
import Empty from '@/components/common/Empty.vue'
import ConfirmDialog from '@/components/common/ConfirmDialog.vue'
import { useCartStore } from '@/stores/cart'
import { useUserStore } from '@/stores/user'
import { showToast } from '@/utils/toast'

const router = useRouter()
const cartStore = useCartStore()
const userStore = useUserStore()

// 删除确认弹窗状态
const showDeleteDialog = ref(false)
const pendingDeleteId = ref(null)

// Valid items (status !== 0)
const validItems = computed(() => {
  return cartStore.items.filter(item => item.status !== 0)
})

// Invalid items (status === 0)
const invalidItems = computed(() => {
  return cartStore.items.filter(item => item.status === 0)
})

onMounted(() => {
  if (userStore.isLoggedIn) {
    cartStore.fetchCart()
  }
})

async function updateQuantity(id, quantity) {
  if (quantity < 1 || quantity > 99) return
  await cartStore.updateQuantity(id, quantity)
}

async function handleRemove(id) {
  pendingDeleteId.value = id
  showDeleteDialog.value = true
}

async function confirmRemove() {
  if (pendingDeleteId.value) {
    await cartStore.removeItem(pendingDeleteId.value)
    pendingDeleteId.value = null
  }
}

function handleCheckout() {
  if (!userStore.isLoggedIn) {
    showToast('请先登录', 'warning')
    router.push('/login?redirect=/cart')
    return
  }
  if (cartStore.selectedCount === 0) {
    showToast('请选择商品', 'warning')
    return
  }
  router.push('/order/confirm')
}
</script>

<style scoped>
.safe-area-pb {
  padding-bottom: max(0.75rem, env(safe-area-inset-bottom));
}
</style>
