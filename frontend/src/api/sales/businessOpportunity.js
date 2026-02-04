import request from '../axios'

// 商机管理API
const businessOpportunityApi = {
  // 获取商机列表
  getBusinessOpportunityList: (params) => {
    return request({
      url: '/api/sales/opportunity/list',
      method: 'get',
      params
    })
  },
  
  // 获取商机详情
  getBusinessOpportunityInfo: (id) => {
    return request({
      url: `/api/sales/opportunity/${id}`,
      method: 'get'
    })
  },
  
  // 创建商机
  createBusinessOpportunity: (data) => {
    return request({
      url: '/api/sales/opportunity',
      method: 'post',
      data
    })
  },
  
  // 更新商机
  updateBusinessOpportunity: (id, data) => {
    return request({
      url: `/api/sales/opportunity/${id}`,
      method: 'put',
      data
    })
  },
  
  // 删除商机
  deleteBusinessOpportunity: (id) => {
    return request({
      url: `/api/sales/opportunity/${id}`,
      method: 'delete'
    })
  }
}

export default businessOpportunityApi