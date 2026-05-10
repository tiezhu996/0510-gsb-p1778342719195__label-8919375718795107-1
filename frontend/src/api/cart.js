import request from '@/utils/request'

export function getCartList() {
  return request.get('/cart/list')
}

export function addToCart(data) {
  return request.post('/cart/add', data)
}

export function updateCartQuantity(id, quantity) {
  return request.put(`/cart/${id}/quantity`, { quantity })
}

export function deleteCartItem(id) {
  return request.delete(`/cart/${id}`)
}

export function updateCartSelected(id, selected) {
  return request.put(`/cart/${id}/selected`, { selected })
}

export function selectAllCart(selected) {
  return request.put('/cart/select-all', { selected })
}
