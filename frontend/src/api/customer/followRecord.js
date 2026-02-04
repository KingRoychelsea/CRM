import request from '../axios'

// 跟进记录管理API
const followRecordApi = {
  // 获取跟进记录列表
  getFollowRecordList: (customerId, params) => {
    return request({
      url: `/customer/follow/list/${customerId}`,
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
  updateFollowRecord: (id, data) => {
    return request({
      url: `/customer/follow/${id}`,
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
  }
}

export default followRecordApi