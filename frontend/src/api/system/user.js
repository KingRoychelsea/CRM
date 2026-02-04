import request from '../axios'
import { API_CONFIG } from '../../config/api.config'

// 用户管理API
const userApi = {
  // 获取用户列表
  getUserList: (params) => {
    return request({
      url: API_CONFIG.SYSTEM.USER.LIST,
      method: 'get',
      params
    })
  },
  
  // 获取用户详情
  getUserInfo: (id) => {
    return request({
      url: `${API_CONFIG.SYSTEM.USER.DETAIL}/${id}`,
      method: 'get'
    })
  },
  
  // 创建用户
  createUser: (data) => {
    return request({
      url: API_CONFIG.SYSTEM.USER.CREATE,
      method: 'post',
      data
    })
  },
  
  // 更新用户
  updateUser: (id, data) => {
    return request({
      url: `${API_CONFIG.SYSTEM.USER.UPDATE}/${id}`,
      method: 'put',
      data
    })
  },
  
  // 删除用户
  deleteUser: (id) => {
    return request({
      url: `${API_CONFIG.SYSTEM.USER.DELETE}/${id}`,
      method: 'delete'
    })
  },
  
  // 重置密码
  resetPassword: (id, data) => {
    return request({
      url: `${API_CONFIG.SYSTEM.USER.RESET_PASSWORD}/${id}`,
      method: 'put',
      data
    })
  },
  
  // 获取部门树
  getDeptTree: () => {
    return request({
      url: API_CONFIG.SYSTEM.DEPT.TREE,
      method: 'get'
    })
  },
  
  // 获取角色列表
  getRoleList: () => {
    return request({
      url: API_CONFIG.SYSTEM.ROLE.LIST,
      method: 'get'
    })
  }
}

export default userApi