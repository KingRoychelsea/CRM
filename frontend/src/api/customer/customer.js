import request from '../axios'

// 客户管理API
const customerApi = {
  // 获取客户列表
  getCustomerList: (params) => {
    return request({
      url: '/api/customer/list',
      method: 'get',
      params
    })
  },
  
  // 获取客户详情
  getCustomerInfo: (id) => {
    return request({
      url: `/api/customer/${id}`,
      method: 'get'
    })
  },
  
  // 创建客户
  createCustomer: (data) => {
    return request({
      url: '/api/customer',
      method: 'post',
      data
    })
  },
  
  // 更新客户
  updateCustomer: (id, data) => {
    return request({
      url: `/api/customer/${id}`,
      method: 'put',
      data
    })
  },
  
  // 删除客户
  deleteCustomer: (id) => {
    return request({
      url: `/api/customer/${id}`,
      method: 'delete'
    })
  },
  
  // 批量删除客户
  batchDeleteCustomer: (ids) => {
    return request({
      url: '/api/customer/batchDelete',
      method: 'delete',
      data: { ids }
    })
  }
}

export default customerApi