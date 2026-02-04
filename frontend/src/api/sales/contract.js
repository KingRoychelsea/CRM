import request from '../axios'

// 合同管理API
const contractApi = {
  // 获取合同列表
  getContractList: (params) => {
    return request({
      url: '/api/sales/contract/list',
      method: 'get',
      params
    })
  },
  
  // 获取合同详情
  getContractInfo: (id) => {
    return request({
      url: `/api/sales/contract/${id}`,
      method: 'get'
    })
  },
  
  // 创建合同
  createContract: (data) => {
    return request({
      url: '/api/sales/contract',
      method: 'post',
      data
    })
  },
  
  // 更新合同
  updateContract: (id, data) => {
    return request({
      url: `/api/sales/contract/${id}`,
      method: 'put',
      data
    })
  },
  
  // 删除合同
  deleteContract: (id) => {
    return request({
      url: `/api/sales/contract/${id}`,
      method: 'delete'
    })
  }
}

export default contractApi