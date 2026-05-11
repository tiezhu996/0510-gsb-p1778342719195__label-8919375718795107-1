<template>
  <div class="min-h-screen bg-slate-50 pb-24">
    <NavBar title="确认订单" :show-back="true" />

    <main class="max-w-4xl mx-auto px-4 sm:px-6 py-4 space-y-4">
      <!-- Address Section -->
      <section class="bg-white rounded-3xl p-4 shadow-sm">
        <div v-if="selectedAddress" class="flex items-center gap-3">
          <i class="fa-solid fa-location-dot text-primary-500"></i>
          <div class="flex-1">
            <div class="flex items-center gap-2">
              <span class="font-medium">{{ selectedAddress.name }}</span>
              <span class="text-slate-500">{{ selectedAddress.phone }}</span>
            </div>
            <p class="text-sm text-slate-500 mt-1">
              {{ selectedAddress.province }}{{ selectedAddress.city }}{{ selectedAddress.district }}{{ selectedAddress.detail }}
            </p>
          </div>
          <button @click="showAddressPicker = true" class="text-primary-500 text-sm">
            更换
          </button>
        </div>
        <div v-else class="flex items-center justify-between">
          <span class="text-slate-500">请选择收货地址</span>
          <button @click="showAddressPicker = true" class="text-primary-500 text-sm">
            选择地址
          </button>
        </div>
      </section>

      <!-- Order Items -->
      <section class="bg-white rounded-3xl p-4 shadow-sm">
        <h3 class="font-medium mb-4">商品清单</h3>
        <div class="space-y-3">
          <div
            v-for="item in orderItems"
            :key="item.id"
            class="flex gap-3"
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
        </div>
      </section>

      <!-- Order Note -->
      <section class="bg-white rounded-3xl p-4 shadow-sm">
        <label class="text-sm text-slate-600">订单备注</label>
        <input
          v-model="orderNote"
          class="w-full mt-2 px-3 py-2 rounded-xl border border-slate-200 outline-none focus:border-primary-500 text-sm"
          placeholder="选填，请输入备注信息"
        />
      </section>

      <!-- Price Summary -->
      <section class="bg-white rounded-3xl p-4 shadow-sm">
        <div class="space-y-2 text-sm">
          <div class="flex justify-between">
            <span class="text-slate-500">商品金额</span>
            <span>¥{{ subtotal.toFixed(2) }}</span>
          </div>
          <div class="flex justify-between">
            <span class="text-slate-500">运费</span>
            <span>{{ shippingFee > 0 ? '¥' + shippingFee.toFixed(2) : '免运费' }}</span>
          </div>
          <div class="flex justify-between">
            <span class="text-slate-500">优惠</span>
            <span class="text-primary-500">-¥{{ discount.toFixed(2) }}</span>
          </div>
          <div class="border-t border-slate-100 pt-2 flex justify-between font-medium">
            <span>实付金额</span>
            <span class="text-primary-500 text-lg">¥{{ totalPrice.toFixed(2) }}</span>
          </div>
        </div>
      </section>
    </main>

    <!-- Bottom Bar -->
    <div class="fixed bottom-0 left-0 right-0 bg-white border-t border-slate-100 px-4 py-3 safe-area-pb">
      <div class="max-w-4xl mx-auto flex items-center justify-between">
        <div>
          <span class="text-sm text-slate-500">实付：</span>
          <span class="text-xl font-bold text-primary-500">¥{{ totalPrice.toFixed(2) }}</span>
        </div>
        <button
          @click="handleSubmit"
          :disabled="submitting || !selectedAddress"
          class="px-8 py-2.5 rounded-2xl bg-primary-500 text-white font-medium hover:bg-primary-600 transition disabled:opacity-50"
        >
          {{ submitting ? '提交中...' : '提交订单' }}
        </button>
      </div>
    </div>

    <!-- Address Picker Modal -->
    <Transition name="fade">
      <div v-if="showAddressPicker" class="fixed inset-0 bg-black/50 flex items-end sm:items-center justify-center z-50">
        <div class="bg-white w-full sm:w-96 sm:rounded-3xl rounded-t-3xl p-6 max-h-[70vh] overflow-y-auto">
          <div class="flex items-center justify-between mb-4">
            <h3 class="font-semibold">选择收货地址</h3>
            <button @click="showAddressPicker = false" class="text-slate-400">
              <i class="fa-solid fa-xmark"></i>
            </button>
          </div>
          <div class="space-y-3">
            <div
              v-for="addr in addresses"
              :key="addr.id"
              @click="selectAddress(addr)"
              :class="[
                'p-4 rounded-2xl border-2 cursor-pointer transition',
                selectedAddress?.id === addr.id
                  ? 'border-primary-500 bg-primary-50'
                  : 'border-slate-200 hover:border-primary-200'
              ]"
            >
              <div class="flex items-center gap-2">
                <span class="font-medium">{{ addr.name }}</span>
                <span class="text-slate-500 text-sm">{{ addr.phone }}</span>
                <span v-if="addr.isDefault" class="text-xs text-primary-500 border border-primary-200 px-1.5 py-0.5 rounded-full">默认</span>
              </div>
              <p class="text-sm text-slate-500 mt-1">
                {{ addr.province }}{{ addr.city }}{{ addr.district }}{{ addr.detail }}
              </p>
            </div>
          </div>
          <router-link
            to="/address"
            class="block mt-4 text-center text-primary-500 text-sm"
          >
            + 新增收货地址
          </router-link>
        </div>
      </div>
    </Transition>

    <Loading :visible="loading" />
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import NavBar from '@/components/common/NavBar.vue'
import Loading from '@/components/common/Loading.vue'
import { useCartStore } from '@/stores/cart'
import { getAddressList } from '@/api/address'
import { createOrder } from '@/api/order'
import { showToast } from '@/utils/toast'

