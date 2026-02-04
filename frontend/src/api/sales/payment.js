import request from '../axios'

// 回款管理API
const paymentApi = {
  // 获取回款列表
  getPaymentList: (params) => {
    return request({
      url: '/api/sales/payment/list',
      method: 'get',
      params
    })
  },
  
  // 获取回款详情
  getPaymentInfo: (id) => {
    return request({
      url: `/api/sales/payment/${id}`,
      method: 'get'
    })
  },
  
  // 创建回款
  createPayment: (data) => {
    return request({
      url: '/api/sales/payment',
      method: 'post',
      data
    })
  },
  
  // 更新回款
  updatePayment: (id, data) => {
    return request({
      url: `/api/sales/payment/${id}`,
      method: 'put',
      data
    })
  },
  
  // 删除回款
  deletePayment: (id) => {
    return request({
      url: `/api/sales/payment/${id}`,
      method: 'delete'
    })
  }
}

export default paymentApi