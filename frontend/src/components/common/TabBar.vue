<template>
  <nav class="fixed bottom-0 left-0 right-0 bg-white border-t border-slate-100 px-4 py-3 z-40">
    <div class="max-w-6xl mx-auto flex items-center justify-around">
      <router-link
        v-for="item in tabs"
        :key="item.path"
        :to="item.path"
        class="flex flex-col items-center gap-1 text-xs transition-colors"
        :class="isActive(item.path) ? 'text-primary-500' : 'text-slate-400'"
      >
        <i :class="[item.icon, 'text-lg']"></i>
        <span>{{ item.name }}</span>
      </router-link>
    </div>
  </nav>
  <div class="h-16"></div>
</template>

<script setup>
import { computed } from 'vue'
import { useRoute } from 'vue-router'
import { useCartStore } from '@/stores/cart'

const route = useRoute()
const cartStore = useCartStore()

const tabs = computed(() => [
  { path: '/', name: '首页', icon: 'fa-solid fa-house' },
  { path: '/category', name: '分类', icon: 'fa-regular fa-compass' },
  { path: '/cart', name: '购物车', icon: 'fa-solid fa-bag-shopping', badge: cartStore.totalCount },
  { path: '/profile', name: '我的', icon: 'fa-regular fa-user' }
])

function isActive(path) {
  if (path === '/') return route.path === '/'
  return route.path.startsWith(path)
}
</script>
