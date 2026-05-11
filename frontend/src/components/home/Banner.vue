<template>
  <div class="relative overflow-hidden rounded-3xl">
    <div
      class="flex transition-transform duration-500 ease-out"
      :style="{ transform: `translateX(-${currentIndex * 100}%)` }"
    >
      <div
        v-for="banner in banners"
        :key="banner.id"
        class="w-full flex-shrink-0"
      >
        <router-link :to="banner.link">
          <img
            :src="banner.image"
            :alt="banner.title"
            class="w-full h-48 sm:h-64 lg:h-80 object-cover"
          />
        </router-link>
      </div>
    </div>
    <div class="absolute bottom-4 left-1/2 -translate-x-1/2 flex gap-2">
      <span
        v-for="(_, index) in banners"
        :key="index"
        class="h-2 w-2 rounded-full transition-colors cursor-pointer"
        :class="index === currentIndex ? 'bg-white' : 'bg-white/50'"
        @click="currentIndex = index"
      ></span>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted } from 'vue'

defineProps({
  banners: {
    type: Array,
    default: () => []
  }
})

const currentIndex = ref(0)
let timer = null

onMounted(() => {
  startAutoPlay()
})

onUnmounted(() => {
  stopAutoPlay()
})

function startAutoPlay() {
  timer = setInterval(() => {
    currentIndex.value = (currentIndex.value + 1) % 3
  }, 4000)
}

function stopAutoPlay() {
  if (timer) clearInterval(timer)
}
</script>
