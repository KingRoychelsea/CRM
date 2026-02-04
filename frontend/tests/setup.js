// 测试环境设置
import { vi } from 'vitest'

// 模拟全局对象
global.document = {
  title: '',
  createElement: vi.fn(),
  getElementById: vi.fn(),
  querySelector: vi.fn(),
  querySelectorAll: vi.fn()
}

global.window = {
  localStorage: {
    getItem: vi.fn(),
    setItem: vi.fn(),
    removeItem: vi.fn()
  },
  location: {
    href: '',
    pathname: ''
  }
}

// 模拟console
if (!global.console) {
  global.console = {
    log: vi.fn(),
    error: vi.fn(),
    warn: vi.fn(),
    info: vi.fn()
  }
}
