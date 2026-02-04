import request from '../axios'

// 跟进记录管理API
const followRecordApi = {
  // 获取跟进记录列表
  getFollowRecordList: (params) => {
    return request({
      url: '/customer/follow/list',
      method: 'get',
      params
    })
  },
  
  // 获取跟进记录详情
  getFollowRecordInfo: (id) => {
    return request({
      url: `/customer/follow/${id}`,
      method: 'get'
    })
  },
  
  // 创建跟进记录
  createFollowRecord: (data) => {
    return request({
      url: '/customer/follow',
      method: 'post',
      data
    })
  },
  
  // 更新跟进记录
  updateFollowRecord: (data) => {
    return request({
      url: '/customer/follow',
      method: 'put',
      data
    })
  },
  
  // 删除跟进记录
  deleteFollowRecord: (id) => {
    return request({
      url: `/customer/follow/${id}`,
      method: 'delete'
    })
  },
  
  // 批量删除跟进记录
  batchDeleteFollowRecord: (ids) => {
    return request({
      url: '/customer/follow/batch',
      method: 'delete',
      data: ids
    })
  },
  
  // 根据客户ID获取跟进记录列表
  getFollowRecordListByCustomerId: (customerId) => {
    return request({
      url: `/customer/follow/byCustomer/${customerId}`,
      method: 'get'
    })
  },
  
  // 获取待跟进提醒列表
  getFollowRemind: () => {
    return request({
      url: '/customer/follow/remind',
      method: 'get'
    })
  }
}

export default followRecordApi