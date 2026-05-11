<template>
  <div class="min-h-screen bg-slate-50">
    <NavBar title="登录" :show-back="true" :show-cart="false" />

    <main class="flex items-center justify-center px-4 py-10">
      <div class="w-full max-w-md bg-white rounded-3xl shadow-xl p-8">
        <div class="text-center">
          <div class="mx-auto h-12 w-12 rounded-2xl bg-primary-500 text-white flex items-center justify-center">
            <i class="fa-solid fa-leaf"></i>
          </div>
          <h1 class="mt-4 text-xl font-semibold">水果在线购</h1>
          <p class="text-sm text-slate-500 mt-1">欢迎回来</p>
        </div>

        <form class="mt-8 space-y-4" @submit.prevent="handleLogin">
          <div>
            <label class="text-sm text-slate-600">手机号</label>
            <div class="mt-2 flex items-center gap-3 rounded-2xl border border-slate-200 px-4 py-3 focus-within:border-primary-500">
              <i class="fa-solid fa-mobile-screen text-slate-400"></i>
              <input
                v-model="form.phone"
                class="w-full outline-none text-sm"
                placeholder="请输入手机号"
                maxlength="11"
              />
            </div>
            <p v-if="errors.phone" class="mt-1 text-xs text-red-500">{{ errors.phone }}</p>
          </div>

          <div>
            <label class="text-sm text-slate-600">密码</label>
            <div class="mt-2 flex items-center gap-3 rounded-2xl border border-slate-200 px-4 py-3 focus-within:border-primary-500">
              <i class="fa-solid fa-lock text-slate-400"></i>
              <input
                v-model="form.password"
                class="w-full outline-none text-sm"
                placeholder="请输入密码"
                :type="showPassword ? 'text' : 'password'"
              />
              <i
                :class="showPassword ? 'fa-solid fa-eye' : 'fa-regular fa-eye'"
                class="text-slate-400 cursor-pointer"
                @click="showPassword = !showPassword"
              ></i>
            </div>
            <p v-if="errors.password" class="mt-1 text-xs text-red-500">{{ errors.password }}</p>
          </div>

          <div class="flex items-center justify-between text-sm text-slate-500">
            <label class="flex items-center gap-2 cursor-pointer">
              <input v-model="form.remember" type="checkbox" class="rounded text-primary-500" />
              记住登录（7天内免登录）
            </label>
          </div>

          <button
            type="submit"
            :disabled="submitting"
            class="w-full mt-2 bg-primary-500 text-white rounded-2xl py-3 font-medium hover:bg-primary-600 transition disabled:opacity-50"
          >
            {{ submitting ? '登录中...' : '立即登录' }}
          </button>
        </form>

        <p class="text-center text-sm text-slate-500 mt-6">
          还没有账号？
          <router-link to="/register" class="text-primary-500">立即注册</router-link>
        </p>
      </div>
    </main>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import NavBar from '@/components/common/NavBar.vue'
import { useUserStore } from '@/stores/user'
import { validatePhone, validatePassword } from '@/utils/validate'
import { showToast } from '@/utils/toast'

const router = useRouter()
const route = useRoute()
const userStore = useUserStore()

const form = reactive({
  phone: '',
  password: '',
  remember: false
})

const errors = reactive({
  phone: '',
  password: ''
})

const showPassword = ref(false)
const submitting = ref(false)

function validate() {
  errors.phone = validatePhone(form.phone)
  errors.password = validatePassword(form.password)
  return !errors.phone && !errors.password
}

async function handleLogin() {
  if (!validate()) return

  submitting.value = true
  try {
    await userStore.login(form)
    showToast('登录成功', 'success')
    const redirect = route.query.redirect || '/'
    router.push(redirect)
  } catch {
    // Error handled by interceptor
  } finally {
    submitting.value = false
  }
}
</script>
