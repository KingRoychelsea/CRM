<template>
  <div class="dashboard">
    <el-card class="welcome-card">
      <template #header>
        <div class="card-header">
          <span>欢迎使用CRM系统</span>
        </div>
      </template>
      <div class="welcome-content">
        <p>这是CRM系统的首页，您可以在这里查看系统概览和关键数据。</p>
        <el-button type="primary" @click="goToCustomer">客户管理</el-button>
        <el-button type="success" @click="goToOpportunity">商机管理</el-button>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { useRouter } from 'vue-router'

const router = useRouter()

const goToCustomer = () => {
  router.push('/customer')
}

const goToOpportunity = () => {
  router.push('/sales/opportunity')
}
</script>

<style scoped>
.dashboard {
  padding: 20px;
}

.welcome-card {
  margin-bottom: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.welcome-content {
  padding: 20px 0;
}

.welcome-content p {
  margin-bottom: 20px;
  font-size: 16px;
}

.welcome-content .el-button {
  margin-right: 10px;
}
</style>