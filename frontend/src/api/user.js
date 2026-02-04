import service from './axios'

// 登录
export function login(data) {
  return service({
    url: '/api/auth/login',
    method: 'post',
    data
  })
}

// 登出
export function logout() {
  return service({
    url: '/api/auth/logout',
    method: 'post'
  })
}

// 获取用户信息
export function getInfo() {
  return service({
    url: '/api/auth/info',
    method: 'get'
  })
}

// 刷新token
export function refreshToken() {
  return service({
    url: '/api/auth/refresh',
    method: 'post'
  })
}

// 重置密码
export function resetPassword(data) {
  return service({
    url: '/api/sys/user/resetPassword',
    method: 'put',
    data
  })
}

// 修改密码
export function changePassword(data) {
  return service({
    url: '/api/sys/user/changePassword',
    method: 'put',
    data
  })
}
