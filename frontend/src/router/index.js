import { createRouter, createWebHistory } from 'vue-router'

// 静态路由
const routes = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('../pages/login/Login.vue'),
    meta: {
      title: '登录'
    }
  },
  {
    path: '/',
    name: 'Home',
    component: () => import('../layout/index.vue'),
    redirect: '/dashboard',
    children: [
      {
        path: 'dashboard',
        name: 'Dashboard',
        component: () => import('../pages/dashboard/index.vue'),
        meta: {
          title: '首页',
          requiresAuth: true
        }
      },
      {
        path: 'system/user',
        name: 'SystemUser',
        component: () => import('../pages/system/user/index.vue'),
        meta: {
          title: '用户管理',
          requiresAuth: true
        }
      },
      {
        path: 'system/role',
        name: 'SystemRole',
        component: () => import('../pages/system/role/index.vue'),
        meta: {
          title: '角色管理',
          requiresAuth: true
        }
      },
      {
        path: 'system/menu',
        name: 'SystemMenu',
        component: () => import('../pages/system/menu/index.vue'),
        meta: {
          title: '菜单管理',
          requiresAuth: true
        }
      },
      {
        path: 'system/dept',
        name: 'SystemDept',
        component: () => import('../pages/system/dept/index.vue'),
        meta: {
          title: '部门管理',
          requiresAuth: true
        }
      },
      {
        path: 'system/dict',
        name: 'SystemDict',
        component: () => import('../pages/system/dict/index.vue'),
        meta: {
          title: '数据字典',
          requiresAuth: true
        }
      },
      {
        path: 'system/log',
        name: 'SystemLog',
        component: () => import('../pages/system/log/index.vue'),
        meta: {
          title: '系统日志',
          requiresAuth: true
        }
      },
      {
        path: 'customer',
        name: 'Customer',
        component: () => import('../pages/customer/index.vue'),
        meta: {
          title: '客户管理',
          requiresAuth: true
        }
      },
      {
        path: 'sales/opportunity',
        name: 'SalesOpportunity',
        component: () => import('../pages/sales/opportunity/index.vue'),
        meta: {
          title: '商机管理',
          requiresAuth: true
        }
      },
      {
        path: 'sales/contract',
        name: 'SalesContract',
        component: () => import('../pages/sales/contract/index.vue'),
        meta: {
          title: '合同管理',
          requiresAuth: true
        }
      },
      {
        path: 'sales/order',
        name: 'SalesOrder',
        component: () => import('../pages/sales/order/index.vue'),
        meta: {
          title: '订单管理',
          requiresAuth: true
        }
      },
      {
        path: 'sales/payment',
        name: 'SalesPayment',
        component: () => import('../pages/sales/payment/index.vue'),
        meta: {
          title: '回款管理',
          requiresAuth: true
        }
      },
      {
        path: 'stats',
        name: 'Stats',
        component: () => import('../pages/stats/index.vue'),
        meta: {
          title: '数据统计',
          requiresAuth: true
        }
      }
    ]
  }
]

// 创建路由实例
const router = createRouter({
  history: createWebHistory(),
  routes
})

// 路由守卫
router.beforeEach((to, from, next) => {
  // 设置页面标题
  document.title = to.meta.title || 'CRM系统'
  
  // 检查是否需要认证
  if (to.meta.requiresAuth) {
    const token = localStorage.getItem('token')
    if (token) {
      next()
    } else {
      next({ name: 'Login' })
    }
  } else {
    next()
  }
})

export default router
