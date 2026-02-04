import request from '../axios'

// 订单管理API
const orderApi = {
  // 获取订单列表
  getOrderList: (params) => {
    return request({
      url: '/api/sales/order/list',
      method: 'get',
      params
    })
  },
  
  // 获取订单详情
  getOrderInfo: (id) => {
    return request({
      url: `/api/sales/order/${id}`,
      method: 'get'
    })
  },
  
  // 创建订单
  createOrder: (data) => {
    return request({
      url: '/api/sales/order',
      method: 'post',
      data
    })
  },
  
  // 更新订单
  updateOrder: (id, data) => {
    return request({
      url: `/api/sales/order/${id}`,
      method: 'put',
      data
    })
  },
  
  // 删除订单
  deleteOrder: (id) => {
    return request({
      url: `/api/sales/order/${id}`,
      method: 'delete'
    })
  }
}

export default orderApi