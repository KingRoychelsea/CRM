import request from '../axios'
import { API_CONFIG } from '../../config/api.config'

// 部门管理API
const deptApi = {
  // 获取部门列表
  getDeptList: (params) => {
    return request({
      url: API_CONFIG.SYSTEM.DEPT.LIST,
      method: 'get',
      params
    })
  },
  
  // 获取部门树
  getDeptTree: () => {
    return request({
      url: API_CONFIG.SYSTEM.DEPT.TREE,
      method: 'get'
    })
  },
  
  // 获取部门详情
  getDeptInfo: (id) => {
    return request({
      url: `${API_CONFIG.SYSTEM.DEPT.DETAIL}/${id}`,
      method: 'get'
    })
  },
  
  // 创建部门
  createDept: (data) => {
    return request({
      url: API_CONFIG.SYSTEM.DEPT.CREATE,
      method: 'post',
      data
    })
  },
  
  // 更新部门
  updateDept: (id, data) => {
    return request({
      url: `${API_CONFIG.SYSTEM.DEPT.UPDATE}/${id}`,
      method: 'put',
      data
    })
  },
  
  // 删除部门
  deleteDept: (id) => {
    return request({
      url: `${API_CONFIG.SYSTEM.DEPT.DELETE}/${id}`,
      method: 'delete'
    })
  }
}

export default deptApi