const router = useRouter()
const cartStore = useCartStore()

const loading = ref(false)
const submitting = ref(false)
const showAddressPicker = ref(false)
const addresses = ref([])
const selectedAddress = ref(null)
const orderNote = ref('')

const orderItems = computed(() => cartStore.selectedItems)

const subtotal = computed(() => {
  return orderItems.value.reduce((sum, item) => sum + item.price * item.quantity, 0)
})

const shippingFee = computed(() => {
  return subtotal.value >= 99 ? 0 : 8
})

const discount = computed(() => {
  return 0
})

const totalPrice = computed(() => {
  return subtotal.value + shippingFee.value - discount.value
})

onMounted(async () => {
  loading.value = true
  try {
    addresses.value = await getAddressList()
    selectedAddress.value = addresses.value.find(a => a.isDefault) || addresses.value[0] || null
  } catch {
    addresses.value = [
      { id: 1, name: '张三', phone: '138****8000', province: '北京市', city: '北京市', district: '朝阳区', detail: '建国路88号SOHO现代城', isDefault: true }
    ]
    selectedAddress.value = addresses.value[0]
  } finally {
    loading.value = false
  }
})

function selectAddress(addr) {
  selectedAddress.value = addr
  showAddressPicker.value = false
}

async function handleSubmit() {
  if (!selectedAddress.value) {
    showToast('请选择收货地址', 'warning')
    return
  }

  submitting.value = true
  try {
    const result = await createOrder({
      addressId: selectedAddress.value.id,
      remark: orderNote.value,
      cartIds: orderItems.value.map(item => item.id)
    })
    showToast('订单提交成功', 'success')
    router.push(`/order/detail/${result.orderId}`)
  } catch {
    // Error handled by interceptor
  } finally {
    submitting.value = false
  }
}
</script>

<style scoped>
.safe-area-pb {
  padding-bottom: max(0.75rem, env(safe-area-inset-bottom));
}

.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.2s ease;
}

.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}
</style>
