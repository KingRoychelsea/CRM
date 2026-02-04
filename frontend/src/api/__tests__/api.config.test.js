import { API_CONFIG, getApiPath, getApiPathWithId, getApiPathWithIdAndSubPath } from '../config/api.config'

/**
 * API路径验证测试
 * 验证前端API路径配置的正确性
 */
describe('API Path Configuration', () => {
  
  test('API路径配置完整性', () => {
    expect(API_CONFIG).toBeDefined()
    expect(API_CONFIG.AUTH).toBeDefined()
    expect(API_CONFIG.SYSTEM).toBeDefined()
    expect(API_CONFIG.CUSTOMER).toBeDefined()
    expect(API_CONFIG.SALES).toBeDefined()
    expect(API_CONFIG.STATS).toBeDefined()
    expect(API_CONFIG.HEALTH).toBeDefined()
  })

  test('认证相关API路径', () => {
    expect(API_CONFIG.AUTH.LOGIN).toBe('/api/auth/login')
    expect(API_CONFIG.AUTH.LOGOUT).toBe('/api/auth/logout')
    expect(API_CONFIG.AUTH.REFRESH).toBe('/api/auth/refresh')
  })

  test('系统管理API路径', () => {
    expect(API_CONFIG.SYSTEM.USER.LIST).toBe('/api/sys/user/list')
    expect(API_CONFIG.SYSTEM.USER.CREATE).toBe('/api/sys/user')
    expect(API_CONFIG.SYSTEM.ROLE.LIST).toBe('/api/sys/role/list')
    expect(API_CONFIG.SYSTEM.DEPT.TREE).toBe('/api/sys/dept/tree')
    expect(API_CONFIG.SYSTEM.MENU.TREE).toBe('/api/sys/menu/tree')
    expect(API_CONFIG.SYSTEM.DICT.LIST).toBe('/api/sys/dict/list')
  })

  test('客户管理API路径', () => {
    expect(API_CONFIG.CUSTOMER.LIST).toBe('/api/customer/list')
    expect(API_CONFIG.CUSTOMER.CREATE).toBe('/api/customer')
  })

  test('销售管理API路径', () => {
    expect(API_CONFIG.SALES.OPPORTUNITY.LIST).toBe('/api/sales/opportunity/list')
    expect(API_CONFIG.SALES.CONTRACT.LIST).toBe('/api/sales/contract/list')
    expect(API_CONFIG.SALES.ORDER.LIST).toBe('/api/sales/order/list')
    expect(API_CONFIG.SALES.PAYMENT.LIST).toBe('/api/sales/payment/list')
  })

  test('统计报表API路径', () => {
    expect(API_CONFIG.STATS.DASHBOARD).toBe('/api/stats/dashboard')
    expect(API_CONFIG.STATS.CUSTOMER_STATS).toBe('/api/stats/customer')
    expect(API_CONFIG.STATS.SALES_STATS).toBe('/api/stats/sales')
  })

  test('健康检查API路径', () => {
    expect(API_CONFIG.HEALTH.STATUS).toBe('/api/health/status')
    expect(API_CONFIG.HEALTH.ENDPOINTS).toBe('/api/health/endpoints')
  })

  test('路径构建工具函数', () => {
    // 测试获取完整路径
    const fullPath = getApiPath('/api/user', 'list')
    expect(fullPath).toBe('/api/user/list')
    
    // 测试获取带ID的路径
    const pathWithId = getApiPathWithId('/api/user', 1)
    expect(pathWithId).toBe('/api/user/1')
    
    // 测试获取带ID和子路径的路径
    const pathWithIdAndSubPath = getApiPathWithIdAndSubPath('/api/user', 1, 'roles')
    expect(pathWithIdAndSubPath).toBe('/api/user/1/roles')
  })

  test('路径格式验证', () => {
    // 验证所有路径都以/api开头
    const validatePath = (path) => {
      expect(path).toMatch(/^\/api/)
    }
    
    // 验证认证路径
    Object.values(API_CONFIG.AUTH).forEach(validatePath)
    
    // 验证系统管理路径
    Object.values(API_CONFIG.SYSTEM.USER).forEach(validatePath)
    Object.values(API_CONFIG.SYSTEM.ROLE).forEach(validatePath)
    Object.values(API_CONFIG.SYSTEM.DEPT).forEach(validatePath)
    Object.values(API_CONFIG.SYSTEM.MENU).forEach(validatePath)
    Object.values(API_CONFIG.SYSTEM.DICT).forEach(validatePath)
    
    // 验证客户管理路径
    Object.values(API_CONFIG.CUSTOMER).forEach(validatePath)
    
    // 验证销售管理路径
    Object.values(API_CONFIG.SALES.OPPORTUNITY).forEach(validatePath)
    Object.values(API_CONFIG.SALES.CONTRACT).forEach(validatePath)
    Object.values(API_CONFIG.SALES.ORDER).forEach(validatePath)
    Object.values(API_CONFIG.SALES.PAYMENT).forEach(validatePath)
    
    // 验证统计路径
    Object.values(API_CONFIG.STATS).forEach(validatePath)
    
    // 验证健康检查路径
    Object.values(API_CONFIG.HEALTH).forEach(validatePath)
  })
})