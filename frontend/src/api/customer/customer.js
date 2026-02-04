import request from '../axios'
import { API_CONFIG } from '../../config/api.config'

// 客户管理API
const customerApi = {
  // 获取客户列表
  getCustomerList: (params) => {
    return request({
      url: API_CONFIG.CUSTOMER.LIST,
      method: 'get',
      params
    })
  },
  
  // 获取客户详情
  getCustomerInfo: (id) => {
    return request({
      url: `${API_CONFIG.CUSTOMER.DETAIL}/${id}`,
      method: 'get'
    })
  },
  
  // 创建客户
  createCustomer: (data) => {
    return request({
      url: API_CONFIG.CUSTOMER.CREATE,
      method: 'post',
      data
    })
  },
  
  // 更新客户
  updateCustomer: (id, data) => {
    return request({
      url: `${API_CONFIG.CUSTOMER.UPDATE}/${id}`,
      method: 'put',
      data
    })
  },
  
  // 删除客户
  deleteCustomer: (id) => {
    return request({
      url: `${API_CONFIG.CUSTOMER.DELETE}/${id}`,
      method: 'delete'
    })
  },
  
  // 批量删除客户
  batchDeleteCustomer: (ids) => {
    return request({
      url: API_CONFIG.CUSTOMER.BATCH_DELETE,
      method: 'delete',
      data: ids
    })
  }
}

export default customerApi