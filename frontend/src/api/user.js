import request from './axios'
import { API_CONFIG } from '../config/api.config'

// 登录
export function login(data) {
  return request({
    url: API_CONFIG.AUTH.LOGIN,
    method: 'post',
    data
  })
}

// 登出
export function logout() {
  return request({
    url: API_CONFIG.AUTH.LOGOUT,
    method: 'post'
  })
}

// 获取用户信息
export function getInfo() {
  return request({
    url: API_CONFIG.AUTH.LOGIN.replace('/login', '/info'),
    method: 'get'
  })
}

// 刷新token
export function refreshToken() {
  return request({
    url: API_CONFIG.AUTH.REFRESH,
    method: 'post'
  })
}

// 重置密码
export function resetPassword(id) {
  return request({
    url: `${API_CONFIG.SYSTEM.USER.RESET_PASSWORD}/${id}`,
    method: 'put'
  })
}

// 修改密码
export function changePassword(data) {
  return request({
    url: '/sys/user/changePassword',
    method: 'put',
    data
  })
}
