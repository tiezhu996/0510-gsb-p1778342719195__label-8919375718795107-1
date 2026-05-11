import axios from 'axios'
import { storage } from '@/utils/storage'
import { showToast } from '@/utils/toast'
import router from '@/router'

const request = axios.create({
  baseURL: '/api',
  timeout: 10000,
  headers: {
    'Content-Type': 'application/json'
  }
})

request.interceptors.request.use(
  config => {
    const token = storage.getToken()
    if (token) {
      config.headers.Authorization = `Bearer ${token}`
    }
    return config
  },
  error => Promise.reject(error)
)

request.interceptors.response.use(
  response => {
    const { code, message, data } = response.data
    if (code === 200) {
      return data
    }
    showToast(message || '请求失败', 'error')
    return Promise.reject(new Error(message))
  },
  error => {
    if (error.response?.status === 401) {
      storage.clear()
      router.push({ name: 'Login', query: { redirect: router.currentRoute.value.fullPath } })
      showToast('请先登录', 'warning')
    } else {
      showToast(error.message || '网络错误', 'error')
    }
    return Promise.reject(error)
  }
)

export default request
