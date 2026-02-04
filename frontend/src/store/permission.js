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
        const asyncRoutes = [
          {
            path: '/system',
            name: 'System',
            component: () => import('../layout/index.vue'),
            meta: {
              title: '系统管理',
              icon: 'Setting'
            },
            children: [
              {
                path: 'user',
                name: 'User',
                component: () => import('../pages/system/user/index.vue'),
                meta: {
                  title: '用户管理',
                  requiresAuth: true
                }
              },
              {
                path: 'role',
                name: 'Role',
                component: () => import('../pages/system/role/index.vue'),
                meta: {
                  title: '角色管理',
                  requiresAuth: true
                }
              }
            ]
          },
          {
            path: '/customer',
            name: 'Customer',
            component: () => import('../layout/index.vue'),
            meta: {
              title: '客户管理',
              icon: 'UserFilled'
            },
            children: [
              {
                path: 'index',
                name: 'CustomerList',
                component: () => import('../pages/customer/index.vue'),
                meta: {
                  title: '客户列表',
                  requiresAuth: true
                }
              }
            ]
          },
          {
            path: '/sales',
            name: 'Sales',
            component: () => import('../layout/index.vue'),
            meta: {
              title: '销售管理',
              icon: 'TrendCharts'
            },
            children: [
              {
                path: 'opportunity',
                name: 'Opportunity',
                component: () => import('../pages/sales/opportunity/index.vue'),
                meta: {
                  title: '商机管理',
                  requiresAuth: true
                }
              },
              {
                path: 'contract',
                name: 'Contract',
                component: () => import('../pages/sales/contract/index.vue'),
                meta: {
                  title: '合同管理',
                  requiresAuth: true
                }
              },
              {
                path: 'order',
                name: 'Order',
                component: () => import('../pages/sales/order/index.vue'),
                meta: {
                  title: '订单管理',
                  requiresAuth: true
                }
              },
              {
                path: 'payment',
                name: 'Payment',
                component: () => import('../pages/sales/payment/index.vue'),
                meta: {
                  title: '回款管理',
                  requiresAuth: true
                }
              }
            ]
          },
          {
            path: '/stats',
            name: 'Stats',
            component: () => import('../layout/index.vue'),
            meta: {
              title: '数据统计',
              icon: 'DataAnalysis'
            },
            children: [
              {
                path: 'index',
                name: 'StatsIndex',
                component: () => import('../pages/stats/index.vue'),
                meta: {
                  title: '统计分析',
                  requiresAuth: true
                }
              }
            ]
          }
        ]

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
