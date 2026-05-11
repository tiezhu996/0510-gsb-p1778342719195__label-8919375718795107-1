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
        <div class="space-y-4">
          <div
            v-for="item in order.items"
            :key="item.id"
            class="pb-4 border-b border-slate-100 last:border-0 last:pb-0"
          >
            <div class="flex gap-3">
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
            
            <!-- 评价区域 -->
            <div v-if="order.status === 3" class="mt-3">
              <!-- 已评价内容 -->
              <div v-if="item.review" class="bg-slate-50 rounded-2xl p-3">
                <div class="flex items-center gap-1 mb-2">
                  <span class="text-amber-400">
                    <i v-for="n in 5" :key="n" :class="['fa-star', n <= item.review.rating ? 'fa-solid' : 'fa-regular']"></i>
                  </span>
                  <span class="text-xs text-slate-400 ml-2">
                    {{ formatDateTime(item.review.createTime) }}
                  </span>
                </div>
                <p v-if="item.review.content" class="text-sm text-slate-600">{{ item.review.content }}</p>
              </div>
              <!-- 评价按钮 -->
              <button
                v-else
                @click="openReviewDialog(item)"
                class="w-full py-2 text-sm text-amber-600 bg-amber-50 rounded-xl hover:bg-amber-100 transition"
              >
                <i class="fa-regular fa-star mr-1"></i>评价商品
              </button>
            </div>
          </div>
        </div>
      </section>

      <!-- 评价进度 -->
      <section v-if="order.status === 3" class="bg-white rounded-3xl p-4 shadow-sm">
        <div class="flex items-center justify-between mb-3">
          <h3 class="font-medium">评价进度</h3>
          <span class="text-sm text-slate-500">
            {{ order.reviewedCount || 0 }}/{{ order.totalItemCount || order.items?.length || 0 }} 件商品已评价
          </span>
        </div>
        <div class="w-full bg-slate-100 rounded-full h-2">
          <div 
            class="bg-amber-500 h-2 rounded-full transition-all"
            :style="{ width: ((order.reviewedCount || 0) / (order.totalItemCount || order.items?.length || 1) * 100) + '%' }"
          ></div>
        </div>
        <p v-if="order.reviewStatus === 0" class="text-xs text-slate-400 mt-2">
          <i class="fa-regular fa-star mr-1"></i>还没有评价任何商品
        </p>
        <p v-else-if="order.reviewStatus === 1" class="text-xs text-orange-500 mt-2">
          <i class="fa-solid fa-star-half-stroke mr-1"></i>部分商品已评价，继续评价剩余商品
        </p>
        <p v-else-if="order.reviewStatus === 2" class="text-xs text-amber-500 mt-2">
          <i class="fa-solid fa-star mr-1"></i>所有商品已完成评价，感谢您的反馈！
        </p>
      </section>

      <!-- 评价列表（全部评价展示） -->
      <section v-if="order.reviews && order.reviews.length > 0" class="bg-white rounded-3xl p-4 shadow-sm">
        <h3 class="font-medium mb-4">评价详情</h3>
        <div class="space-y-4">
          <div v-for="review in order.reviews" :key="review.id" class="border-b border-slate-100 pb-4 last:border-0 last:pb-0">
            <div class="flex gap-3">
              <img :src="review.fruitImage" :alt="review.fruitName" class="w-12 h-12 rounded-lg object-cover" />
              <div class="flex-1 min-w-0">
                <p class="text-sm font-medium">{{ review.fruitName }}</p>
                <p v-if="review.specName" class="text-xs text-slate-400">{{ review.specName }}</p>
              </div>
            </div>
            <div class="mt-2">
              <div class="flex items-center gap-1 mb-2">
                <span class="text-amber-400">
                  <i v-for="n in 5" :key="n" :class="['fa-star', n <= review.rating ? 'fa-solid' : 'fa-regular']"></i>
                </span>
                <span class="text-xs text-slate-400 ml-2">
                  {{ formatDateTime(review.createTime) }}
                </span>
              </div>
              <p v-if="review.content" class="text-sm text-slate-600">{{ review.content }}</p>
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

    <!-- 评价弹窗 -->
    <div v-if="reviewDialogVisible" class="fixed inset-0 z-50 flex items-center justify-center p-4">
      <div class="absolute inset-0 bg-black/50" @click="reviewDialogVisible = false"></div>
      <div class="relative bg-white rounded-3xl w-full max-w-md overflow-hidden">
        <div class="p-4 border-b border-slate-100 flex items-center justify-between">
          <h3 class="font-semibold">评价商品</h3>
          <button @click="reviewDialogVisible = false" class="text-slate-400 hover:text-slate-600">
            <i class="fa-solid fa-xmark"></i>
          </button>
        </div>
        <div v-if="currentReviewItem" class="p-4">
          <div class="flex gap-3 mb-6">
            <img :src="currentReviewItem.image" :alt="currentReviewItem.name" class="w-16 h-16 rounded-xl object-cover" />
            <div class="flex-1 min-w-0">
              <p class="font-medium line-clamp-1">{{ currentReviewItem.name }}</p>
              <p v-if="currentReviewItem.spec" class="text-sm text-slate-400">{{ currentReviewItem.spec }}</p>
            </div>
          </div>
          
          <div class="mb-6">
            <label class="block text-sm font-medium text-slate-700 mb-2">评分</label>
            <div class="flex gap-2">
              <button
                v-for="n in 5"
                :key="n"
                @click="reviewForm.rating = n"
                class="text-2xl transition-transform hover:scale-110"
                :class="n <= reviewForm.rating ? 'text-amber-400' : 'text-slate-200'"
              >
                <i class="fa-star" :class="n <= reviewForm.rating ? 'fa-solid' : 'fa-regular'"></i>
              </button>
            </div>
          </div>
          
          <div class="mb-6">
            <label class="block text-sm font-medium text-slate-700 mb-2">评价内容</label>
            <textarea
              v-model="reviewForm.content"
              placeholder="分享您的购买体验..."
              class="w-full px-4 py-3 border border-slate-200 rounded-2xl resize-none focus:outline-none focus:border-primary-500"
              rows="4"
            ></textarea>
          </div>
          
          <div class="flex gap-3">
            <button
              @click="reviewDialogVisible = false"
              class="flex-1 py-3 rounded-2xl border border-slate-200 text-slate-600 hover:border-slate-300 transition"
            >
              取消
            </button>
            <button
              @click="handleSubmitReview"
              :disabled="submittingReview"
              class="flex-1 py-3 rounded-2xl bg-amber-500 text-white hover:bg-amber-600 transition disabled:opacity-50"
            >
              {{ submittingReview ? '提交中...' : '提交评价' }}
            </button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import NavBar from '@/components/common/NavBar.vue'
