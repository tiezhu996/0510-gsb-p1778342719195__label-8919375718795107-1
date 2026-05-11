<template>
  <div class="fixed inset-0 z-50 flex items-end justify-center" v-if="visible">
    <div class="absolute inset-0 bg-black/50" @click="handleClose"></div>
    <div class="relative bg-white rounded-t-3xl w-full max-w-lg max-h-[85vh] overflow-y-auto safe-area-pb">
      <div class="sticky top-0 bg-white px-6 py-4 border-b border-slate-100 flex items-center justify-between rounded-t-3xl z-10">
        <h3 class="text-lg font-semibold">商品评价</h3>
        <button @click="handleClose" class="text-slate-400 hover:text-slate-600">
          <i class="fa-solid fa-xmark text-xl"></i>
        </button>
      </div>

      <div class="px-6 py-4 space-y-6">
        <div v-for="item in items" :key="item.id" class="border-b border-slate-100 pb-6 last:border-b-0 last:pb-0">
          <div class="flex gap-3 mb-3">
            <img :src="item.image" :alt="item.name" class="w-14 h-14 rounded-xl object-cover" />
            <div class="flex-1 min-w-0">
              <p class="text-sm font-medium line-clamp-1">{{ item.name }}</p>
              <p v-if="item.spec" class="text-xs text-slate-400 mt-1">{{ item.spec }}</p>
            </div>
          </div>

          <div class="flex items-center gap-2 mb-3">
            <span class="text-sm text-slate-500">评分</span>
            <div class="flex gap-1">
              <button
                v-for="star in 5"
                :key="star"
                @click="setRating(item.id, star)"
                class="text-xl transition"
                :class="getRating(item.id) >= star ? 'text-amber-400' : 'text-slate-200'"
              >
                <i class="fa-solid fa-star"></i>
              </button>
            </div>
            <span class="text-sm text-slate-400">{{ getRatingText(getRating(item.id)) }}</span>
          </div>

          <textarea
            :value="getContent(item.id)"
            @input="setContent(item.id, $event.target.value)"
            placeholder="分享您的购买体验吧..."
            class="w-full h-20 px-3 py-2 rounded-xl border border-slate-200 text-sm resize-none focus:outline-none focus:border-primary-500 transition"
            maxlength="500"
          ></textarea>
        </div>
      </div>

      <div class="sticky bottom-0 bg-white px-6 py-4 border-t border-slate-100">
        <button
          @click="handleSubmit"
          :disabled="submitting || !allRated"
          :class="[
            'w-full py-3 rounded-2xl text-white font-medium transition',
            submitting || !allRated
              ? 'bg-slate-300 cursor-not-allowed'
              : 'bg-primary-500 hover:bg-primary-600'
          ]"
        >
          {{ submitting ? '提交中...' : '提交评价' }}
        </button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { showToast } from '@/utils/toast'

const props = defineProps({
  visible: Boolean,
  items: { type: Array, default: () => [] }
})

const emit = defineEmits(['update:visible', 'submit'])

const submitting = ref(false)
const reviewData = ref({})

function getRating(itemId) {
  return reviewData.value[itemId]?.rating || 0
}

function setRating(itemId, rating) {
  if (!reviewData.value[itemId]) {
    reviewData.value[itemId] = { rating: 0, content: '' }
  }
  reviewData.value[itemId].rating = rating
}

function getContent(itemId) {
  return reviewData.value[itemId]?.content || ''
}

function setContent(itemId, content) {
  if (!reviewData.value[itemId]) {
    reviewData.value[itemId] = { rating: 0, content: '' }
  }
  reviewData.value[itemId].content = content
}

function getRatingText(rating) {
  const map = { 0: '', 1: '很差', 2: '较差', 3: '一般', 4: '满意', 5: '非常满意' }
  return map[rating] || ''
}

const allRated = computed(() => {
  return props.items.every(item => getRating(item.id) > 0)
})

function handleClose() {
  emit('update:visible', false)
}

async function handleSubmit() {
  if (!allRated.value || submitting.value) return
  submitting.value = true
  try {
    const reviews = props.items.map(item => ({
      orderItemId: item.id,
      rating: getRating(item.id),
      content: getContent(item.id)
    }))
    emit('submit', reviews)
  } finally {
    submitting.value = false
  }
}
</script>

<style scoped>
.safe-area-pb {
  padding-bottom: max(0.75rem, env(safe-area-inset-bottom));
}
</style>
