<template>
  <Teleport to="body">
    <Transition name="fade">
      <div v-if="visible" class="fixed inset-0 z-50 flex items-center justify-center p-4">
        <!-- Overlay -->
        <div class="absolute inset-0 bg-black/50" @click="handleCancel"></div>

        <!-- Dialog -->
        <Transition name="scale">
          <div v-if="visible" class="relative bg-white rounded-3xl shadow-2xl w-full max-w-sm overflow-hidden">
            <!-- Header -->
            <div class="px-6 pt-6 pb-4 text-center">
              <div class="mx-auto w-12 h-12 rounded-full flex items-center justify-center mb-4"
                   :class="iconBgClass">
                <i :class="['text-xl', iconClass]"></i>
              </div>
              <h3 class="text-lg font-semibold text-slate-800">{{ title }}</h3>
              <p v-if="message" class="mt-2 text-sm text-slate-500">{{ message }}</p>
            </div>

            <!-- Actions -->
            <div class="px-6 pb-6 flex gap-3">
              <button
                @click="handleCancel"
                class="flex-1 px-4 py-2.5 rounded-2xl border border-slate-200 text-slate-600 font-medium hover:bg-slate-50 transition"
              >
                {{ cancelText }}
              </button>
              <button
                @click="handleConfirm"
                class="flex-1 px-4 py-2.5 rounded-2xl font-medium transition"
                :class="confirmBtnClass"
              >
                {{ confirmText }}
              </button>
            </div>
          </div>
        </Transition>
      </div>
    </Transition>
  </Teleport>
</template>

<script setup>
import { computed } from 'vue'

const props = defineProps({
  visible: {
    type: Boolean,
    default: false
  },
  title: {
    type: String,
    default: '确认操作'
  },
  message: {
    type: String,
    default: ''
  },
  type: {
    type: String,
    default: 'warning', // warning, danger, info
    validator: (val) => ['warning', 'danger', 'info'].includes(val)
  },
  confirmText: {
    type: String,
    default: '确定'
  },
  cancelText: {
    type: String,
    default: '取消'
  }
})

const emit = defineEmits(['confirm', 'cancel', 'update:visible'])

const iconClass = computed(() => {
  const map = {
    warning: 'fa-solid fa-triangle-exclamation text-amber-500',
    danger: 'fa-solid fa-trash-can text-red-500',
    info: 'fa-solid fa-circle-info text-blue-500'
  }
  return map[props.type]
})

const iconBgClass = computed(() => {
  const map = {
    warning: 'bg-amber-50',
    danger: 'bg-red-50',
    info: 'bg-blue-50'
  }
  return map[props.type]
})

const confirmBtnClass = computed(() => {
  const map = {
    warning: 'bg-amber-500 text-white hover:bg-amber-600',
    danger: 'bg-red-500 text-white hover:bg-red-600',
    info: 'bg-primary-500 text-white hover:bg-primary-600'
  }
  return map[props.type]
})

function handleConfirm() {
  emit('confirm')
  emit('update:visible', false)
}

function handleCancel() {
  emit('cancel')
  emit('update:visible', false)
}
</script>

<style scoped>
.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.2s ease;
}

.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}

.scale-enter-active,
.scale-leave-active {
  transition: all 0.2s ease;
}

.scale-enter-from,
.scale-leave-to {
  opacity: 0;
  transform: scale(0.95);
}
</style>
