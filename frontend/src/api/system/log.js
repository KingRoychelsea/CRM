import request from '../axios'
import { API_CONFIG } from '../../config/api.config'

// 系统日志API
const logApi = {
  // 获取登录日志列表
  getLoginLogList: (params) => {
    return request({
      url: API_CONFIG.SYSTEM.LOG.LOGIN_LIST,
      method: 'get',
      params
    })
  },
  
  // 删除登录日志
  deleteLoginLog: (id) => {
    return request({
      url: `/api/sys/log/login/${id}`,
      method: 'delete'
    })
  },
  
  // 清空登录日志
  clearLoginLog: () => {
    return request({
      url: '/api/sys/log/login/clear',
      method: 'delete'
    })
  },
  
  // 获取操作日志列表
  getOperLogList: (params) => {
    return request({
      url: API_CONFIG.SYSTEM.LOG.OPERATION_LIST,
      method: 'get',
      params
    })
  },
  
  // 删除操作日志
  deleteOperLog: (id) => {
    return request({
      url: `/api/sys/log/oper/${id}`,
      method: 'delete'
    })
  },
  
  // 清空操作日志
  clearOperLog: () => {
    return request({
      url: '/api/sys/log/oper/clear',
      method: 'delete'
    })
  }
}

export default logApi