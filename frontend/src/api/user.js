import request from '@/utils/request'

export function register(data) {
  return request.post('/user/register', data)
}

export function login(data) {
  return request.post('/user/login', data)
}

export function getUserInfo() {
  return request.get('/user/info')
}

export function updatePassword(data) {
  return request.put('/user/password', data)
}

export function updateNickname(nickname) {
  return request.put('/user/nickname', { nickname })
}
