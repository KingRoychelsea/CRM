import { describe, it, expect, vi, beforeEach } from 'vitest'
import { mount } from '@vue/test-utils'
import UserManagement from '../../pages/system/user/index.vue'
import { useRouter } from 'vue-router'

// 模拟路由
vi.mock('vue-router', () => ({
  useRouter: vi.fn(() => ({
    push: vi.fn()
  })),
  createRouter: vi.fn(),
  createWebHistory: vi.fn()
}))

// 模拟API
vi.mock('../../api/user', () => ({
  getUserList: vi.fn(() => Promise.resolve({
    code: 200,
    data: [
      {
        id: 1,
        username: 'admin',
        realName: '管理员',
        email: 'admin@example.com',
        phone: '13800138000',
        status: 1
      }
    ]
  })),
  addUser: vi.fn(() => Promise.resolve({ code: 200 })),
  updateUser: vi.fn(() => Promise.resolve({ code: 200 })),
  deleteUser: vi.fn(() => Promise.resolve({ code: 200 }))
}))

describe('UserManagement Component', () => {
  let wrapper
  let router

  beforeEach(() => {
    router = {
      push: vi.fn()
    }

    useRouter.mockReturnValue(router)

    wrapper = mount(UserManagement)
  })

  it('renders user management page correctly', () => {
    expect(wrapper.find('h2').text()).toBe('用户管理')
    expect(wrapper.find('el-button').text()).toBe('添加用户')
    expect(wrapper.find('el-table').exists()).toBe(true)
  })

  it('loads user list on mount', async () => {
    // 等待组件挂载和数据加载
    await wrapper.vm.$nextTick()
    
    // 验证用户列表是否加载
    expect(wrapper.vm.users).toBeDefined()
    expect(wrapper.vm.users.length).toBeGreaterThan(0)
  })

  it('opens add user dialog when add button is clicked', async () => {
    const addButton = wrapper.find('el-button')
    await addButton.trigger('click')
    
    expect(wrapper.vm.dialogVisible).toBe(true)
    expect(wrapper.find('el-dialog').exists()).toBe(true)
  })

  it('validates add user form', async () => {
    // 打开添加对话框
    const addButton = wrapper.find('el-button')
    await addButton.trigger('click')
    
    // 提交空表单
    const submitButton = wrapper.find('el-button').at(1)
    await submitButton.trigger('click')
    
    // 应该显示验证错误
    expect(wrapper.find('.el-form-item__error').exists()).toBe(true)
  })

  it('closes dialog when cancel button is clicked', async () => {
    // 打开添加对话框
    const addButton = wrapper.find('el-button')
    await addButton.trigger('click')
    
    // 点击取消按钮
    const cancelButton = wrapper.find('el-button').at(2)
    await cancelButton.trigger('click')
    
    expect(wrapper.vm.dialogVisible).toBe(false)
  })

  it('renders user table with correct columns', () => {
    expect(wrapper.find('el-table-column').at(0).attributes('prop')).toBe('username')
    expect(wrapper.find('el-table-column').at(1).attributes('prop')).toBe('realName')
    expect(wrapper.find('el-table-column').at(2).attributes('prop')).toBe('email')
    expect(wrapper.find('el-table-column').at(3).attributes('prop')).toBe('phone')
    expect(wrapper.find('el-table-column').at(4).attributes('prop')).toBe('status')
  })
})
