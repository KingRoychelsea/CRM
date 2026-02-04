import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import { resolve } from 'path'

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [vue()],
  resolve: {
    alias: {
      '@': resolve(__dirname, 'src')
    }
  },
  server: {
    port: 5173,
    proxy: {
      // 统一的API代理配置
      '^/api': {
        target: 'http://localhost:8080',
        changeOrigin: true,
        // 保留原始路径，不再添加额外的/api前缀
        rewrite: (path) => path,
        // 添加超时配置
        timeout: 30000,
        // 添加重试机制
        onProxyReq: (proxyReq, req, res) => {
          console.log(`[Proxy] ${req.method} ${req.url} -> ${proxyReq.path}`)
        },
        onProxyRes: (proxyRes, req, res) => {
          console.log(`[Proxy] ${proxyRes.statusCode} ${req.url}`)
        },
        onError: (err, req, res) => {
          console.error(`[Proxy Error] ${req.method} ${req.url}: ${err.message}`)
        }
      },
      // 处理直接的/login请求
      '^/login': {
        target: 'http://localhost:8080',
        changeOrigin: true,
        rewrite: (path) => '/api/auth/login',
        timeout: 30000,
        onProxyReq: (proxyReq, req, res) => {
          console.log(`[Proxy] ${req.method} ${req.url} -> ${proxyReq.path}`)
        },
        onProxyRes: (proxyRes, req, res) => {
          console.log(`[Proxy] ${proxyRes.statusCode} ${req.url}`)
        },
        onError: (err, req, res) => {
          console.error(`[Proxy Error] ${req.method} ${req.url}: ${err.message}`)
        }
      }
    }
  },
  
  // 构建配置
  build: {
    // 输出目录
    outDir: 'dist',
    // 静态资源目录
    assetsDir: 'assets',
    // 启用压缩
    minify: 'terser',
    // 启用source map
    sourcemap: true,
    // 代码分割
    rollupOptions: {
      output: {
        manualChunks: {
          'vue-vendor': ['vue', 'vue-router', 'pinia'],
          'element-plus': ['element-plus'],
          'echarts': ['echarts']
        }
      }
    }
  },
  
  // 环境变量配置
  define: {
    __VUE_OPTIONS_API__: true,
    __VUE_PROD_DEVTOOLS__: false
  }
})