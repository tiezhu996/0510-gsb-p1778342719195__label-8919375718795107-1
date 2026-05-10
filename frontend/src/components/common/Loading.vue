<template>
  <div v-if="visible" class="fixed inset-0 bg-black/30 flex items-center justify-center z-50">
    <div class="bg-white rounded-2xl p-6 flex flex-col items-center">
      <div class="animate-spin w-10 h-10 border-4 border-primary-500 border-t-transparent rounded-full"></div>
      <p class="mt-3 text-slate-600">{{ text }}</p>
    </div>
  </div>
</template>

<script setup>
defineProps({
  visible: {
    type: Boolean,
    default: false
  },
  text: {
    type: String,
    default: '加载中...'
  }
})
</script>
