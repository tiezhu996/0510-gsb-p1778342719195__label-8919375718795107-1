<template>
  <div class="min-h-screen bg-slate-50">
    <NavBar title="收货地址" :show-back="true" />

    <main class="max-w-4xl mx-auto px-4 sm:px-6 py-6">
      <section class="space-y-4">
        <div
          v-for="address in addresses"
          :key="address.id"
          class="bg-white rounded-3xl p-6 shadow-sm"
        >
          <div class="flex items-center justify-between">
            <div>
              <p class="font-semibold">{{ address.name }} {{ address.phone }}</p>
              <p class="text-sm text-slate-500 mt-1">
                {{ address.province }}{{ address.city }}{{ address.district }}{{ address.detail }}
              </p>
            </div>
            <span
              v-if="address.isDefault"
              class="text-xs text-primary-500 border border-primary-200 px-2 py-1 rounded-full"
            >
              默认
            </span>
          </div>
          <div class="mt-4 flex items-center gap-3 text-sm text-slate-500">
            <button @click="editAddress(address)" class="px-3 py-1 rounded-full border border-slate-200 hover:border-primary-500 transition">
              编辑
            </button>
            <button @click="handleDelete(address.id)" class="px-3 py-1 rounded-full border border-slate-200 hover:border-red-500 hover:text-red-500 transition">
              删除
            </button>
            <button
              v-if="!address.isDefault"
              @click="handleSetDefault(address.id)"
              class="px-3 py-1 rounded-full border border-slate-200 hover:border-primary-500 transition"
            >
              设为默认
            </button>
          </div>
        </div>

        <Empty v-if="addresses.length === 0" text="暂无收货地址" />
      </section>

      <button
        @click="showModal = true"
        class="mt-6 w-full py-3 rounded-2xl bg-primary-500 text-white hover:bg-primary-600 transition"
      >
        + 新增收货地址
      </button>
    </main>

    <!-- Address Modal -->
    <Transition name="fade">
      <div v-if="showModal" class="fixed inset-0 bg-black/50 flex items-center justify-center z-50 p-4">
        <div class="bg-white rounded-3xl p-6 w-full max-w-md">
          <h3 class="text-lg font-semibold">{{ editingId ? '编辑地址' : '新增地址' }}</h3>
          <form class="mt-4 space-y-4" @submit.prevent="handleSubmit">
            <div>
              <label class="text-sm text-slate-600">收货人</label>
              <input
                v-model="form.name"
                class="w-full mt-2 px-4 py-3 rounded-2xl border border-slate-200 outline-none focus:border-primary-500"
                placeholder="请输入收货人姓名"
              />
            </div>
            <div>
              <label class="text-sm text-slate-600">手机号</label>
              <input
                v-model="form.phone"
                class="w-full mt-2 px-4 py-3 rounded-2xl border border-slate-200 outline-none focus:border-primary-500"
                placeholder="请输入手机号"
                maxlength="11"
              />
            </div>
            <div class="grid grid-cols-3 gap-2">
              <div>
                <label class="text-sm text-slate-600">省</label>
                <input
                  v-model="form.province"
                  class="w-full mt-2 px-3 py-3 rounded-2xl border border-slate-200 outline-none focus:border-primary-500 text-sm"
                  placeholder="省"
                />
              </div>
              <div>
                <label class="text-sm text-slate-600">市</label>
                <input
                  v-model="form.city"
                  class="w-full mt-2 px-3 py-3 rounded-2xl border border-slate-200 outline-none focus:border-primary-500 text-sm"
                  placeholder="市"
                />
              </div>
              <div>
                <label class="text-sm text-slate-600">区</label>
                <input
                  v-model="form.district"
                  class="w-full mt-2 px-3 py-3 rounded-2xl border border-slate-200 outline-none focus:border-primary-500 text-sm"
                  placeholder="区"
                />
              </div>
            </div>
            <div>
              <label class="text-sm text-slate-600">详细地址</label>
              <input
                v-model="form.detail"
                class="w-full mt-2 px-4 py-3 rounded-2xl border border-slate-200 outline-none focus:border-primary-500"
                placeholder="请输入详细地址"
              />
            </div>
            <label class="flex items-center gap-2 text-sm text-slate-500 cursor-pointer">
              <input v-model="form.isDefault" type="checkbox" class="rounded text-primary-500" />
              设为默认地址
            </label>
            <div class="flex gap-3">
              <button
                type="button"
                @click="closeModal"
                class="flex-1 py-3 rounded-2xl border border-slate-200"
              >
                取消
              </button>
              <button
                type="submit"
                class="flex-1 py-3 rounded-2xl bg-primary-500 text-white"
              >
                保存
              </button>
            </div>
          </form>
        </div>
      </div>
    </Transition>

    <Loading :visible="loading" />
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import NavBar from '@/components/common/NavBar.vue'
import Loading from '@/components/common/Loading.vue'
import Empty from '@/components/common/Empty.vue'
import * as addressApi from '@/api/address'
import { showToast } from '@/utils/toast'

const loading = ref(false)
const showModal = ref(false)
const editingId = ref(null)
const addresses = ref([])

const form = reactive({
  name: '',
  phone: '',
  province: '',
  city: '',
  district: '',
  detail: '',
  isDefault: false
})

onMounted(() => {
  fetchAddresses()
})

async function fetchAddresses() {
  loading.value = true
  try {
    addresses.value = await addressApi.getAddressList()
  } catch {
    addresses.value = [
      { id: 1, name: '张三', phone: '138****8000', province: '北京市', city: '北京市', district: '朝阳区', detail: '建国路88号SOHO现代城', isDefault: true },
      { id: 2, name: '李四', phone: '139****9000', province: '上海市', city: '上海市', district: '浦东新区', detail: '世纪大道1号', isDefault: false }
    ]
  } finally {
    loading.value = false
  }
}

function editAddress(address) {
  editingId.value = address.id
  // Copy address fields but exclude masked phone - user needs to re-enter
  const { phone, ...addressFields } = address
  Object.assign(form, addressFields)
  form.phone = '' // Clear phone so user must re-enter it
  showModal.value = true
}

function closeModal() {
  showModal.value = false
  editingId.value = null
  Object.assign(form, { name: '', phone: '', province: '', city: '', district: '', detail: '', isDefault: false })
}

async function handleSubmit() {
  if (!form.name || !form.phone || !form.detail) {
    showToast('请填写完整信息', 'warning')
    return
  }

  try {
    if (editingId.value) {
      // Remove id and fullAddress fields from form data before sending
      const { id, fullAddress, ...updateData } = form
      await addressApi.updateAddress(editingId.value, updateData)
      showToast('修改成功', 'success')
    } else {
      await addressApi.addAddress(form)
      showToast('添加成功', 'success')
    }
    closeModal()
    fetchAddresses()
  } catch {
    // Error handled by interceptor
  }
}

async function handleDelete(id) {
  if (!confirm('确定删除该地址吗？')) return
  try {
    await addressApi.deleteAddress(id)
    showToast('删除成功', 'success')
    fetchAddresses()
  } catch {
    // Error handled by interceptor
  }
}

async function handleSetDefault(id) {
  try {
    await addressApi.setDefaultAddress(id)
    showToast('设置成功', 'success')
    fetchAddresses()
  } catch {
    // Error handled by interceptor
  }
}
</script>
