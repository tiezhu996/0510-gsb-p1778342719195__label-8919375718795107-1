<template>
  <div class="min-h-screen bg-slate-50">
    <NavBar title="修改密码" :show-back="true" :show-cart="false" />

    <main class="max-w-md mx-auto px-4 py-6">
      <div class="bg-white rounded-3xl p-6 shadow-sm">
        <form class="space-y-4" @submit.prevent="handleSubmit">
          <div>
            <label class="text-sm text-slate-600">原密码</label>
            <div class="mt-2 flex items-center gap-3 rounded-2xl border border-slate-200 px-4 py-3 focus-within:border-primary-500">
              <i class="fa-solid fa-lock text-slate-400"></i>
              <input
                v-model="form.oldPassword"
                class="w-full outline-none text-sm"
                placeholder="请输入原密码"
                type="password"
              />
            </div>
          </div>

          <div>
            <label class="text-sm text-slate-600">新密码</label>
            <div class="mt-2 flex items-center gap-3 rounded-2xl border border-slate-200 px-4 py-3 focus-within:border-primary-500">
              <i class="fa-solid fa-lock text-slate-400"></i>
              <input
                v-model="form.newPassword"
                class="w-full outline-none text-sm"
                placeholder="请输入新密码"
                type="password"
              />
            </div>
          </div>

          <div>
            <label class="text-sm text-slate-600">确认新密码</label>
            <div class="mt-2 flex items-center gap-3 rounded-2xl border border-slate-200 px-4 py-3 focus-within:border-primary-500">
              <i class="fa-solid fa-lock text-slate-400"></i>
              <input
                v-model="form.confirmPassword"
                class="w-full outline-none text-sm"
                placeholder="请再次输入新密码"
                type="password"
              />
            </div>
          </div>

          <button
            type="submit"
            :disabled="submitting"
            class="w-full py-3 rounded-2xl bg-primary-500 text-white hover:bg-primary-600 transition disabled:opacity-50"
          >
            {{ submitting ? '提交中...' : '确认修改' }}
          </button>
        </form>
      </div>
    </main>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import NavBar from '@/components/common/NavBar.vue'
import { updatePassword } from '@/api/user'
import { showToast } from '@/utils/toast'

const router = useRouter()
const submitting = ref(false)

const form = reactive({
  oldPassword: '',
  newPassword: '',
  confirmPassword: ''
})

async function handleSubmit() {
  if (!form.oldPassword) {
    showToast('请输入原密码', 'warning')
    return
  }
  if (!form.newPassword || form.newPassword.length < 6) {
    showToast('新密码至少6位', 'warning')
    return
  }
  if (form.newPassword !== form.confirmPassword) {
    showToast('两次密码不一致', 'warning')
    return
  }

  submitting.value = true
  try {
    await updatePassword(form)
    showToast('密码修改成功', 'success')
    router.back()
  } catch {
    // Error handled by interceptor
  } finally {
    submitting.value = false
  }
}
</script>
