import { defineStore } from 'pinia'
import router from '../router'

export const usePermissionStore = defineStore('permission', {
  state: () => ({
    routes: [],
    addRoutes: []
  }),
  actions: {
    // 生成路由
    generateRoutes(roles) {
      return new Promise(resolve => {
        // 模拟动态路由
        const asyncRoutes = []

        // 添加动态路由作为 Home 路由的子路由
        asyncRoutes.forEach(route => {
          router.addRoute('Home', route)
        })

        this.addRoutes = asyncRoutes
        this.routes = router.options.routes.concat(asyncRoutes)
        resolve(asyncRoutes)
      })
    },
    // 重置路由
    resetRoutes() {
      this.routes = []
      this.addRoutes = []
    }
  }
})
