import { defineStore } from 'pinia'
import { storage } from '@/utils/storage'
import * as userApi from '@/api/user'

export const useUserStore = defineStore('user', {
  state: () => ({
    token: storage.getToken() || '',
    userInfo: storage.getUser() || null
  }),

  getters: {
    isLoggedIn: state => !!state.token,
    maskedPhone: state => state.userInfo?.phone || ''
  },

  actions: {
    async login(credentials) {
      const data = await userApi.login(credentials)
      this.token = data.token
      this.userInfo = data.user
      storage.setToken(data.token)
      storage.setUser(data.user)
      return data
    },

    async register(formData) {
      const data = await userApi.register(formData)
      this.token = data.token
      this.userInfo = data.user
      storage.setToken(data.token)
      storage.setUser(data.user)
      return data
    },

    async fetchUserInfo() {
      const data = await userApi.getUserInfo()
      this.userInfo = data
      storage.setUser(data)
      return data
    },

    async updateNickname(nickname) {
      const data = await userApi.updateNickname(nickname)
      if (this.userInfo) {
        this.userInfo.nickname = nickname
        storage.setUser(this.userInfo)
      }
      return data
    },

    logout() {
      this.token = ''
      this.userInfo = null
      storage.clear()
    }
  }
})
