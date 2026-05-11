<template>
  <div class="min-h-screen bg-slate-50 pb-24">
    <NavBar title="订单详情" :show-back="true" />

    <main v-if="order" class="max-w-4xl mx-auto px-4 sm:px-6 py-4 space-y-4">
      <!-- Order Status -->
      <section class="bg-gradient-to-r from-primary-500 to-primary-400 rounded-3xl p-6 text-white">
        <div class="flex items-center gap-3">
          <i :class="['fa-solid text-2xl', statusIcon]"></i>
          <div>
            <p class="text-lg font-semibold">{{ statusText }}</p>
            <p class="text-sm text-white/80 mt-1">{{ statusDesc }}</p>
          </div>
        </div>
      </section>

      <!-- Address -->
      <section class="bg-white rounded-3xl p-4 shadow-sm">
        <div class="flex items-start gap-3">
          <i class="fa-solid fa-location-dot text-primary-500 mt-1"></i>
          <div>
            <div class="flex items-center gap-2">
              <span class="font-medium">{{ order.address.name }}</span>
              <span class="text-slate-500">{{ order.address.phone }}</span>
            </div>
            <p class="text-sm text-slate-500 mt-1">
              {{ order.address.province }}{{ order.address.city }}{{ order.address.district }}{{ order.address.detail }}
            </p>
          </div>
        </div>
      </section>

      <!-- Logistics Info (for shipped orders) -->
      <section v-if="order.status === 2 && (order.logisticsCompany || order.logisticsNo)" class="bg-white rounded-3xl p-4 shadow-sm">
        <div class="flex items-start gap-3">
          <i class="fa-solid fa-truck text-primary-500 mt-1"></i>
          <div class="flex-1">
            <h3 class="font-medium text-slate-700 mb-2">物流信息</h3>
            <div class="space-y-1 text-sm">
              <div v-if="order.logisticsCompany" class="flex items-center gap-2">
                <span class="text-slate-500">快递公司:</span>
                <span class="text-slate-700">{{ order.logisticsCompany }}</span>
              </div>
              <div v-if="order.logisticsNo" class="flex items-center gap-2">
                <span class="text-slate-500">运单号:</span>
                <span class="text-slate-700 font-mono">{{ order.logisticsNo }}</span>
              </div>
            </div>
          </div>
        </div>
      </section>

      <!-- Order Items -->
      <section class="bg-white rounded-3xl p-4 shadow-sm">
        <h3 class="font-medium mb-4">商品信息</h3>
        <div class="space-y-3">
          <div
            v-for="item in order.items"
            :key="item.id"
            class="flex gap-3"
          >
            <router-link :to="`/fruit/${item.fruitId}`">
              <img
                :src="item.image"
                :alt="item.name"
                class="w-16 h-16 rounded-xl object-cover"
              />
            </router-link>
            <div class="flex-1 min-w-0">
              <router-link :to="`/fruit/${item.fruitId}`" class="text-sm font-medium line-clamp-1 hover:text-primary-500">
                {{ item.name }}
              </router-link>
              <p v-if="item.spec" class="text-xs text-slate-400 mt-1">{{ item.spec }}</p>
              <div class="flex items-center justify-between mt-2">
                <span class="text-primary-500">¥{{ item.price }}</span>
                <span class="text-slate-400 text-sm">x{{ item.quantity }}</span>
              </div>
            </div>
          </div>
        </div>
      </section>

      <!-- Order Info -->
      <section class="bg-white rounded-3xl p-4 shadow-sm">
        <h3 class="font-medium mb-4">订单信息</h3>
        <div class="space-y-2 text-sm">
          <div class="flex justify-between">
            <span class="text-slate-500">订单编号</span>
            <span class="text-slate-700">{{ order.orderNo }}</span>
          </div>
          <div class="flex justify-between">
            <span class="text-slate-500">下单时间</span>
            <span class="text-slate-700">{{ order.createTime }}</span>
          </div>
          <div v-if="order.payTime" class="flex justify-between">
            <span class="text-slate-500">支付时间</span>
            <span class="text-slate-700">{{ order.payTime }}</span>
          </div>
          <div v-if="order.note" class="flex justify-between">
            <span class="text-slate-500">备注</span>
            <span class="text-slate-700">{{ order.note }}</span>
          </div>
        </div>
      </section>

      <!-- Price Summary -->
      <section class="bg-white rounded-3xl p-4 shadow-sm">
        <div class="space-y-2 text-sm">
          <div class="flex justify-between">
            <span class="text-slate-500">商品金额</span>
            <span>¥{{ order.subtotal }}</span>
          </div>
          <div class="flex justify-between">
            <span class="text-slate-500">运费</span>
            <span>{{ order.shippingFee > 0 ? '¥' + order.shippingFee : '免运费' }}</span>
          </div>
          <div v-if="order.discount > 0" class="flex justify-between">
            <span class="text-slate-500">优惠</span>
            <span class="text-primary-500">-¥{{ order.discount }}</span>
          </div>
          <div class="border-t border-slate-100 pt-2 flex justify-between font-medium">
            <span>实付金额</span>
            <span class="text-primary-500 text-lg">¥{{ order.totalPrice }}</span>
          </div>
        </div>
      </section>
    </main>

    <!-- Bottom Actions -->
    <div v-if="order" class="fixed bottom-0 left-0 right-0 bg-white border-t border-slate-100 px-4 py-3 safe-area-pb">
      <div class="max-w-4xl mx-auto flex justify-end gap-3">
        <button
          v-if="order.status === 0"
          @click="handleCancel"
          class="px-6 py-2 rounded-2xl border border-slate-200 text-slate-600 hover:border-slate-300 transition"
        >
          取消订单
        </button>
        <button
          v-if="order.status === 0"
          @click="handlePay"
          class="px-6 py-2 rounded-2xl bg-primary-500 text-white hover:bg-primary-600 transition"
        >
          立即支付
        </button>
        <button
          v-if="order.status === 2"
          @click="handleReceive"
          class="px-6 py-2 rounded-2xl bg-primary-500 text-white hover:bg-primary-600 transition"
        >
          确认收货
        </button>
        <button
          v-if="order.status === 3"
          @click="handleBuyAgain"
          class="px-6 py-2 rounded-2xl bg-primary-500 text-white hover:bg-primary-600 transition"
        >
          再次购买
        </button>
      </div>
    </div>

    <Loading :visible="loading" />

    <!-- 确认弹窗 -->
    <ConfirmDialog
      v-model:visible="dialogVisible"
      :title="dialogConfig.title"
      :message="dialogConfig.message"
      :type="dialogConfig.type"
      :confirmText="dialogConfig.confirmText"
      @confirm="handleDialogConfirm"
    />
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import NavBar from '@/components/common/NavBar.vue'
import Loading from '@/components/common/Loading.vue'
import ConfirmDialog from '@/components/common/ConfirmDialog.vue'
import { getOrderDetail, payOrder, cancelOrder, receiveOrder } from '@/api/order'
import { showToast } from '@/utils/toast'
import { useCartStore } from '@/stores/cart'

