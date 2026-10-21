import { defineStore } from 'pinia'

export const useUserStore = defineStore('user', {
  state: () => ({
    token: localStorage.getItem('admin_token') || '',
    user: JSON.parse(localStorage.getItem('admin_user') || 'null')
  }),
  
  getters: {
    isLoggedIn: (state) => !!state.token,
    isAdmin: (state) => state.user?.userType === 1
  },
  
  actions: {
    setToken(token) {
      this.token = token
      localStorage.setItem('admin_token', token)
    },
    
    setUser(user) {
      this.user = user
      localStorage.setItem('admin_user', JSON.stringify(user))
    },
    
    logout() {
      this.token = ''
      this.user = null
      localStorage.removeItem('admin_token')
      localStorage.removeItem('admin_user')
    }
  }
})
