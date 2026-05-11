<template>
  <div class="min-h-screen bg-slate-50">
    <main class="max-w-5xl mx-auto px-4 sm:px-6 py-6">
      <header>
        <h1 class="text-lg font-semibold">个人中心</h1>
      </header>

      <!-- User Info -->
      <section class="mt-6 bg-white rounded-3xl p-6 shadow-sm">
        <div class="flex items-center gap-4">
          <img
            class="h-16 w-16 rounded-2xl object-cover"
            :src="userInfo?.avatar || 'https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?w=100&auto=format&fit=crop&q=80'"
            alt="头像"
          />
          <div class="flex-1">
            <p class="font-semibold">{{ userInfo?.nickname || '未登录' }}</p>
            <p class="text-sm text-slate-400">{{ userInfo?.phone || '请先登录' }}</p>
          </div>
          <button @click="showEditModal = true" class="text-primary-500 text-sm">编辑</button>
        </div>
      </section>

      <!-- Order Stats -->
      <section class="mt-6 bg-white rounded-3xl p-6 shadow-sm">
        <div class="flex items-center justify-between">
          <h2 class="text-base font-semibold">我的订单</h2>
          <router-link to="/order/list" class="text-sm text-primary-500">全部订单</router-link>
        </div>
        <div class="mt-4 grid grid-cols-2 sm:grid-cols-4 gap-3 text-center">
          <router-link to="/order/list?status=0" class="rounded-2xl bg-slate-50 p-4 hover:bg-slate-100 transition">
            <i class="fa-solid fa-credit-card text-primary-500"></i>
            <p class="mt-2 text-xs text-slate-500">待付款 ({{ orderCount.unpaid }})</p>
          </router-link>
          <router-link to="/order/list?status=1" class="rounded-2xl bg-slate-50 p-4 hover:bg-slate-100 transition">
            <i class="fa-solid fa-box text-primary-500"></i>
            <p class="mt-2 text-xs text-slate-500">待发货 ({{ orderCount.unshipped }})</p>
          </router-link>
          <router-link to="/order/list?status=2" class="rounded-2xl bg-slate-50 p-4 hover:bg-slate-100 transition">
            <i class="fa-solid fa-truck text-primary-500"></i>
            <p class="mt-2 text-xs text-slate-500">待收货 ({{ orderCount.unreceived }})</p>
          </router-link>
          <router-link to="/order/list?status=3" class="rounded-2xl bg-slate-50 p-4 hover:bg-slate-100 transition">
            <i class="fa-solid fa-circle-check text-primary-500"></i>
            <p class="mt-2 text-xs text-slate-500">已完成 ({{ orderCount.completed }})</p>
          </router-link>
        </div>
      </section>

      <!-- Menu -->
      <section class="mt-6 space-y-3">
        <router-link to="/address" class="bg-white rounded-2xl p-4 shadow-sm flex items-center justify-between">
          <span class="text-sm">收货地址</span>
          <i class="fa-solid fa-chevron-right text-slate-400"></i>
        </router-link>
        <router-link to="/password" class="bg-white rounded-2xl p-4 shadow-sm flex items-center justify-between">
          <span class="text-sm">修改密码</span>
          <i class="fa-solid fa-chevron-right text-slate-400"></i>
        </router-link>
        <div @click="handleShowAgreement" class="bg-white rounded-2xl p-4 shadow-sm flex items-center justify-between cursor-pointer">
          <span class="text-sm">用户协议</span>
          <i class="fa-solid fa-chevron-right text-slate-400"></i>
        </div>
        <div @click="handleShowAbout" class="bg-white rounded-2xl p-4 shadow-sm flex items-center justify-between cursor-pointer">
          <span class="text-sm">关于我们</span>
          <i class="fa-solid fa-chevron-right text-slate-400"></i>
        </div>
      </section>

      <button
        @click="handleLogout"
        class="mt-6 w-full py-3 rounded-2xl border border-slate-200 bg-white text-slate-600 hover:bg-slate-50 transition"
      >
        退出登录
      </button>
    </main>

    <TabBar />

    <!-- Edit Nickname Modal -->
    <Transition name="fade">
      <div v-if="showEditModal" class="fixed inset-0 bg-black/50 flex items-center justify-center z-50 p-4">
        <div class="bg-white rounded-3xl p-6 w-full max-w-md">
          <h3 class="text-lg font-semibold">修改昵称</h3>
          <form class="mt-4 space-y-4" @submit.prevent="handleSaveNickname">
            <div>
              <label class="text-sm text-slate-600">新昵称</label>
              <input
                v-model="nickname"
                class="w-full mt-2 px-4 py-3 rounded-2xl border border-slate-200 outline-none focus:border-primary-500"
                placeholder="请输入新昵称"
                maxlength="20"
              />
            </div>
            <div class="flex gap-3">
              <button
                type="button"
                @click="showEditModal = false"
                class="flex-1 py-3 rounded-2xl border border-slate-200 text-slate-600 hover:bg-slate-50 transition"
              >
                取消
              </button>
              <button
                type="submit"
                class="flex-1 py-3 rounded-2xl bg-primary-500 text-white hover:bg-primary-600 transition"
              >
                保存
              </button>
            </div>
          </form>
        </div>
      </div>
    </Transition>
  </div>
</template>

<script setup>
import { computed, ref } from 'vue'
import { useRouter } from 'vue-router'
import TabBar from '@/components/common/TabBar.vue'
import { useUserStore } from '@/stores/user'
import { showToast } from '@/utils/toast'

const router = useRouter()
const userStore = useUserStore()

const userInfo = computed(() => userStore.userInfo)
const orderCount = computed(() => userInfo.value?.orderCount || { unpaid: 0, unshipped: 0, unreceived: 0, completed: 0 })

const showEditModal = ref(false)
const nickname = ref(userInfo.value?.nickname || '')

async function handleSaveNickname() {
  if (!nickname.value.trim()) {
    showToast('请输入昵称', 'error')
    return
  }
  if (nickname.value.length < 2) {
    showToast('昵称至少2个字符', 'error')
    return
  }
  try {
    await userStore.updateNickname(nickname.value.trim())
    showToast('保存成功', 'success')
    showEditModal.value = false
  } catch (error) {
    showToast(error.message || '保存失败', 'error')
  }
}

function handleLogout() {
  userStore.logout()
  showToast('已退出登录', 'success')
  router.push('/')
}

function handleShowAgreement() {
  router.push('/agreement')
}

function handleShowAbout() {
  router.push('/about')
}
</script>
