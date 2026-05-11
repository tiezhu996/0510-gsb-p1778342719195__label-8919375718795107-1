<template>
  <div class="min-h-screen bg-slate-50">
    <NavBar title="我的订单" :show-back="true" />

    <main class="max-w-4xl mx-auto px-4 sm:px-6 py-4">
      <!-- Status Tabs -->
      <div class="bg-white rounded-2xl p-2 shadow-sm flex gap-1 mb-4">
        <button
          v-for="tab in tabs"
          :key="tab.value"
          @click="currentTab = tab.value"
          :class="[
            'flex-1 py-2 rounded-xl text-sm font-medium transition',
            currentTab === tab.value
              ? 'bg-primary-500 text-white'
              : 'text-slate-500 hover:bg-slate-50'
          ]"
        >
          {{ tab.label }}
        </button>
      </div>

      <!-- Order List -->
      <div class="space-y-4">
        <div
          v-for="order in filteredOrders"
          :key="order.id"
          class="bg-white rounded-3xl shadow-sm overflow-hidden"
        >
          <!-- Order Header -->
          <div class="px-4 py-3 border-b border-slate-100 flex items-center justify-between">
            <span class="text-sm text-slate-500">订单号：{{ order.orderNo }}</span>
            <span :class="['text-sm font-medium', statusColor(order.status)]">
              {{ statusText(order.status) }}
            </span>
            <span
              v-if="order.status === 3 && order.reviewed"
              class="text-xs text-amber-500 bg-amber-50 px-2 py-0.5 rounded-full ml-2"
            >
              已评价
            </span>
            <span
              v-if="order.status === 3 && !order.reviewed && order.reviewedItemIds && order.reviewedItemIds.length > 0"
              class="text-xs text-primary-500 bg-primary-50 px-2 py-0.5 rounded-full ml-2"
            >
              部分评价
            </span>
          </div>

          <!-- Order Items -->
          <router-link :to="`/order/detail/${order.id}`" class="block p-4">
            <div
              v-for="item in order.items.slice(0, 2)"
              :key="item.id"
              class="flex gap-3 mb-3 last:mb-0"
            >
              <img
                :src="item.image"
                :alt="item.name"
                class="w-16 h-16 rounded-xl object-cover"
              />
              <div class="flex-1 min-w-0">
                <p class="text-sm font-medium line-clamp-1">{{ item.name }}</p>
                <p v-if="item.spec" class="text-xs text-slate-400 mt-1">{{ item.spec }}</p>
                <div class="flex items-center justify-between mt-2">
                  <span class="text-primary-500">¥{{ item.price }}</span>
                  <span class="text-slate-400 text-sm">x{{ item.quantity }}</span>
                </div>
              </div>
            </div>
            <p v-if="order.items.length > 2" class="text-xs text-slate-400 text-center">
              共 {{ order.items.length }} 件商品
            </p>
          </router-link>

          <!-- Order Footer -->
          <div class="px-4 py-3 border-t border-slate-100 flex items-center justify-between">
            <span class="text-sm">
              实付：<span class="text-primary-500 font-semibold">¥{{ order.totalPrice }}</span>
            </span>
            <div class="flex gap-2">
              <button
                v-if="order.status === 0"
                @click.prevent="handlePay(order.id)"
                class="px-4 py-1.5 rounded-full bg-primary-500 text-white text-sm hover:bg-primary-600 transition"
              >
                去支付
              </button>
              <button
                v-if="order.status === 0"
                @click.prevent="handleCancel(order.id)"
                class="px-4 py-1.5 rounded-full border border-slate-200 text-slate-500 text-sm hover:border-slate-300 transition"
              >
                取消
              </button>
              <button
                v-if="order.status === 2"
                @click.prevent="handleReceive(order.id)"
                class="px-4 py-1.5 rounded-full bg-primary-500 text-white text-sm hover:bg-primary-600 transition"
              >
                确认收货
              </button>
              <button
                v-if="order.status === 3 && !order.reviewed"
                @click.prevent="$router.push(`/order/detail/${order.id}`)"
                class="px-4 py-1.5 rounded-full bg-primary-500 text-white text-sm hover:bg-primary-600 transition"
              >
                待评价
              </button>
              <button
                v-if="order.status === 3 || order.status === 4"
                @click.prevent="handleDelete(order.id)"
                class="px-4 py-1.5 rounded-full border border-slate-200 text-slate-500 text-sm hover:border-red-300 hover:text-red-500 transition"
              >
                删除
              </button>
            </div>
          </div>
        </div>
      </div>

      <Empty v-if="filteredOrders.length === 0 && !loading" text="暂无订单" />
    </main>

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
import { ref, computed, onMounted, watch } from 'vue'
import { useRoute } from 'vue-router'
import NavBar from '@/components/common/NavBar.vue'
import Loading from '@/components/common/Loading.vue'
import Empty from '@/components/common/Empty.vue'
import ConfirmDialog from '@/components/common/ConfirmDialog.vue'
import { getOrderList, payOrder, cancelOrder, receiveOrder, deleteOrder } from '@/api/order'
import { showToast } from '@/utils/toast'

