<template>
  <div class="log-management">
    <el-card shadow="hover">
      <template #header>
        <div class="card-header">
          <span>系统日志</span>
          <el-tabs v-model="activeTab">
            <el-tab-pane label="登录日志" name="login"></el-tab-pane>
            <el-tab-pane label="操作日志" name="oper"></el-tab-pane>
          </el-tabs>
        </div>
      </template>
      
      <!-- 登录日志 -->
      <div v-if="activeTab === 'login'">
        <!-- 搜索表单 -->
        <el-form :inline="true" :model="loginSearchForm" class="search-form">
          <el-form-item label="用户账号">
            <el-input v-model="loginSearchForm.username" placeholder="请输入用户账号" style="width: 180px" />
          </el-form-item>
          <el-form-item label="登录状态">
            <el-select v-model="loginSearchForm.status" placeholder="请选择状态" style="width: 120px">
              <el-option label="成功" value="1" />
              <el-option label="失败" value="0" />
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="handleLoginSearch">查询</el-button>
            <el-button @click="handleLoginReset">重置</el-button>
            <el-button type="danger" @click="handleClearLoginLog">清空日志</el-button>
          </el-form-item>
        </el-form>
        
        <!-- 登录日志列表 -->
        <el-table :data="loginLogList" style="width: 100%" border>
          <el-table-column prop="id" label="日志ID" width="80" />
          <el-table-column prop="username" label="用户账号" width="150" />
          <el-table-column prop="ipaddr" label="登录IP" width="150" />
          <el-table-column prop="loginLocation" label="登录地点" width="200" />
          <el-table-column prop="browser" label="浏览器" width="150" />
          <el-table-column prop="os" label="操作系统" width="150" />
          <el-table-column prop="status" label="登录状态" width="100">
            <template #default="scope">
              <el-tag :type="scope.row.status === 1 ? 'success' : 'danger'">
                {{ scope.row.status === 1 ? '成功' : '失败' }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="msg" label="登录信息" />
          <el-table-column prop="loginTime" label="登录时间" width="200" />
          <el-table-column label="操作" width="100" fixed="right">
            <template #default="scope">
              <el-button type="danger" size="small" @click="handleDeleteLoginLog(scope.row.id)">
                <el-icon><Delete /></el-icon>
              </el-button>
            </template>
          </el-table-column>
        </el-table>
        
        <!-- 登录日志分页 -->
        <div class="pagination">
          <el-pagination
            v-model:current-page="loginPagination.currentPage"
            v-model:page-size="loginPagination.pageSize"
            :page-sizes="[10, 20, 50, 100]"
            layout="total, sizes, prev, pager, next, jumper"
            :total="loginPagination.total"
            @size-change="handleLoginSizeChange"
            @current-change="handleLoginCurrentChange"
          />
        </div>
      </div>
      
      <!-- 操作日志 -->
      <div v-if="activeTab === 'oper'">
        <!-- 搜索表单 -->
        <el-form :inline="true" :model="operSearchForm" class="search-form">
          <el-form-item label="操作人">
            <el-input v-model="operSearchForm.username" placeholder="请输入操作人" style="width: 180px" />
          </el-form-item>
          <el-form-item label="操作类型">
            <el-select v-model="operSearchForm.operType" placeholder="请选择操作类型" style="width: 120px">
              <el-option label="新增" value="insert" />
              <el-option label="修改" value="update" />
              <el-option label="删除" value="delete" />
              <el-option label="查询" value="select" />
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="handleOperSearch">查询</el-button>
            <el-button @click="handleOperReset">重置</el-button>
            <el-button type="danger" @click="handleClearOperLog">清空日志</el-button>
          </el-form-item>
        </el-form>
        
        <!-- 操作日志列表 -->
        <el-table :data="operLogList" style="width: 100%" border>
          <el-table-column prop="id" label="日志ID" width="80" />
          <el-table-column prop="username" label="操作人" width="150" />
          <el-table-column prop="title" label="操作模块" width="150" />
          <el-table-column prop="businessType" label="业务类型" width="120">
            <template #default="scope">
              <el-tag :type="getBusinessTypeTag(scope.row.businessType)">
                {{ getBusinessTypeName(scope.row.businessType) }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="method" label="请求方法" width="200" />
          <el-table-column prop="requestMethod" label="请求方式" width="100">
            <template #default="scope">
              <el-tag :type="scope.row.requestMethod === 'GET' ? 'info' : 'warning'">
                {{ scope.row.requestMethod }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="operIp" label="操作IP" width="150" />
          <el-table-column prop="operLocation" label="操作地点" width="200" />
          <el-table-column prop="status" label="操作状态" width="100">
            <template #default="scope">
              <el-tag :type="scope.row.status === 1 ? 'success' : 'danger'">
                {{ scope.row.status === 1 ? '成功' : '失败' }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="operTime" label="操作时间" width="200" />
          <el-table-column label="操作" width="100" fixed="right">
            <template #default="scope">
              <el-button type="danger" size="small" @click="handleDeleteOperLog(scope.row.id)">
                <el-icon><Delete /></el-icon>
              </el-button>
            </template>
          </el-table-column>
        </el-table>
        
        <!-- 操作日志分页 -->
        <div class="pagination">
          <el-pagination
            v-model:current-page="operPagination.currentPage"
            v-model:page-size="operPagination.pageSize"
            :page-sizes="[10, 20, 50, 100]"
            layout="total, sizes, prev, pager, next, jumper"
            :total="operPagination.total"
            @size-change="handleOperSizeChange"
            @current-change="handleOperCurrentChange"
          />
        </div>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, watch } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Delete } from '@element-plus/icons-vue'
import logApi from '@/api/system/log'

// 响应式数据
const activeTab = ref('login')

// 登录日志搜索表单
const loginSearchForm = reactive({
  username: '',
  status: ''
})

// 登录日志分页
const loginPagination = reactive({
  currentPage: 1,
  pageSize: 10,
  total: 0
})

// 登录日志列表
const loginLogList = ref([])

// 操作日志搜索表单
const operSearchForm = reactive({
  username: '',
  operType: ''
})

// 操作日志分页
const operPagination = reactive({
  currentPage: 1,
  pageSize: 10,
  total: 0
})

// 操作日志列表
const operLogList = ref([])

// 业务类型映射（后端使用数字：0-其他，1-新增，2-修改，3-删除）
const businessTypeMap = {
  '0': { name: '其他', tag: 'info' },
  '1': { name: '新增', tag: 'success' },
  '2': { name: '修改', tag: 'warning' },
  '3': { name: '删除', tag: 'danger' }
}

// 获取业务类型名称
const getBusinessTypeName = (type) => {
  return businessTypeMap[type]?.name || type
}

// 获取业务类型标签类型
const getBusinessTypeTag = (type) => {
  return businessTypeMap[type]?.tag || 'info'
}

// 获取登录日志列表
const getLoginLogList = async () => {
  try {
    const params = {
      ...loginSearchForm,
      page: loginPagination.currentPage,
      pageSize: loginPagination.pageSize
    }
    const response = await logApi.getLoginLogList(params)
    // 处理后端返回的直接列表格式
    loginLogList.value = response.data
    loginPagination.total = response.data.length
  } catch (error) {
    console.error('获取登录日志失败:', error)
    ElMessage.error('获取登录日志失败')
  }
}

// 获取操作日志列表
const getOperLogList = async () => {
  try {
    const params = {
      ...operSearchForm,
      page: operPagination.currentPage,
      pageSize: operPagination.pageSize
    }
    const response = await logApi.getOperLogList(params)
    // 处理后端返回的直接列表格式
    operLogList.value = response.data
    operPagination.total = response.data.length
  } catch (error) {
    console.error('获取操作日志失败:', error)
    ElMessage.error('获取操作日志失败')
  }
}

// 登录日志搜索
const handleLoginSearch = () => {
  loginPagination.currentPage = 1
  getLoginLogList()
}

// 登录日志重置
const handleLoginReset = () => {
  Object.keys(loginSearchForm).forEach(key => {
    loginSearchForm[key] = ''
  })
  loginPagination.currentPage = 1
  getLoginLogList()
}

// 登录日志分页大小变化
const handleLoginSizeChange = (size) => {
  loginPagination.pageSize = size
  getLoginLogList()
}

// 登录日志当前页变化
const handleLoginCurrentChange = (current) => {
  loginPagination.currentPage = current
  getLoginLogList()
}

// 删除登录日志
const handleDeleteLoginLog = (id) => {
  ElMessageBox.confirm('确定要删除该登录日志吗？', '警告', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await logApi.deleteLoginLog(id)
      getLoginLogList()
      ElMessage.success('删除成功')
    } catch (error) {
      console.error('删除失败:', error)
      ElMessage.error('删除失败')
    }
  }).catch(() => {
    // 取消删除
  })
}

