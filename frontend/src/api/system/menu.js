import request from '../axios'
import { API_CONFIG } from '../../config/api.config'

// 菜单管理API
const menuApi = {
  // 获取菜单列表
  getMenuList: (params) => {
    return request({
      url: API_CONFIG.SYSTEM.MENU.LIST,
      method: 'get',
      params
    })
  },
  
  // 获取菜单树
  getMenuTree: () => {
    return request({
      url: API_CONFIG.SYSTEM.MENU.TREE,
      method: 'get'
    })
  },
  
  // 获取菜单详情
  getMenuInfo: (id) => {
    return request({
      url: `${API_CONFIG.SYSTEM.MENU.DETAIL}/${id}`,
      method: 'get'
    })
  },
  
  // 创建菜单
  createMenu: (data) => {
    return request({
      url: API_CONFIG.SYSTEM.MENU.CREATE,
      method: 'post',
      data
    })
  },
  
  // 更新菜单
  updateMenu: (id, data) => {
    return request({
      url: `${API_CONFIG.SYSTEM.MENU.UPDATE}/${id}`,
      method: 'put',
      data
    })
  },
  
  // 删除菜单
  deleteMenu: (id) => {
    return request({
      url: `${API_CONFIG.SYSTEM.MENU.DELETE}/${id}`,
      method: 'delete'
    })
  }
}

export default menuApi