<template>
  <div class="min-h-screen bg-slate-50">
    <header class="bg-white shadow-sm sticky top-0 z-10">
      <div class="max-w-5xl mx-auto px-4 sm:px-6 py-4 flex items-center gap-4">
        <button @click="router.back()" class="text-slate-600">
          <i class="fa-solid fa-arrow-left"></i>
        </button>
        <h1 class="text-lg font-semibold">用户协议</h1>
      </div>
    </header>

    <main class="max-w-5xl mx-auto px-4 sm:px-6 py-6">
      <div class="bg-white rounded-3xl p-6 shadow-sm">
        <div class="prose prose-slate max-w-none">
          <h2 class="text-xl font-semibold mb-4">水果在线购用户服务协议</h2>

          <section class="mb-6">
            <h3 class="text-lg font-medium mb-2">1. 服务条款的接受</h3>
            <p class="text-slate-600">欢迎使用水果在线购服务。通过访问或使用本服务，您同意遵守本用户协议的所有条款和条件。</p>
          </section>

          <section class="mb-6">
            <h3 class="text-lg font-medium mb-2">2. 账户注册</h3>
            <p class="text-slate-600">您在注册时必须提供真实、准确、完整的个人信息。您应对您的账户和密码的安全负责，并对您账户下的所有活动承担责任。</p>
          </section>

          <section class="mb-6">
            <h3 class="text-lg font-medium mb-2">3. 用户行为规范</h3>
            <p class="text-slate-600">在使用本服务时，您同意不会：</p>
            <ul class="list-disc list-inside text-slate-600 mt-2">
              <li>违反任何适用的法律法规</li>
              <li>侵犯他人的知识产权或其他权利</li>
              <li>发布虚假、误导性或欺诈性信息</li>
              <li>干扰或破坏本服务的正常运行</li>
            </ul>
          </section>

          <section class="mb-6">
            <h3 class="text-lg font-medium mb-2">4. 商品信息</h3>
            <p class="text-slate-600">我们尽力确保商品信息的准确性，但商品价格、规格等信息可能随时变更。如有变更，恕不另行通知。</p>
          </section>

          <section class="mb-6">
            <h3 class="text-lg font-medium mb-2">5. 订单与支付</h3>
            <p class="text-slate-600">订单确认后，我们将尽快安排配送。配送时间可能因天气、交通等因素而有所延迟。支付完成后，我们将向您发送订单确认信息。</p>
          </section>

          <section class="mb-6">
            <h3 class="text-lg font-medium mb-2">6. 退换货政策</h3>
            <p class="text-slate-600">如商品存在质量问题，您可以在收到商品后7天内申请退换货。退换货运费由我们承担。</p>
          </section>

          <section class="mb-6">
            <h3 class="text-lg font-medium mb-2">7. 隐私保护</h3>
            <p class="text-slate-600">我们重视您的隐私保护。我们将按照相关法律法规的要求，保护您的个人信息安全。</p>
          </section>

          <section class="mb-6">
            <h3 class="text-lg font-medium mb-2">8. 免责声明</h3>
            <p class="text-slate-600">在法律允许的范围内，我们不对因使用本服务而产生的任何直接或间接损失承担责任。</p>
          </section>

          <section class="mb-6">
            <h3 class="text-lg font-medium mb-2">9. 协议修改</h3>
            <p class="text-slate-600">我们保留随时修改本协议的权利。修改后的协议一经发布即生效。继续使用本服务即表示您接受修改后的协议。</p>
          </section>

          <section>
            <h3 class="text-lg font-medium mb-2">10. 联系我们</h3>
            <p class="text-slate-600">如有任何问题或建议，请通过以下方式联系我们：</p>
            <p class="text-slate-600 mt-2">客服电话：400-123-4567</p>
            <p class="text-slate-600">邮箱：service@fruitshop.com</p>
          </section>
        </div>
      </div>
    </main>
  </div>
</template>

<script setup>
import { useRouter } from 'vue-router'

const router = useRouter()
</script>