// 清空登录日志
const handleClearLoginLog = () => {
  ElMessageBox.confirm('确定要清空所有登录日志吗？', '警告', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await logApi.clearLoginLog()
      getLoginLogList()
      ElMessage.success('清空成功')
    } catch (error) {
      console.error('清空失败:', error)
      ElMessage.error('清空失败')
    }
  }).catch(() => {
    // 取消清空
  })
}

// 操作日志搜索
const handleOperSearch = () => {
  operPagination.currentPage = 1
  getOperLogList()
}

// 操作日志重置
const handleOperReset = () => {
  Object.keys(operSearchForm).forEach(key => {
    operSearchForm[key] = ''
  })
  operPagination.currentPage = 1
  getOperLogList()
}

// 操作日志分页大小变化
const handleOperSizeChange = (size) => {
  operPagination.pageSize = size
  getOperLogList()
}

// 操作日志当前页变化
const handleOperCurrentChange = (current) => {
  operPagination.currentPage = current
  getOperLogList()
}

// 删除操作日志
const handleDeleteOperLog = (id) => {
  ElMessageBox.confirm('确定要删除该操作日志吗？', '警告', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await logApi.deleteOperLog(id)
      getOperLogList()
      ElMessage.success('删除成功')
    } catch (error) {
      console.error('删除失败:', error)
      ElMessage.error('删除失败')
    }
  }).catch(() => {
    // 取消删除
  })
}

// 清空操作日志
const handleClearOperLog = () => {
  ElMessageBox.confirm('确定要清空所有操作日志吗？', '警告', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await logApi.clearOperLog()
      getOperLogList()
      ElMessage.success('清空成功')
    } catch (error) {
      console.error('清空失败:', error)
      ElMessage.error('清空失败')
    }
  }).catch(() => {
    // 取消清空
  })
}

// 监听标签切换
watch(activeTab, (newTab) => {
  if (newTab === 'login') {
    getLoginLogList()
  } else {
    getOperLogList()
  }
})

// 组件挂载时
onMounted(() => {
  getLoginLogList()
})
</script>

<style scoped>
.log-management {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.search-form {
  margin-bottom: 20px;
}

.pagination {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}
</style>