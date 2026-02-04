import request from '../axios'
import { API_CONFIG } from '../../config/api.config'

// 合同管理API
const contractApi = {
  // 获取合同列表
  getContractList: (params) => {
    return request({
      url: API_CONFIG.SALES.CONTRACT.LIST,
      method: 'get',
      params
    })
  },
  
  // 获取合同详情
  getContractInfo: (id) => {
    return request({
      url: `${API_CONFIG.SALES.CONTRACT.DETAIL}/${id}`,
      method: 'get'
    })
  },
  
  // 创建合同
  createContract: (data) => {
    return request({
      url: API_CONFIG.SALES.CONTRACT.CREATE,
      method: 'post',
      data
    })
  },
  
  // 更新合同
  updateContract: (id, data) => {
    return request({
      url: `${API_CONFIG.SALES.CONTRACT.UPDATE}/${id}`,
      method: 'put',
      data
    })
  },
  
  // 删除合同
  deleteContract: (id) => {
    return request({
      url: `${API_CONFIG.SALES.CONTRACT.DELETE}/${id}`,
      method: 'delete'
    })
  }
}

export default contractApi