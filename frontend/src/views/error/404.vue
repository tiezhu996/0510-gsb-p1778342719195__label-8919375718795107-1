<template>
  <div class="min-h-screen bg-slate-50 flex items-center justify-center px-4">
    <div class="text-center">
      <div class="text-8xl font-bold text-primary-500">404</div>
      <h1 class="mt-4 text-2xl font-semibold text-slate-700">页面未找到</h1>
      <p class="mt-2 text-slate-500">抱歉，您访问的页面不存在</p>
      <div class="mt-8 flex items-center justify-center gap-4">
        <router-link
          to="/"
          class="px-6 py-2.5 rounded-2xl bg-primary-500 text-white hover:bg-primary-600 transition"
        >
          返回首页
        </router-link>
        <button
          @click="$router.back()"
          class="px-6 py-2.5 rounded-2xl border border-slate-200 text-slate-600 hover:border-slate-300 transition"
        >
          返回上页
        </button>
      </div>
    </div>
  </div>
</template>

<script setup>
</script>
