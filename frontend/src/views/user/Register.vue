<template>
  <div class="min-h-screen bg-slate-50">
    <NavBar title="注册" :show-back="true" :show-cart="false" />

    <main class="flex items-center justify-center px-4 py-10">
      <div class="w-full max-w-md bg-white rounded-3xl shadow-xl p-8">
        <div class="text-center">
          <div class="mx-auto h-12 w-12 rounded-2xl bg-primary-500 text-white flex items-center justify-center">
            <i class="fa-solid fa-leaf"></i>
          </div>
          <h1 class="mt-4 text-xl font-semibold">水果在线购</h1>
          <p class="text-sm text-slate-500 mt-1">创建新账号</p>
        </div>

        <form class="mt-8 space-y-4" @submit.prevent="handleRegister">
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

          <div>
            <label class="text-sm text-slate-600">确认密码</label>
            <div class="mt-2 flex items-center gap-3 rounded-2xl border border-slate-200 px-4 py-3 focus-within:border-primary-500">
              <i class="fa-solid fa-lock text-slate-400"></i>
              <input
                v-model="form.confirmPassword"
                class="w-full outline-none text-sm"
                placeholder="请再次输入密码"
                :type="showConfirmPassword ? 'text' : 'password'"
              />
              <i
                :class="showConfirmPassword ? 'fa-solid fa-eye' : 'fa-regular fa-eye'"
                class="text-slate-400 cursor-pointer"
                @click="showConfirmPassword = !showConfirmPassword"
              ></i>
            </div>
            <p v-if="errors.confirmPassword" class="mt-1 text-xs text-red-500">{{ errors.confirmPassword }}</p>
          </div>

          <div>
            <label class="text-sm text-slate-600">昵称</label>
            <div class="mt-2 flex items-center gap-3 rounded-2xl border border-slate-200 px-4 py-3 focus-within:border-primary-500">
              <i class="fa-solid fa-user text-slate-400"></i>
              <input
                v-model="form.nickname"
                class="w-full outline-none text-sm"
                placeholder="请输入昵称"
                maxlength="20"
              />
            </div>
            <p v-if="errors.nickname" class="mt-1 text-xs text-red-500">{{ errors.nickname }}</p>
          </div>

          <label class="flex items-center gap-2 text-sm text-slate-500 cursor-pointer">
            <input v-model="form.agree" type="checkbox" class="rounded text-primary-500" />
            我已阅读并同意《用户协议》
          </label>
          <p v-if="errors.agree" class="mt-1 text-xs text-red-500">{{ errors.agree }}</p>

          <button
            type="submit"
            :disabled="submitting"
            class="w-full bg-primary-500 text-white rounded-2xl py-3 font-medium hover:bg-primary-600 transition disabled:opacity-50"
          >
            {{ submitting ? '注册中...' : '立即注册' }}
          </button>
        </form>

        <p class="text-center text-sm text-slate-500 mt-6">
          已有账号？
          <router-link to="/login" class="text-primary-500">立即登录</router-link>
        </p>
      </div>
    </main>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import NavBar from '@/components/common/NavBar.vue'
import { useUserStore } from '@/stores/user'
import { validatePhone, validatePassword, validateNickname, validateConfirmPassword } from '@/utils/validate'
import { showToast } from '@/utils/toast'

const router = useRouter()
const userStore = useUserStore()

const form = reactive({
  phone: '',
  password: '',
  confirmPassword: '',
  nickname: '',
  agree: false
})

const errors = reactive({
  phone: '',
  password: '',
  confirmPassword: '',
  nickname: '',
  agree: ''
})

const showPassword = ref(false)
const showConfirmPassword = ref(false)
const submitting = ref(false)

function validate() {
  errors.phone = validatePhone(form.phone)
  errors.password = validatePassword(form.password)
  errors.confirmPassword = validateConfirmPassword(form.password, form.confirmPassword)
  errors.nickname = validateNickname(form.nickname)
  errors.agree = form.agree ? '' : '请同意用户协议'
  return !errors.phone && !errors.password && !errors.confirmPassword && !errors.nickname && !errors.agree
}

async function handleRegister() {
  if (!validate()) return

  submitting.value = true
  try {
    await userStore.register(form)
    showToast('注册成功', 'success')
    router.push('/')
  } catch {
    // Error handled by interceptor
  } finally {
    submitting.value = false
  }
}
</script>
