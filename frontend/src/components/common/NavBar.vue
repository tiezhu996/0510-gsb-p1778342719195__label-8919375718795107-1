<template>
  <header class="sticky top-0 bg-white/95 backdrop-blur-sm border-b border-slate-100 z-30">
    <div class="max-w-6xl mx-auto px-4 sm:px-6 py-3 flex items-center justify-between gap-4">
      <div class="flex items-center gap-3">
        <button
          v-if="showBack"
          @click="goBack"
          class="h-10 w-10 rounded-full bg-slate-50 border border-slate-200 flex items-center justify-center hover:bg-slate-100 transition"
        >
          <i class="fa-solid fa-arrow-left text-slate-600"></i>
        </button>
        <div v-else class="flex items-center gap-3">
          <div class="h-10 w-10 rounded-2xl bg-primary-500 text-white flex items-center justify-center">
            <i class="fa-solid fa-leaf"></i>
          </div>
          <div>
            <p class="text-xs text-slate-500">FruitShop Online</p>
            <h1 class="font-display text-lg text-ink">水果在线购</h1>
          </div>
        </div>
        <h1 v-if="title && showBack" class="text-lg font-semibold text-ink">{{ title }}</h1>
      </div>

      <div class="flex items-center gap-3">
        <button
          v-if="showSearch"
          @click="$router.push('/search')"
          class="h-10 w-10 rounded-full bg-slate-50 border border-slate-200 flex items-center justify-center hover:bg-slate-100 transition"
        >
          <i class="fa-solid fa-magnifying-glass text-slate-600"></i>
        </button>
        <router-link
          v-if="showCart"
          to="/cart"
          class="relative h-10 w-10 rounded-full bg-primary-500 text-white flex items-center justify-center hover:bg-primary-600 transition"
        >
          <i class="fa-solid fa-bag-shopping"></i>
          <span
            v-if="cartCount > 0"
            class="absolute -top-1 -right-1 h-5 min-w-5 px-1 rounded-full bg-red-500 text-white text-xs flex items-center justify-center"
          >
            {{ cartCount > 99 ? '99+' : cartCount }}
          </span>
        </router-link>
        <slot name="right"></slot>
      </div>
    </div>
  </header>
</template>

<script setup>
import { computed } from 'vue'
import { useRouter } from 'vue-router'
import { useCartStore } from '@/stores/cart'

const props = defineProps({
  title: String,
  showBack: {
    type: Boolean,
    default: false
  },
  showSearch: {
    type: Boolean,
    default: false
  },
  showCart: {
    type: Boolean,
    default: true
  }
})

const router = useRouter()
const cartStore = useCartStore()

const cartCount = computed(() => cartStore.totalCount)

function goBack() {
  router.back()
}
</script>
