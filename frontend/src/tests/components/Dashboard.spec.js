import { describe, it, expect, vi, beforeEach } from 'vitest'
import { mount } from '@vue/test-utils'
import Dashboard from '../../pages/dashboard/index.vue'
import { useRouter } from 'vue-router'

// 模拟路由
vi.mock('vue-router', () => ({
  useRouter: vi.fn(() => ({
    push: vi.fn()
  })),
  createRouter: vi.fn(),
  createWebHistory: vi.fn()
}))

describe('Dashboard Component', () => {
  let wrapper
  let router

  beforeEach(() => {
    router = {
      push: vi.fn()
    }

    useRouter.mockReturnValue(router)

    wrapper = mount(Dashboard)
  })

  it('renders dashboard correctly', () => {
    expect(wrapper.find('h2').text()).toBe('欢迎使用CRM系统')
    expect(wrapper.find('.welcome-content p').text()).toBe('这是CRM系统的首页，您可以在这里查看系统概览和关键数据。')
  })

  it('has navigation buttons', () => {
    expect(wrapper.find('el-button').at(0).text()).toBe('客户管理')
    expect(wrapper.find('el-button').at(1).text()).toBe('商机管理')
  })

  it('navigates to customer page when customer button is clicked', async () => {
    const customerButton = wrapper.find('el-button').at(0)
    await customerButton.trigger('click')
    
    expect(router.push).toHaveBeenCalledWith('/customer')
  })

  it('navigates to opportunity page when opportunity button is clicked', async () => {
    const opportunityButton = wrapper.find('el-button').at(1)
    await opportunityButton.trigger('click')
    
    expect(router.push).toHaveBeenCalledWith('/sales/opportunity')
  })

  it('renders card layout correctly', () => {
    expect(wrapper.find('.dashboard').exists()).toBe(true)
    expect(wrapper.find('.welcome-card').exists()).toBe(true)
    expect(wrapper.find('.card-header').exists()).toBe(true)
  })
})
