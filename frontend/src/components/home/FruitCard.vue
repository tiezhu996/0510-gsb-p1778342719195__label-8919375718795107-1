<template>
  <router-link
    :to="`/fruit/${fruit.id}`"
    class="block rounded-2xl bg-white shadow-sm overflow-hidden hover:shadow-lg transition group"
  >
    <div class="relative overflow-hidden">
      <img
        :src="fruit.mainImage || fruit.image"
        :alt="fruit.name"
        class="h-36 w-full object-cover group-hover:scale-105 transition-transform duration-300"
      />
      <span
        v-if="fruit.isHot"
        class="absolute top-2 left-2 px-2 py-1 rounded-full bg-red-500 text-white text-xs"
      >
        热门
      </span>
      <span
        v-if="fruit.isNew"
        class="absolute top-2 left-2 px-2 py-1 rounded-full bg-primary-500 text-white text-xs"
        :class="{ 'left-14': fruit.isHot }"
      >
        新品
      </span>
    </div>
    <div class="p-4">
      <p class="text-sm font-medium text-ink line-clamp-1">{{ fruit.name }}</p>
      <p class="text-xs text-slate-500 mt-1 line-clamp-1">{{ fruit.description }}</p>
      <div class="mt-3 flex items-center justify-between">
        <div>
          <span class="text-primary-500 font-semibold">¥{{ fruit.price }}</span>
          <span v-if="fruit.originalPrice" class="ml-1 text-xs text-slate-400 line-through">
            ¥{{ fruit.originalPrice }}
          </span>
        </div>
        <button
          @click.prevent="handleAddToCart"
          class="h-9 w-9 rounded-full bg-primary-50 text-primary-500 hover:bg-primary-100 transition flex items-center justify-center"
        >
          <i class="fa-solid fa-plus"></i>
        </button>
      </div>
    </div>
  </router-link>
</template>

<script setup>
import { useCartStore } from '@/stores/cart'
import { useUserStore } from '@/stores/user'
import { useRouter } from 'vue-router'
import { showToast } from '@/utils/toast'

const props = defineProps({
  fruit: {
    type: Object,
    required: true
  }
})

const cartStore = useCartStore()
const userStore = useUserStore()
const router = useRouter()

async function handleAddToCart() {
  if (!userStore.isLoggedIn) {
    showToast('请先登录', 'warning')
    router.push('/login')
    return
  }
  await cartStore.addToCart(props.fruit.id, null, 1)
}
</script>
