import request from '../axios'

// 联系人管理API
const contactApi = {
  // 获取联系人列表
  getContactList: (customerId, params) => {
    return request({
      url: `/customer/contact/list/${customerId}`,
      method: 'get',
      params
    })
  },
  
  // 获取联系人详情
  getContactInfo: (id) => {
    return request({
      url: `/customer/contact/${id}`,
      method: 'get'
    })
  },
  
  // 创建联系人
  createContact: (data) => {
    return request({
      url: '/customer/contact',
      method: 'post',
      data
    })
  },
  
  // 更新联系人
  updateContact: (id, data) => {
    return request({
      url: `/customer/contact/${id}`,
      method: 'put',
      data
    })
  },
  
  // 删除联系人
  deleteContact: (id) => {
    return request({
      url: `/customer/contact/${id}`,
      method: 'delete'
    })
  }
}

export default contactApi