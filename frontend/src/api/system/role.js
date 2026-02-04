import request from '../axios'

// 角色管理API
const roleApi = {
  // 获取角色列表
  getRoleList: (params) => {
    return request({
      url: '/api/sys/role/list',
      method: 'get',
      params
    })
  },
  
  // 获取角色详情
  getRoleInfo: (id) => {
    return request({
      url: `/api/sys/role/${id}`,
      method: 'get'
    })
  },
  
  // 创建角色
  createRole: (data) => {
    return request({
      url: '/api/sys/role',
      method: 'post',
      data
    })
  },
  
  // 更新角色
  updateRole: (id, data) => {
    return request({
      url: `/api/sys/role/${id}`,
      method: 'put',
      data
    })
  },
  
  // 删除角色
  deleteRole: (id) => {
    return request({
      url: `/api/sys/role/${id}`,
      method: 'delete'
    })
  },
  
  // 获取菜单树（用于权限分配）
  getMenuTree: () => {
    return request({
      url: '/api/sys/menu/tree',
      method: 'get'
    })
  },
  
  // 分配菜单权限
  assignMenus: (id, data) => {
    return request({
      url: `/api/sys/role/assignMenus/${id}`,
      method: 'put',
      data
    })
  }
}

export default roleApi