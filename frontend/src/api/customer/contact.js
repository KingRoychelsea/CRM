import request from '../axios'

// 联系人管理API
const contactApi = {
  // 获取联系人列表
  getContactList: (params) => {
    return request({
      url: '/customer/contact/list',
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
  updateContact: (data) => {
    return request({
      url: '/customer/contact',
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
  },
  
  // 批量删除联系人
  batchDeleteContact: (ids) => {
    return request({
      url: '/customer/contact/batch',
      method: 'delete',
      data: ids
    })
  },
  
  // 更新联系人状态
  updateContactStatus: (id, status) => {
    return request({
      url: `/customer/contact/status/${id}`,
      method: 'put',
      params: { status }
    })
  },
  
  // 根据客户ID获取联系人列表
  getContactListByCustomerId: (customerId) => {
    return request({
      url: `/customer/contact/byCustomer/${customerId}`,
      method: 'get'
    })
  },
  
  // 获取联系人下拉列表
  getContactSelect: (customerId) => {
    return request({
      url: '/customer/contact/select',
      method: 'get',
      params: { customerId }
    })
  }
}

export default contactApi