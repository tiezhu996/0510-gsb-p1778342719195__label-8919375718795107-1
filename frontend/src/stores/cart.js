import { defineStore } from 'pinia'
import * as cartApi from '@/api/cart'
import { showToast } from '@/utils/toast'

export const useCartStore = defineStore('cart', {
  state: () => ({
    items: [],
    loading: false
  }),

  getters: {
    totalCount: state => state.items.reduce((sum, item) => sum + item.quantity, 0),
    // Only count valid items (status !== 0) for selection and pricing
    validItems: state => state.items.filter(item => item.status !== 0),
    selectedItems: state => state.items.filter(item => item.selected && item.status !== 0),
    selectedCount: state => state.items.filter(item => item.selected && item.status !== 0).reduce((sum, item) => sum + item.quantity, 0),
    totalPrice: state => state.items.filter(item => item.selected && item.status !== 0).reduce((sum, item) => sum + item.price * item.quantity, 0),
    isAllSelected: state => {
      const validItems = state.items.filter(item => item.status !== 0)
      return validItems.length > 0 && validItems.every(item => item.selected)
    }
  },

  actions: {
    async fetchCart() {
      this.loading = true
      try {
        const data = await cartApi.getCartList()
        // 转换后端数据格式到前端期望格式
        this.items = (data.items || []).map(item => ({
          ...item,
          name: item.fruitName || item.name,
          image: item.fruitImage || item.image,
          spec: item.specName || item.spec
        }))
      } finally {
        this.loading = false
      }
    },

    async addToCart(fruitId, specId, quantity = 1) {
      await cartApi.addToCart({ fruitId, specId, quantity })
      showToast('已加入购物车', 'success')
      await this.fetchCart()
    },

    async updateQuantity(id, quantity) {
      await cartApi.updateCartQuantity(id, quantity)
      const item = this.items.find(i => i.id === id)
      if (item) item.quantity = quantity
    },

    async removeItem(id) {
      await cartApi.deleteCartItem(id)
      this.items = this.items.filter(i => i.id !== id)
      showToast('已删除', 'success')
    },

    async toggleSelect(id) {
      const item = this.items.find(i => i.id === id)
      if (item) {
        await cartApi.updateCartSelected(id, !item.selected)
        item.selected = !item.selected
      }
    },

    async selectAll(selected) {
      await cartApi.selectAllCart(selected)
      this.items.forEach(item => {
        item.selected = selected
      })
    }
  }
})
