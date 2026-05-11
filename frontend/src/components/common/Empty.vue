<template>
  <div class="flex flex-col items-center justify-center py-20">
    <i :class="[iconClass, 'text-6xl text-slate-300']"></i>
    <p class="mt-4 text-slate-400">{{ text }}</p>
    <slot></slot>
  </div>
</template>

<script setup>
import { computed } from 'vue'

const props = defineProps({
  text: {
    type: String,
    default: '暂无数据'
  },
  type: {
    type: String,
    default: 'default'
  }
})

const iconClass = computed(() => {
  const icons = {
    default: 'fa-regular fa-folder-open',
    cart: 'fa-solid fa-cart-shopping',
    order: 'fa-solid fa-receipt',
    search: 'fa-solid fa-magnifying-glass'
  }
  return icons[props.type] || icons.default
})
</script>
