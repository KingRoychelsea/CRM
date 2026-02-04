import request from '../axios'
import { API_CONFIG } from '../../config/api.config'

// 系统日志API
const logApi = {
  // 获取登录日志列表
  getLoginLogList: (params) => {
    // 调整参数名称以匹配后端
    const adjustedParams = {
      loginUser: params.username,
      status: params.status,
      loginIp: params.ipaddr,
      startTime: params.startTime,
      endTime: params.endTime,
      pageNum: params.page,
      pageSize: params.pageSize
    }
    return request({
      url: API_CONFIG.SYSTEM.LOG.LOGIN_LIST,
      method: 'get',
      params: adjustedParams
    })
  },
  
  // 删除登录日志
  deleteLoginLog: (id) => {
    return request({
      url: `/sys/log/login/${id}`,
      method: 'delete'
    })
  },
  
  // 清空登录日志
  clearLoginLog: () => {
    return request({
      url: '/sys/log/login/clear',
      method: 'delete'
    })
  },
  
  // 获取操作日志列表
  getOperLogList: (params) => {
    // 调整参数名称以匹配后端
    const adjustedParams = {
      operUser: params.username,
      operType: params.operType,
      operModule: params.title,
      startTime: params.startTime,
      endTime: params.endTime,
      pageNum: params.page,
      pageSize: params.pageSize
    }
    return request({
      url: API_CONFIG.SYSTEM.LOG.OPERATION_LIST,
      method: 'get',
      params: adjustedParams
    })
  },
  
  // 删除操作日志
  deleteOperLog: (id) => {
    return request({
      url: `/sys/log/oper/${id}`,
      method: 'delete'
    })
  },
  
  // 清空操作日志
  clearOperLog: () => {
    return request({
      url: '/sys/log/oper/clear',
      method: 'delete'
    })
  }
}

export default logApi