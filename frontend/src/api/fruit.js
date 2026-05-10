import request from '@/utils/request'

export function getHomeData() {
  return request.get('/fruit/home')
}

export function getFruitList(params) {
  return request.get('/fruit/list', { params })
}

export function getFruitDetail(id) {
  return request.get(`/fruit/${id}`)
}

export function getCategoryList() {
  return request.get('/category/list')
}
