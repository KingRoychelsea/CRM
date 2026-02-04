import { defineStore } from 'pinia'
import { login, logout, getInfo } from '../api/user'
import { usePermissionStore } from './permission'

export const useUserStore = defineStore('user', {
  state: () => ({
    token: localStorage.getItem('token') || '',
    userInfo: JSON.parse((localStorage.getItem('userInfo') || '{}') === 'undefined' ? '{}' : (localStorage.getItem('userInfo') || '{}')),
    roles: [],
    permissions: []
  }),
  getters: {
    isLoggedIn: (state) => !!state.token,
    hasPermission: (state) => (permission) => {
      return state.permissions.includes(permission)
    }
  },
  actions: {
    // 登录
    async login(userInfo) {
      const { username, password } = userInfo
      const response = await login({ username, password })
      const { token } = response.data
      this.token = token
      localStorage.setItem('token', token)
      return response
    },
    // 获取用户信息
    async getInfo() {
      const response = await getInfo()
      const { user, roles, permissions } = response.data
      this.userInfo = user
      this.roles = roles
      this.permissions = permissions
      localStorage.setItem('userInfo', JSON.stringify(user))
      
      // 生成动态路由
      const permissionStore = usePermissionStore()
      await permissionStore.generateRoutes(roles)
      
      return response
    },
    // 登出
    async logout() {
      await logout()
      this.token = ''
      this.userInfo = {}
      this.roles = []
      this.permissions = []
      localStorage.removeItem('token')
      localStorage.removeItem('userInfo')
    },
    // 清除token
    resetToken() {
      this.token = ''
      this.userInfo = {}
      this.roles = []
      this.permissions = []
      localStorage.removeItem('token')
      localStorage.removeItem('userInfo')
    }
  }
})
