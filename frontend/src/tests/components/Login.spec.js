import { describe, it, expect, vi, beforeEach } from 'vitest'
import { mount } from '@vue/test-utils'
import Login from '../../pages/login/Login.vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '../../store/user'

// 模拟路由
vi.mock('vue-router', () => ({
  useRouter: vi.fn(() => ({
    push: vi.fn()
  })),
  createRouter: vi.fn(),
  createWebHistory: vi.fn()
}))

// 模拟用户store
vi.mock('../../store/user', () => ({
  useUserStore: vi.fn(() => ({
    login: vi.fn(),
    token: null
  }))
}))

describe('Login Component', () => {
  let wrapper
  let router
  let userStore

  beforeEach(() => {
    router = {
      push: vi.fn()
    }
    userStore = {
      login: vi.fn(),
      token: null
    }

    useRouter.mockReturnValue(router)
    useUserStore.mockReturnValue(userStore)

    wrapper = mount(Login)
  })

  it('renders login form correctly', () => {
    expect(wrapper.find('h1').text()).toBe('CRM系统登录')
    expect(wrapper.find('el-form').exists()).toBe(true)
    expect(wrapper.find('el-form-item').at(0).exists()).toBe(true)
    expect(wrapper.find('el-form-item').at(1).exists()).toBe(true)
    expect(wrapper.find('el-button').text()).toBe('登录')
  })

  it('validates form fields', async () => {
    // 测试空表单提交
    await wrapper.find('el-button').trigger('click')
    
    // 应该显示验证错误
    expect(wrapper.find('.el-form-item__error').exists()).toBe(true)
  })

  it('validates username field', async () => {
    // 测试用户名验证
    const usernameInput = wrapper.find('input[type="text"]')
    await usernameInput.setValue('')
    await usernameInput.trigger('blur')
    
    expect(wrapper.find('.el-form-item__error').text()).toContain('用户名不能为空')
  })

  it('validates password field', async () => {
    // 测试密码验证
    const passwordInput = wrapper.find('input[type="password"]')
    await passwordInput.setValue('')
    await passwordInput.trigger('blur')
    
    expect(wrapper.find('.el-form-item__error').text()).toContain('密码不能为空')
  })

  it('submits login form successfully', async () => {
    // 模拟登录成功
    userStore.login.mockResolvedValue({ success: true })
    userStore.token = 'mock-token'

    // 填写表单
    const usernameInput = wrapper.find('input[type="text"]')
    const passwordInput = wrapper.find('input[type="password"]')
    
    await usernameInput.setValue('admin')
    await passwordInput.setValue('123456')
    
    // 提交表单
    await wrapper.find('el-button').trigger('click')
    
    // 验证登录方法被调用
    expect(userStore.login).toHaveBeenCalledWith({
      username: 'admin',
      password: '123456'
    })
    
    // 验证路由跳转
    expect(router.push).toHaveBeenCalledWith('/')
  })

  it('handles login failure', async () => {
    // 模拟登录失败
    userStore.login.mockRejectedValue(new Error('登录失败'))

    // 填写表单
    const usernameInput = wrapper.find('input[type="text"]')
    const passwordInput = wrapper.find('input[type="password"]')
    
    await usernameInput.setValue('admin')
    await passwordInput.setValue('123456')
    
    // 提交表单
    await wrapper.find('el-button').trigger('click')
    
    // 验证登录方法被调用
    expect(userStore.login).toHaveBeenCalledWith({
      username: 'admin',
      password: '123456'
    })
    
    // 验证路由未跳转
    expect(router.push).not.toHaveBeenCalled()
  })
})
