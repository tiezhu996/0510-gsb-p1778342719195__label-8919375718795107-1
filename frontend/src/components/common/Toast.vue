<template>
  <Transition name="fade">
    <div v-if="toastState.visible" class="fixed top-20 left-1/2 -translate-x-1/2 z-50">
      <div :class="[
        'px-6 py-3 rounded-2xl shadow-lg text-white flex items-center gap-2',
        typeClass
      ]">
        <i :class="iconClass"></i>
        <span>{{ toastState.message }}</span>
      </div>
    </div>
  </Transition>
</template>

<script setup>
import { computed } from 'vue'
import { useToast } from '@/utils/toast'

const { toastState } = useToast()

const typeClass = computed(() => {
  const classes = {
    success: 'bg-primary-500',
    error: 'bg-red-500',
    warning: 'bg-amber-500',
    info: 'bg-slate-700'
  }
  return classes[toastState.value.type] || classes.info
})

const iconClass = computed(() => {
  const icons = {
    success: 'fa-solid fa-circle-check',
    error: 'fa-solid fa-circle-xmark',
    warning: 'fa-solid fa-triangle-exclamation',
    info: 'fa-solid fa-circle-info'
  }
  return icons[toastState.value.type] || icons.info
})
</script>