const route = useRoute()
const router = useRouter()
const loading = ref(false)
const order = ref(null)
const cartStore = useCartStore()

// 弹窗状态
const dialogVisible = ref(false)
const dialogConfig = ref({
  title: '',
  message: '',
  type: 'warning',
  confirmText: '确定',
  action: null
})

const statusText = computed(() => {
  const map = { 0: '待付款', 1: '待发货', 2: '待收货', 3: '已完成', 4: '已取消' }
  return map[order.value?.status] || '未知'
})

const statusIcon = computed(() => {
  const map = { 0: 'fa-credit-card', 1: 'fa-box', 2: 'fa-truck', 3: 'fa-circle-check', 4: 'fa-circle-xmark' }
  return map[order.value?.status] || 'fa-circle-question'
})

const statusDesc = computed(() => {
  const map = {
    0: '请尽快完成支付',
    1: '商家正在准备商品',
    2: '商品正在配送中',
    3: '感谢您的购买',
    4: '订单已取消'
  }
  return map[order.value?.status] || ''
})

onMounted(() => {
  fetchDetail()
})

async function fetchDetail() {
  loading.value = true
  try {
    const rawOrder = await getOrderDetail(route.params.id)
    // 转换后端数据格式到前端期望格式
    order.value = {
      ...rawOrder,
      totalPrice: rawOrder.payAmount,
      subtotal: rawOrder.totalAmount,
      shippingFee: rawOrder.freight,
      discount: 0,
      note: rawOrder.remark,
      logisticsCompany: rawOrder.logisticsCompany || null,
      logisticsNo: rawOrder.logisticsNo || null,
      createTime: formatDateTime(rawOrder.createTime),
      payTime: rawOrder.payTime ? formatDateTime(rawOrder.payTime) : null,
      address: {
        name: rawOrder.addressName,
        phone: rawOrder.addressPhone,
        province: '',
        city: '',
        district: '',
        detail: rawOrder.addressDetail
      },
      items: (rawOrder.items || []).map(item => ({
        ...item,
        name: item.fruitName,
        image: item.fruitImage,
        spec: item.specName
      }))
    }
  } catch {
    order.value = {
      id: route.params.id,
      orderNo: '20240115001',
      status: 0,
      createTime: '2024-01-15 10:30:00',
      address: {
        name: '张三',
        phone: '138****8000',
        province: '北京市',
        city: '北京市',
        district: '朝阳区',
        detail: '建国路88号SOHO现代城'
      },
      items: [
        { id: 1, fruitId: 1, name: '智利车厘子', price: 99.9, quantity: 2, image: 'https://images.unsplash.com/photo-1528821128474-27f963b062bf?w=200&auto=format&fit=crop&q=80' }
      ],
      subtotal: 199.8,
      shippingFee: 0,
      discount: 0,
      totalPrice: 199.8
    }
  } finally {
    loading.value = false
  }
}

