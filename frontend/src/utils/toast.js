import { ref } from 'vue'

const toastState = ref({
  visible: false,
  message: '',
  type: 'info',
  duration: 2000
})

let timer = null

export function showToast(message, type = 'info', duration = 2000) {
  if (timer) {
    clearTimeout(timer)
  }

  toastState.value = {
    visible: true,
    message,
    type,
    duration
  }

  timer = setTimeout(() => {
    toastState.value.visible = false
  }, duration)
}

export function hideToast() {
  toastState.value.visible = false
  if (timer) {
    clearTimeout(timer)
  }
}

export function useToast() {
  return {
    toastState,
    showToast,
    hideToast
  }
}
