import request from '../axios'
import { API_CONFIG } from '../../config/api.config'

// 商机管理API
const businessOpportunityApi = {
  // 获取商机列表
  getBusinessOpportunityList: (params) => {
    return request({
      url: API_CONFIG.SALES.OPPORTUNITY.LIST,
      method: 'get',
      params
    })
  },
  
  // 获取商机详情
  getBusinessOpportunityInfo: (id) => {
    return request({
      url: `${API_CONFIG.SALES.OPPORTUNITY.DETAIL}/${id}`,
      method: 'get'
    })
  },
  
  // 创建商机
  createBusinessOpportunity: (data) => {
    return request({
      url: API_CONFIG.SALES.OPPORTUNITY.CREATE,
      method: 'post',
      data
    })
  },
  
  // 更新商机
  updateBusinessOpportunity: (id, data) => {
    return request({
      url: `${API_CONFIG.SALES.OPPORTUNITY.UPDATE}/${id}`,
      method: 'put',
      data
    })
  },
  
  // 删除商机
  deleteBusinessOpportunity: (id) => {
    return request({
      url: `${API_CONFIG.SALES.OPPORTUNITY.DELETE}/${id}`,
      method: 'delete'
    })
  }
}

export default businessOpportunityApi