function formatDateTime(dateStr) {
  if (!dateStr) return null
  const date = new Date(dateStr)
  return date.toLocaleString('zh-CN', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit',
    hour: '2-digit',
    minute: '2-digit'
  }).replace(/\//g, '-')
}

async function handlePay() {
  try {
    await payOrder(order.value.id)
    showToast('支付成功', 'success')
    fetchDetail()
  } catch {
    // Error handled by interceptor
  }
}

async function handleCancel() {
  dialogConfig.value = {
    title: '取消订单',
    message: '确定要取消该订单吗？取消后无法恢复。',
    type: 'warning',
    confirmText: '取消订单',
    action: 'cancel'
  }
  dialogVisible.value = true
}

async function handleReceive() {
  dialogConfig.value = {
    title: '确认收货',
    message: '请确认您已收到商品，确认后订单将完成。',
    type: 'info',
    confirmText: '确认收货',
    action: 'receive'
  }
  dialogVisible.value = true
}

async function handleDialogConfirm() {
  const { action } = dialogConfig.value
  try {
    if (action === 'cancel') {
      await cancelOrder(order.value.id)
      showToast('已取消', 'success')
      router.push('/order/list')
    } else if (action === 'receive') {
      await receiveOrder(order.value.id)
      showToast('已确认收货', 'success')
      fetchDetail()
    }
  } catch {
    // Error handled by interceptor
  }
}

async function handleBuyAgain() {
  try {
    // 将订单中的所有商品重新加入购物车
    for (const item of order.value.items) {
      // 获取规格ID：这里需要从商品数据中获取，暂时使用 fruitId 作为默认值
      // 实际场景中需要根据商品规格匹配对应的 specId
      await cartStore.addToCart(item.fruitId, 1, item.quantity)
    }
    showToast('已加入购物车', 'success')
    router.push('/cart')
  } catch (error) {
    // 如果部分商品已下架或库存不足，显示提示
    console.error('再次购买失败:', error)
    showToast('部分商品已下架或库存不足', 'error')
  }
}
</script>

<style scoped>
.safe-area-pb {
  padding-bottom: max(0.75rem, env(safe-area-inset-bottom));
}
</style>
