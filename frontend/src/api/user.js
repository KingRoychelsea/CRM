import service from './axios'

// 登录
export function login(data) {
  return service({
    url: '/auth/login',
    method: 'post',
    data
  })
}

// 登出
export function logout() {
  return service({
    url: '/auth/logout',
    method: 'post'
  })
}

// 获取用户信息
export function getInfo() {
  return service({
    url: '/auth/info',
    method: 'get'
  })
}

// 刷新token
export function refreshToken() {
  return service({
    url: '/auth/refresh',
    method: 'post'
  })
}

// 重置密码
export function resetPassword(data) {
  return service({
    url: '/system/user/resetPassword',
    method: 'put',
    data
  })
}

// 修改密码
export function changePassword(data) {
  return service({
    url: '/system/user/changePassword',
    method: 'put',
    data
  })
}
