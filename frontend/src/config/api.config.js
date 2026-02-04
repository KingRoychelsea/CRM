/**
 * API路径配置
 * 前端API路径集中管理配置
 */
export const API_CONFIG = {
  // 基础配置
  BASE_URL: '',
  TIMEOUT: 10000,
  
  // 认证相关
  AUTH: {
    LOGIN: '/api/auth/login',
    LOGOUT: '/api/auth/logout',
    REFRESH: '/api/auth/refresh'
  },
  
  // 系统管理
  SYSTEM: {
    // 用户管理
    USER: {
      LIST: '/api/sys/user/list',
      DETAIL: '/api/sys/user',
      CREATE: '/api/sys/user',
      UPDATE: '/api/sys/user',
      DELETE: '/api/sys/user',
      BATCH_DELETE: '/api/sys/user/batch',
      RESET_PASSWORD: '/api/sys/user/resetPassword',
      UPDATE_STATUS: '/api/sys/user/status',
      ROLES: '/api/sys/user/roles',
      ASSIGN_ROLES: '/api/sys/user/roles'
    },
    
    // 角色管理
    ROLE: {
      LIST: '/api/sys/role/list',
      DETAIL: '/api/sys/role',
      CREATE: '/api/sys/role',
      UPDATE: '/api/sys/role',
      DELETE: '/api/sys/role',
      MENUS: '/api/sys/role/menu',
      ASSIGN_MENUS: '/api/sys/role/menu'
    },
    
    // 部门管理
    DEPT: {
      LIST: '/api/sys/dept/list',
      TREE: '/api/sys/dept/tree',
      DETAIL: '/api/sys/dept',
      CREATE: '/api/sys/dept',
      UPDATE: '/api/sys/dept',
      DELETE: '/api/sys/dept'
    },
    
    // 菜单管理
    MENU: {
      LIST: '/api/sys/menu/list',
      TREE: '/api/sys/menu/tree',
      DETAIL: '/api/sys/menu',
      CREATE: '/api/sys/menu',
      UPDATE: '/api/sys/menu',
      DELETE: '/api/sys/menu'
    },
    
    // 字典管理
    DICT: {
      LIST: '/api/sys/dict/type/list',
      DETAIL: '/api/sys/dict/type',
      CREATE: '/api/sys/dict/type',
      UPDATE: '/api/sys/dict/type',
      DELETE: '/api/sys/dict/type',
      ITEMS: '/api/sys/dict/item'
    },
    
    // 日志管理
    LOG: {
      LOGIN_LIST: '/api/sys/log/login/list',
      OPERATION_LIST: '/api/sys/log/oper/list',
      ERROR_LIST: '/api/sys/log/error'
    }
  },
  
  // 客户管理
  CUSTOMER: {
    LIST: '/api/customer/list',
    DETAIL: '/api/customer',
    CREATE: '/api/customer',
    UPDATE: '/api/customer',
    DELETE: '/api/customer',
    CONTACTS: '/api/customer/contacts',
    FOLLOW_RECORDS: '/api/customer/follow-records'
  },
  
  // 销售管理
  SALES: {
    // 商机管理
    OPPORTUNITY: {
      LIST: '/api/sales/opportunity/list',
      DETAIL: '/api/sales/opportunity',
      CREATE: '/api/sales/opportunity',
      UPDATE: '/api/sales/opportunity',
      DELETE: '/api/sales/opportunity'
    },
    
    // 合同管理
    CONTRACT: {
      LIST: '/api/sales/contract/list',
      DETAIL: '/api/sales/contract',
      CREATE: '/api/sales/contract',
      UPDATE: '/api/sales/contract',
      DELETE: '/api/sales/contract'
    },
    
    // 订单管理
    ORDER: {
      LIST: '/api/sales/order/list',
      DETAIL: '/api/sales/order',
      CREATE: '/api/sales/order',
      UPDATE: '/api/sales/order',
      DELETE: '/api/sales/order'
    },
    
    // 回款管理
    PAYMENT: {
      LIST: '/api/sales/payment/list',
      DETAIL: '/api/sales/payment',
      CREATE: '/api/sales/payment',
      UPDATE: '/api/sales/payment',
      DELETE: '/api/sales/payment'
    }
  },
  
  // 统计报表
  STATS: {
    DASHBOARD: '/api/stats/dashboard',
    CUSTOMER_STATS: '/api/stats/customer',
    SALES_STATS: '/api/stats/sales',
    PERFORMANCE_STATS: '/api/stats/performance'
  },
  
  // 健康检查
  HEALTH: {
    STATUS: '/api/health/status',
    ENDPOINTS: '/api/health/endpoints'
  }
};

/**
 * 获取完整的API路径
 */
export function getApiPath(basePath, subPath = '') {
  if (!subPath) return basePath;
  return `${basePath}/${subPath}`;
}

/**
 * 获取带ID的API路径
 */
export function getApiPathWithId(basePath, id) {
  return `${basePath}/${id}`;
}

/**
 * 获取带ID和子路径的API路径
 */
export function getApiPathWithIdAndSubPath(basePath, id, subPath) {
  return `${basePath}/${id}/${subPath}`;
}

/**
 * 环境配置
 */
export const ENV_CONFIG = {
  development: {
    baseUrl: '',
    timeout: 10000
  },
  production: {
    baseUrl: import.meta.env?.VITE_API_BASE_URL || '',
    timeout: 30000
  },
  test: {
    baseUrl: '',
    timeout: 5000
  }
};

/**
 * 获取当前环境的配置
 */
export function getCurrentEnvConfig() {
  const env = import.meta.env?.MODE || 'development';
  return ENV_CONFIG[env] || ENV_CONFIG.development;
}