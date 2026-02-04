<template>
  <div class="layout-container">
    <!-- 侧边栏 -->
    <aside class="sidebar">
      <div class="sidebar-header">
        <h2>CRM系统</h2>
      </div>
      <el-menu
        :default-active="activeMenu"
        class="el-menu-vertical-demo"
        @select="handleMenuSelect"
        background-color="#001529"
        text-color="#fff"
        active-text-color="#ffd04b"
      >
        <el-menu-item index="/dashboard">
          <template #icon>
            <el-icon><HomeFilled /></el-icon>
          </template>
          <span>首页</span>
        </el-menu-item>
        
        <!-- 系统管理 -->
        <el-sub-menu index="/system">
          <template #title>
            <el-icon><Setting /></el-icon>
            <span>系统管理</span>
          </template>
          <el-menu-item index="/system/user">
            <span>用户管理</span>
          </el-menu-item>
          <el-menu-item index="/system/role">
            <span>角色管理</span>
          </el-menu-item>
          <el-menu-item index="/system/menu">
            <span>菜单管理</span>
          </el-menu-item>
          <el-menu-item index="/system/dept">
            <span>部门管理</span>
          </el-menu-item>
          <el-menu-item index="/system/dict">
            <span>数据字典</span>
          </el-menu-item>
          <el-menu-item index="/system/log">
            <span>系统日志</span>
          </el-menu-item>
        </el-sub-menu>
        
        <!-- 客户管理 -->
        <el-sub-menu index="/customer">
          <template #title>
            <el-icon><UserFilled /></el-icon>
            <span>客户管理</span>
          </template>
          <el-menu-item index="/customer/index">
            <span>客户列表</span>
          </el-menu-item>
          <el-menu-item index="/customer/contact">
            <span>联系人管理</span>
          </el-menu-item>
          <el-menu-item index="/customer/follow">
            <span>跟进记录</span>
          </el-menu-item>
        </el-sub-menu>
        
        <!-- 销售管理 -->
        <el-sub-menu index="/sales">
          <template #title>
            <el-icon><TrendCharts /></el-icon>
            <span>销售管理</span>
          </template>
          <el-menu-item index="/sales/opportunity">
            <span>商机管理</span>
          </el-menu-item>
          <el-menu-item index="/sales/contract">
            <span>合同管理</span>
          </el-menu-item>
          <el-menu-item index="/sales/order">
            <span>订单管理</span>
          </el-menu-item>
          <el-menu-item index="/sales/payment">
            <span>回款管理</span>
          </el-menu-item>
        </el-sub-menu>
        
        <!-- 数据统计 -->
        <el-sub-menu index="/stats">
          <template #title>
            <el-icon><DataAnalysis /></el-icon>
            <span>数据统计</span>
          </template>
          <el-menu-item index="/stats/customer">
            <span>客户统计</span>
          </el-menu-item>
          <el-menu-item index="/stats/sales">
            <span>销售统计</span>
          </el-menu-item>
          <el-menu-item index="/stats/opportunity">
            <span>商机统计</span>
          </el-menu-item>
          <el-menu-item index="/stats/payment">
            <span>回款统计</span>
          </el-menu-item>
        </el-sub-menu>
      </el-menu>
    </aside>
    
    <!-- 主内容区 -->
    <main class="main-content">
      <!-- 顶部导航 -->
      <header class="header">
        <div class="header-left">
          <el-button type="text" @click="toggleSidebar">
            <el-icon><Menu /></el-icon>
          </el-button>
        </div>
        <div class="header-right">
          <el-dropdown>
            <span class="user-info">
              <el-avatar :size="32">{{ userInfo.username.charAt(0) }}</el-avatar>
              <span class="user-name">{{ userInfo.username }}</span>
              <el-icon class="el-icon--right"><ArrowDown /></el-icon>
            </span>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item @click="handleUserInfo">
                  <el-icon><User /></el-icon>
                  <span>个人信息</span>
                </el-dropdown-item>
                <el-dropdown-item @click="handleChangePassword">
                  <el-icon><Lock /></el-icon>
                  <span>修改密码</span>
                </el-dropdown-item>
                <el-dropdown-item divided @click="handleLogout">
                  <el-icon><SwitchButton /></el-icon>
                  <span>退出登录</span>
                </el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </header>
      
      <!-- 内容区 -->
      <div class="content">
        <router-view />
      </div>
    </main>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { useUserStore } from '../store/user'
import { HomeFilled, Setting, UserFilled, TrendCharts, DataAnalysis, Menu, User, Lock, SwitchButton, ArrowDown } from '@element-plus/icons-vue'

const router = useRouter()
const route = useRoute()
const userStore = useUserStore()

const activeMenu = ref('/dashboard')
const userInfo = ref({ username: '管理员' })
const sidebarCollapsed = ref(false)

// 计算当前激活的菜单
const currentPath = computed(() => route.path)

// 监听路由变化，更新激活菜单
const handleMenuSelect = (key) => {
  router.push(key)
  activeMenu.value = key
}

// 切换侧边栏展开/收起
const toggleSidebar = () => {
  sidebarCollapsed.value = !sidebarCollapsed.value
}

// 处理个人信息
const handleUserInfo = () => {
  // 跳转到个人信息页面
}

// 处理修改密码
const handleChangePassword = () => {
  // 打开修改密码对话框
}

// 处理退出登录
const handleLogout = async () => {
  try {
    await userStore.logout()
    router.push('/login')
  } catch (error) {
    console.error('退出登录失败:', error)
  }
}

// 组件挂载时
onMounted(() => {
  // 获取用户信息
  if (userStore.isLoggedIn) {
    userInfo.value = userStore.userInfo
  }
})
</script>

<style scoped>
.layout-container {
  display: flex;
  width: 100%;
  height: 100vh;
  overflow: hidden;
}

.sidebar {
  width: 200px;
  background-color: #001529;
  color: #fff;
  transition: width 0.3s;
}

.sidebar.collapsed {
  width: 64px;
}

.sidebar-header {
  height: 64px;
  line-height: 64px;
  text-align: center;
  border-bottom: 1px solid #002140;
}

.sidebar-header h2 {
  margin: 0;
  font-size: 18px;
  font-weight: bold;
}

.main-content {
  flex: 1;
  display: flex;
  flex-direction: column;
  overflow: hidden;
}

.header {
  height: 64px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 20px;
  background-color: #fff;
  border-bottom: 1px solid #e8e8e8;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.header-left {
  display: flex;
  align-items: center;
}

.header-right {
  display: flex;
  align-items: center;
}

.user-info {
  display: flex;
  align-items: center;
  cursor: pointer;
}

.user-name {
  margin: 0 10px;
}

.content {
  flex: 1;
  padding: 20px;
  overflow-y: auto;
  background-color: #f5f7fa;
}
</style>
