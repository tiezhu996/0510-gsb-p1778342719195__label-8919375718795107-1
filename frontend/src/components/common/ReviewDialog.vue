<template>
  <div v-if="visible" class="fixed inset-0 z-50 flex items-center justify-center p-4">
    <div class="absolute inset-0 bg-black/50" @click="handleClose"></div>
    <div class="relative bg-white rounded-3xl w-full max-w-lg max-h-[90vh] overflow-hidden shadow-xl">
      <div class="p-6 border-b border-slate-100">
        <div class="flex items-center justify-between">
          <h3 class="text-lg font-semibold">评价商品</h3>
          <button @click="handleClose" class="text-slate-400 hover:text-slate-600 transition">
            <i class="fa-solid fa-times text-xl"></i>
          </button>
        </div>
        <p v-if="item" class="text-sm text-slate-500 mt-1">{{ item.name }}</p>
      </div>

      <div class="p-6 space-y-6 max-h-[60vh] overflow-y-auto">
        <div>
          <label class="block text-sm font-medium text-slate-700 mb-3">评分</label>
          <div class="flex items-center gap-2">
            <button
              v-for="star in 5"
              :key="star"
              @click="rating = star"
              class="p-1 transition-transform hover:scale-110"
            >
              <i
                :class="[
                  'fa-solid fa-star text-2xl',
                  star <= rating ? 'text-yellow-400' : 'text-slate-200'
                ]"
              ></i>
            </button>
            <span class="ml-2 text-sm text-slate-500">{{ ratingText }}</span>
          </div>
        </div>

        <div>
          <label class="block text-sm font-medium text-slate-700 mb-3">评价内容</label>
          <textarea
            v-model="content"
            placeholder="分享您的购买体验..."
            class="w-full h-32 px-4 py-3 border border-slate-200 rounded-2xl resize-none focus:outline-none focus:border-primary-400 focus:ring-2 focus:ring-primary-100 transition"
            maxlength="500"
          ></textarea>
          <p class="text-xs text-slate-400 mt-2 text-right">{{ content.length }}/500</p>
        </div>
      </div>

      <div class="p-6 border-t border-slate-100 flex gap-3">
        <button
          @click="handleClose"
          class="flex-1 py-3 rounded-2xl border border-slate-200 text-slate-600 hover:border-slate-300 transition font-medium"
        >
          取消
        </button>
        <button
          @click="handleSubmit"
          :disabled="rating === 0 || submitting"
          class="flex-1 py-3 rounded-2xl bg-primary-500 text-white hover:bg-primary-600 transition font-medium disabled:opacity-50 disabled:cursor-not-allowed"
        >
          <span v-if="submitting">提交中...</span>
          <span v-else>提交评价</span>
        </button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, watch } from 'vue'

const props = defineProps({
  visible: {
    type: Boolean,
    default: false
  },
  item: {
    type: Object,
    default: null
  }
})

const emit = defineEmits(['update:visible', 'submit'])

const rating = ref(0)
const content = ref('')
const submitting = ref(false)

const ratingText = computed(() => {
  const texts = ['', '非常差', '差', '一般', '好', '非常好']
  return texts[rating.value] || '请选择评分'
})

watch(() => props.visible, (val) => {
  if (val) {
    rating.value = 0
    content.value = ''
  }
})

function handleClose() {
  emit('update:visible', false)
}

async function handleSubmit() {
  if (rating.value === 0) return
  submitting.value = true
  try {
    emit('submit', {
      orderItemId: props.item?.id,
      rating: rating.value,
      content: content.value.trim() || null
    })
  } finally {
    submitting.value = false
  }
}
</script>
