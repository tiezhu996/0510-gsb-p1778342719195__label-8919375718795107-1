import request from '@/utils/request'

export function createOrder(data) {
  return request.post('/order/create', data)
}

export function getOrderList(params) {
  return request.get('/order/list', { params }).then(res => res.list || [])
}

export function getOrderDetail(id) {
  return request.get(`/order/${id}`)
}

export function payOrder(id) {
  return request.post(`/order/${id}/pay`)
}

export function cancelOrder(id) {
  return request.post(`/order/${id}/cancel`)
}

export function receiveOrder(id) {
  return request.post(`/order/${id}/receive`)
}

export function deleteOrder(id) {
  return request.delete(`/order/${id}`)
}

export function submitReview(orderId, data) {
  return request.post(`/review/order/${orderId}`, data)
}

export function getOrderReviews(orderId) {
  return request.get(`/review/order/${orderId}`)
}
