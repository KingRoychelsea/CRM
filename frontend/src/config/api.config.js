/**
 * API路径配置
 * 前端API路径集中管理配置
 */
export const API_CONFIG = {
  // 基础配置
  BASE_URL: '/api',
  TIMEOUT: 10000,
  
  // 认证相关
  AUTH: {
    LOGIN: '/auth/login',
    LOGOUT: '/auth/logout',
    REFRESH: '/auth/refresh'
  },
  
  // 系统管理
  SYSTEM: {
    // 用户管理
    USER: {
      LIST: '/sys/user/list',
      DETAIL: '/sys/user',
      CREATE: '/sys/user',
      UPDATE: '/sys/user',
      DELETE: '/sys/user',
      BATCH_DELETE: '/sys/user/batch',
      RESET_PASSWORD: '/sys/user/resetPassword',
      UPDATE_STATUS: '/sys/user/status',
      ROLES: '/sys/user/roles',
      ASSIGN_ROLES: '/sys/user/roles'
    },
    
    // 角色管理
    ROLE: {
      LIST: '/sys/role/list',
      DETAIL: '/sys/role',
      CREATE: '/sys/role',
      UPDATE: '/sys/role',
      DELETE: '/sys/role',
      MENUS: '/sys/role/menu',
      ASSIGN_MENUS: '/sys/role/menu'
    },
    
    // 部门管理
    DEPT: {
      LIST: '/sys/dept/list',
      TREE: '/sys/dept/tree',
      DETAIL: '/sys/dept',
      CREATE: '/sys/dept',
      UPDATE: '/sys/dept',
      DELETE: '/sys/dept'
    },
    
    // 菜单管理
    MENU: {
      LIST: '/sys/menu/list',
      TREE: '/sys/menu/tree',
      DETAIL: '/sys/menu',
      CREATE: '/sys/menu',
      UPDATE: '/sys/menu',
      DELETE: '/sys/menu'
    },
    
    // 字典管理
    DICT: {
      LIST: '/sys/dict/type/list',
      DETAIL: '/sys/dict/type',
      CREATE: '/sys/dict/type',
      UPDATE: '/sys/dict/type',
      DELETE: '/sys/dict/type',
      ITEMS: '/sys/dict/item'
    },
    
    // 日志管理
    LOG: {
      LOGIN_LIST: '/sys/log/login/list',
      OPERATION_LIST: '/sys/log/oper/list',
      ERROR_LIST: '/sys/log/error'
    }
  },
  
  // 客户管理
  CUSTOMER: {
    LIST: '/customer/list',
    DETAIL: '/customer',
    CREATE: '/customer',
    UPDATE: '/customer',
    DELETE: '/customer',
    BATCH_DELETE: '/customer/batch',
    CONTACTS: '/customer/contacts',
    FOLLOW_RECORDS: '/customer/follow-records'
  },
  
  // 销售管理
  SALES: {
    // 商机管理
    OPPORTUNITY: {
      LIST: '/sales/opportunity/list',
      DETAIL: '/sales/opportunity',
      CREATE: '/sales/opportunity',
      UPDATE: '/sales/opportunity',
      DELETE: '/sales/opportunity'
    },
    
    // 合同管理
    CONTRACT: {
      LIST: '/sales/contract/list',
      DETAIL: '/sales/contract',
      CREATE: '/sales/contract',
      UPDATE: '/sales/contract',
      DELETE: '/sales/contract'
    },
    
    // 订单管理
    ORDER: {
      LIST: '/sales/order/list',
      DETAIL: '/sales/order',
      CREATE: '/sales/order',
      UPDATE: '/sales/order',
      DELETE: '/sales/order'
    },
    
    // 回款管理
    PAYMENT: {
      LIST: '/sales/payment/list',
      DETAIL: '/sales/payment',
      CREATE: '/sales/payment',
      UPDATE: '/sales/payment',
      DELETE: '/sales/payment'
    }
  },
  
  // 统计报表
  STATS: {
    DASHBOARD: '/stats/dashboard',
    CUSTOMER_STATS: '/stats/customer',
    SALES_STATS: '/stats/sales',
    PERFORMANCE_STATS: '/stats/performance'
  },
  
  // 健康检查
  HEALTH: {
    STATUS: '/health/status',
    ENDPOINTS: '/health/endpoints'
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
    baseUrl: '/api',
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
  // 强制使用开发环境配置，确保前端使用正确的baseUrl
  return ENV_CONFIG.development;
}