import Loading from '@/components/common/Loading.vue'
import ConfirmDialog from '@/components/common/ConfirmDialog.vue'
import { getOrderDetail, payOrder, cancelOrder, receiveOrder, submitReview } from '@/api/order'
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

// 评价弹窗状态
const reviewDialogVisible = ref(false)
const currentReviewItem = ref(null)
const reviewForm = ref({
  orderItemId: null,
  rating: 5,
  content: ''
})
const submittingReview = ref(false)

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
    // 构建评价映射
    const reviewMap = {}
    if (rawOrder.reviews && rawOrder.reviews.length > 0) {
      rawOrder.reviews.forEach(review => {
        reviewMap[review.orderItemId] = review
      })
    }
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
        spec: item.specName,
        review: reviewMap[item.id] || null
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

// 打开评价弹窗
function openReviewDialog(item) {
  currentReviewItem.value = item
  reviewForm.value = {
    orderItemId: item.id,
    rating: 5,
    content: ''
  }
  reviewDialogVisible.value = true
}

// 提交评价
async function handleSubmitReview() {
  if (!reviewForm.value.rating || reviewForm.value.rating < 1 || reviewForm.value.rating > 5) {
    showToast('请选择1-5星评分', 'error')
    return
  }
  if (reviewForm.value.content && reviewForm.value.content.length > 500) {
    showToast('评价内容不能超过500字', 'error')
    return
  }
  submittingReview.value = true
  try {
    await submitReview(route.params.id, reviewForm.value)
    showToast('评价成功', 'success')
    reviewDialogVisible.value = false
    fetchDetail()
  } catch {
    // Error handled by interceptor
  } finally {
    submittingReview.value = false
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
