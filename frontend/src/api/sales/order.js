import request from '../axios'
import { API_CONFIG } from '../../config/api.config'

// 订单管理API
const orderApi = {
  // 获取订单列表
  getOrderList: (params) => {
    return request({
      url: API_CONFIG.SALES.ORDER.LIST,
      method: 'get',
      params
    })
  },
  
  // 获取订单详情
  getOrderInfo: (id) => {
    return request({
      url: `${API_CONFIG.SALES.ORDER.DETAIL}/${id}`,
      method: 'get'
    })
  },
  
  // 创建订单
  createOrder: (data) => {
    return request({
      url: API_CONFIG.SALES.ORDER.CREATE,
      method: 'post',
      data
    })
  },
  
  // 更新订单
  updateOrder: (id, data) => {
    return request({
      url: `${API_CONFIG.SALES.ORDER.UPDATE}/${id}`,
      method: 'put',
      data
    })
  },
  
  // 删除订单
  deleteOrder: (id) => {
    return request({
      url: `${API_CONFIG.SALES.ORDER.DELETE}/${id}`,
      method: 'delete'
    })
  }
}

export default orderApi