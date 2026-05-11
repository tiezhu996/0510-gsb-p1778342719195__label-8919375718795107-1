<template>
  <div class="min-h-screen bg-slate-50">
    <router-view v-slot="{ Component }">
      <transition name="fade" mode="out-in">
        <component :is="Component" />
      </transition>
    </router-view>
    <Toast />
  </div>
</template>

<script setup>
import Toast from '@/components/common/Toast.vue'
</script>
