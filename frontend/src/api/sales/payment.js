import request from '../axios'
import { API_CONFIG } from '../../config/api.config'

// 回款管理API
const paymentApi = {
  // 获取回款列表
  getPaymentList: (params) => {
    return request({
      url: API_CONFIG.SALES.PAYMENT.LIST,
      method: 'get',
      params
    })
  },
  
  // 获取回款详情
  getPaymentInfo: (id) => {
    return request({
      url: `${API_CONFIG.SALES.PAYMENT.DETAIL}/${id}`,
      method: 'get'
    })
  },
  
  // 创建回款
  createPayment: (data) => {
    return request({
      url: API_CONFIG.SALES.PAYMENT.CREATE,
      method: 'post',
      data
    })
  },
  
  // 更新回款
  updatePayment: (id, data) => {
    return request({
      url: `${API_CONFIG.SALES.PAYMENT.UPDATE}/${id}`,
      method: 'put',
      data
    })
  },
  
  // 删除回款
  deletePayment: (id) => {
    return request({
      url: `${API_CONFIG.SALES.PAYMENT.DELETE}/${id}`,
      method: 'delete'
    })
  }
}

export default paymentApi