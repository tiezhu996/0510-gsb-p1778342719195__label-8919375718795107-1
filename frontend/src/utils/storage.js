const TOKEN_KEY = 'fruitshop_token'
const USER_KEY = 'fruitshop_user'

export function getItem(key) {
  const value = localStorage.getItem(key)
  try {
    return JSON.parse(value)
  } catch {
    return value
  }
}

export function setItem(key, value) {
  localStorage.setItem(key, typeof value === 'string' ? value : JSON.stringify(value))
}

export const storage = {
  getToken() {
    return localStorage.getItem(TOKEN_KEY)
  },

  setToken(token) {
    localStorage.setItem(TOKEN_KEY, token)
  },

  removeToken() {
    localStorage.removeItem(TOKEN_KEY)
  },

  getUser() {
    const user = localStorage.getItem(USER_KEY)
    return user ? JSON.parse(user) : null
  },

  setUser(user) {
    localStorage.setItem(USER_KEY, JSON.stringify(user))
  },

  removeUser() {
    localStorage.removeItem(USER_KEY)
  },

  clear() {
    this.removeToken()
    this.removeUser()
  }
}
