import request from '../axios'

// 用户管理API
const userApi = {
  // 获取用户列表
  getUserList: (params) => {
    return request({
      url: '/api/sys/user/list',
      method: 'get',
      params
    })
  },
  
  // 获取用户详情
  getUserInfo: (id) => {
    return request({
      url: `/api/sys/user/${id}`,
      method: 'get'
    })
  },
  
  // 创建用户
  createUser: (data) => {
    return request({
      url: '/api/sys/user',
      method: 'post',
      data
    })
  },
  
  // 更新用户
  updateUser: (id, data) => {
    return request({
      url: `/api/sys/user/${id}`,
      method: 'put',
      data
    })
  },
  
  // 删除用户
  deleteUser: (id) => {
    return request({
      url: `/api/sys/user/${id}`,
      method: 'delete'
    })
  },
  
  // 重置密码
  resetPassword: (id, data) => {
    return request({
      url: `/api/sys/user/resetPassword/${id}`,
      method: 'put',
      data
    })
  },
  
  // 获取部门树
  getDeptTree: () => {
    return request({
      url: '/api/sys/dept/tree',
      method: 'get'
    })
  },
  
  // 获取角色列表
  getRoleList: () => {
    return request({
      url: '/api/sys/role/list',
      method: 'get'
    })
  }
}

export default userApi