const route = useRoute()
const loading = ref(false)
const orders = ref([])
const currentTab = ref('all')

// 弹窗状态
const dialogVisible = ref(false)
const dialogConfig = ref({
  title: '',
  message: '',
  type: 'warning',
  confirmText: '确定',
  action: null,
  orderId: null
})

const tabs = [
  { label: '全部', value: 'all' },
  { label: '待付款', value: '0' },
  { label: '待发货', value: '1' },
  { label: '待收货', value: '2' },
  { label: '已完成', value: '3' }
]

const filteredOrders = computed(() => {
  if (currentTab.value === 'all') return orders.value
  return orders.value.filter(o => o.status === Number(currentTab.value))
})

onMounted(() => {
  if (route.query.status !== undefined) {
    currentTab.value = route.query.status
  }
  fetchOrders()
})

watch(currentTab, () => {
  fetchOrders()
})

async function fetchOrders() {
  loading.value = true
  try {
    const rawOrders = await getOrderList({ status: currentTab.value === 'all' ? null : Number(currentTab.value) })
    // 转换后端数据格式到前端期望格式
    orders.value = rawOrders.map(order => ({
      ...order,
      totalPrice: order.payAmount,
      reviewed: order.reviewed || false,
      reviewedItemIds: order.reviewedItemIds || [],
      items: (order.items || []).map(item => ({
        ...item,
        name: item.fruitName,
        image: item.fruitImage,
        spec: item.specName
      }))
    }))
  } catch {
    orders.value = [
      {
        id: 1,
        orderNo: '20240115001',
        status: 0,
        totalPrice: 199.8,
        items: [
          { id: 1, name: '智利车厘子', price: 99.9, quantity: 2, image: 'https://images.unsplash.com/photo-1528821128474-27f963b062bf?w=200&auto=format&fit=crop&q=80' }
        ]
      },
      {
        id: 2,
        orderNo: '20240114002',
        status: 2,
        totalPrice: 168,
        items: [
          { id: 2, name: '泰国金枕榴莲', price: 168, quantity: 1, image: 'https://images.unsplash.com/photo-1588165171080-c89acfa5ee83?w=200&auto=format&fit=crop&q=80' }
        ]
      }
    ]
  } finally {
    loading.value = false
  }
}

function statusText(status) {
  const map = { 0: '待付款', 1: '待发货', 2: '待收货', 3: '已完成', 4: '已取消' }
  return map[status] || '未知'
}

function statusColor(status) {
  const map = { 0: 'text-orange-500', 1: 'text-blue-500', 2: 'text-green-500', 3: 'text-slate-500', 4: 'text-slate-400' }
  return map[status] || 'text-slate-500'
}

async function handlePay(id) {
  try {
    await payOrder(id)
    showToast('支付成功', 'success')
    fetchOrders()
  } catch {
    // Error handled by interceptor
  }
}

async function handleCancel(id) {
  dialogConfig.value = {
    title: '取消订单',
    message: '确定要取消该订单吗？取消后无法恢复。',
    type: 'warning',
    confirmText: '取消订单',
    action: 'cancel',
    orderId: id
  }
  dialogVisible.value = true
}

async function handleReceive(id) {
  dialogConfig.value = {
    title: '确认收货',
    message: '请确认您已收到商品，确认后订单将完成。',
    type: 'info',
    confirmText: '确认收货',
    action: 'receive',
    orderId: id
  }
  dialogVisible.value = true
}

async function handleDelete(id) {
  dialogConfig.value = {
    title: '删除订单',
    message: '确定要删除该订单吗？删除后无法恢复。',
    type: 'danger',
    confirmText: '删除',
    action: 'delete',
    orderId: id
  }
  dialogVisible.value = true
}

async function handleDialogConfirm() {
  const { action, orderId } = dialogConfig.value
  try {
    if (action === 'cancel') {
      await cancelOrder(orderId)
      showToast('已取消', 'success')
    } else if (action === 'receive') {
      await receiveOrder(orderId)
      showToast('已确认收货', 'success')
    } else if (action === 'delete') {
      await deleteOrder(orderId)
      showToast('已删除', 'success')
    }
    fetchOrders()
  } catch {
    // Error handled by interceptor
  }
}
</script>
