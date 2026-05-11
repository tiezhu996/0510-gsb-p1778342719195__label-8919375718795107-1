<template>
  <div class="min-h-screen bg-slate-50">
    <header class="bg-white shadow-sm sticky top-0 z-10">
      <div class="max-w-5xl mx-auto px-4 sm:px-6 py-4 flex items-center gap-4">
        <button @click="router.back()" class="text-slate-600">
          <i class="fa-solid fa-arrow-left"></i>
        </button>
        <h1 class="text-lg font-semibold">关于我们</h1>
      </div>
    </header>

    <main class="max-w-5xl mx-auto px-4 sm:px-6 py-6">
      <div class="bg-white rounded-3xl p-6 shadow-sm text-center">
        <div class="mb-6">
          <div class="w-20 h-20 mx-auto bg-primary-500 rounded-2xl flex items-center justify-center">
            <i class="fa-solid fa-apple-whole text-4xl text-white"></i>
          </div>
        </div>

        <h2 class="text-2xl font-bold text-slate-800 mb-2">水果在线购</h2>
        <p class="text-slate-500 mb-6">新鲜水果，产地直供</p>

        <div class="space-y-4 text-left max-w-md mx-auto">
          <section>
            <h3 class="font-medium text-slate-800 mb-2">我们的使命</h3>
            <p class="text-slate-600 text-sm">为每一位用户提供新鲜、优质、实惠的水果，让健康生活触手可及。</p>
          </section>

          <section>
            <h3 class="font-medium text-slate-800 mb-2">品质承诺</h3>
            <ul class="text-slate-600 text-sm space-y-1">
              <li>• 产地直采，保证新鲜</li>
              <li>• 严格质检，品质保证</li>
              <li>• 1小时送达，快速便捷</li>
              <li>• 7天无理由退换</li>
            </ul>
          </section>

          <section>
            <h3 class="font-medium text-slate-800 mb-2">联系我们</h3>
            <p class="text-slate-600 text-sm">客服电话：400-123-4567</p>
            <p class="text-slate-600 text-sm">工作时间：9:00-21:00</p>
            <p class="text-slate-600 text-sm">邮箱：service@fruitshop.com</p>
          </section>
        </div>

        <div class="mt-8 pt-6 border-t border-slate-100">
          <p class="text-slate-400 text-xs">版本 1.0.0</p>
          <p class="text-slate-400 text-xs mt-1">© 2026 水果在线购 版权所有</p>
        </div>
      </div>
    </main>
  </div>
</template>

<script setup>
import { useRouter } from 'vue-router'

const router = useRouter()
</script>
