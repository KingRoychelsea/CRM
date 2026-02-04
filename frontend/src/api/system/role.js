import request from '../axios'
import { API_CONFIG } from '../../config/api.config'

// 角色管理API
const roleApi = {
  // 获取角色列表
  getRoleList: (params) => {
    return request({
      url: API_CONFIG.SYSTEM.ROLE.LIST,
      method: 'get',
      params
    })
  },
  
  // 获取角色详情
  getRoleInfo: (id) => {
    return request({
      url: `${API_CONFIG.SYSTEM.ROLE.DETAIL}/${id}`,
      method: 'get'
    })
  },
  
  // 创建角色
  createRole: (data) => {
    return request({
      url: API_CONFIG.SYSTEM.ROLE.CREATE,
      method: 'post',
      data
    })
  },
  
  // 更新角色
  updateRole: (id, data) => {
    return request({
      url: `${API_CONFIG.SYSTEM.ROLE.UPDATE}/${id}`,
      method: 'put',
      data
    })
  },
  
  // 删除角色
  deleteRole: (id) => {
    return request({
      url: `${API_CONFIG.SYSTEM.ROLE.DELETE}/${id}`,
      method: 'delete'
    })
  },
  
  // 获取菜单树（用于权限分配）
  getMenuTree: () => {
    return request({
      url: API_CONFIG.SYSTEM.MENU.TREE,
      method: 'get'
    })
  },
  
  // 分配菜单权限
  assignMenus: (id, data) => {
    return request({
      url: `${API_CONFIG.SYSTEM.ROLE.MENUS}/${id}`,
      method: 'put',
      data
    })
  }
}

export default roleApi