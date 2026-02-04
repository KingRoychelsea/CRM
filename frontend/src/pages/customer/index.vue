<template>
  <div class="customer-management">
    <!-- 客户管理页面 -->
    <el-card v-if="currentPage === 'index'">
      <template #header>
        <div class="card-header">
          <span>客户管理</span>
          <div class="header-buttons">
            <el-button type="primary" @click="handleAddCustomer">
              <el-icon><Plus /></el-icon>
              <span>新增客户</span>
            </el-button>
            <el-button @click="handleImportCustomer">
              <el-icon><Upload /></el-icon>
              <span>导入</span>
            </el-button>
            <el-button @click="handleExportCustomer">
              <el-icon><Download /></el-icon>
              <span>导出</span>
            </el-button>
          </div>
        </div>
      </template>
      
      <!-- 搜索表单 -->
      <el-form :model="searchForm" class="search-form" label-width="80px">
        <el-row :gutter="20">
          <el-col :span="6">
            <el-form-item label="客户名称">
              <el-input v-model="searchForm.customerName" placeholder="请输入客户名称" clearable></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="联系人">
              <el-input v-model="searchForm.contactName" placeholder="请输入联系人" clearable></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="电话">
              <el-input v-model="searchForm.phone" placeholder="请输入电话" clearable></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="客户分类">
              <el-select v-model="searchForm.customerType" placeholder="请选择客户分类" clearable>
                <el-option label="潜在客户" value="0"></el-option>
                <el-option label="意向客户" value="1"></el-option>
                <el-option label="合作客户" value="2"></el-option>
                <el-option label="流失客户" value="3"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="24" class="search-buttons">
            <el-button type="primary" @click="handleSearch">
              <el-icon><Search /></el-icon>
              <span>查询</span>
            </el-button>
            <el-button @click="handleReset">
              <el-icon><Refresh /></el-icon>
              <span>重置</span>
            </el-button>
          </el-col>
        </el-row>
      </el-form>
      
      <!-- 客户列表 -->
      <el-table
        v-loading="loading"
        :data="customerList"
        style="width: 100%"
        border
        @row-click="handleRowClick"
      >
        <el-table-column prop="id" label="客户ID" width="80"></el-table-column>
        <el-table-column prop="customerName" label="客户名称" width="180"></el-table-column>
        <el-table-column prop="contactName" label="联系人" width="120"></el-table-column>
        <el-table-column prop="phone" label="电话" width="150"></el-table-column>
        <el-table-column prop="email" label="邮箱" width="180"></el-table-column>
        <el-table-column prop="customerType" label="客户分类" width="120">
          <template #default="scope">
            <el-tag
              :type="customerTypeMap[scope.row.customerType].type"
              :effect="customerTypeMap[scope.row.customerType].effect"
            >
              {{ customerTypeMap[scope.row.customerType].label }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100">
          <template #default="scope">
            <el-switch
              v-model="scope.row.status"
              active-value="1"
              inactive-value="0"
              @change="handleStatusChange(scope.row)"
            ></el-switch>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="180"></el-table-column>
        <el-table-column label="操作" width="250" fixed="right">
          <template #default="scope">
            <el-button type="primary" size="small" @click="handleEditCustomer(scope.row)">
              <el-icon><Edit /></el-icon>
              <span>编辑</span>
            </el-button>
            <el-button type="success" size="small" @click="handleViewCustomer(scope.row)">
              <el-icon><View /></el-icon>
              <span>详情</span>
            </el-button>
            <el-button type="warning" size="small" @click="handleAddFollow(scope.row)">
              <el-icon><ChatDotSquare /></el-icon>
              <span>跟进</span>
            </el-button>
            <el-button type="danger" size="small" @click="handleDeleteCustomer(scope.row.id)">
              <el-icon><Delete /></el-icon>
              <span>删除</span>
            </el-button>
          </template>
        </el-table-column>
      </el-table>
      
      <!-- 分页 -->
      <div class="pagination">
        <el-pagination
          v-model:current-page="pagination.current"
          v-model:page-size="pagination.size"
          :page-sizes="[10, 20, 50, 100]"
          layout="total, sizes, prev, pager, next, jumper"
          :total="pagination.total"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        ></el-pagination>
      </div>
    </el-card>
    
    <!-- 联系人管理页面 -->
    <el-card v-else-if="currentPage === 'contact'">
      <template #header>
        <div class="card-header">
          <span>联系人管理</span>
          <div class="header-buttons">
            <el-button type="primary">
              <el-icon><Plus /></el-icon>
              <span>新增联系人</span>
            </el-button>
          </div>
        </div>
      </template>
      
      <!-- 联系人列表 -->
      <el-table
        :data="contactList"
        style="width: 100%"
        border
      >
        <el-table-column prop="id" label="联系人ID" width="80"></el-table-column>
        <el-table-column prop="contactName" label="联系人姓名" width="120"></el-table-column>
        <el-table-column prop="phone" label="电话" width="150"></el-table-column>
        <el-table-column prop="email" label="邮箱" width="180"></el-table-column>
        <el-table-column prop="position" label="职位" width="120"></el-table-column>
        <el-table-column prop="customerName" label="所属客户" width="180"></el-table-column>
        <el-table-column label="操作" width="150" fixed="right">
          <template #default="scope">
            <el-button type="primary" size="small">
              <el-icon><Edit /></el-icon>
              <span>编辑</span>
            </el-button>
            <el-button type="danger" size="small">
              <el-icon><Delete /></el-icon>
              <span>删除</span>
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
    
    <!-- 跟进记录页面 -->
    <el-card v-else-if="currentPage === 'follow'">
      <template #header>
        <div class="card-header">
          <span>跟进记录</span>
          <div class="header-buttons">
            <el-button type="primary">
              <el-icon><Plus /></el-icon>
              <span>添加跟进记录</span>
            </el-button>
          </div>
        </div>
      </template>
      
      <!-- 跟进记录列表 -->
      <el-table
        :data="followRecordList"
        style="width: 100%"
        border
      >
        <el-table-column prop="id" label="记录ID" width="80"></el-table-column>
        <el-table-column prop="customerName" label="客户名称" width="180"></el-table-column>
        <el-table-column prop="followMethod" label="跟进方式" width="100">
          <template #default="scope">
            {{ followMethodMap[scope.row.followMethod] }}
          </template>
        </el-table-column>
        <el-table-column prop="followContent" label="跟进内容"></el-table-column>
        <el-table-column prop="nextFollowTime" label="下次跟进时间" width="180"></el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="180"></el-table-column>
        <el-table-column label="操作" width="100" fixed="right">
          <template #default="scope">
            <el-button type="primary" size="small">
              <el-icon><Edit /></el-icon>
              <span>编辑</span>
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
    
    <!-- 新增/编辑客户对话框 -->
    <el-dialog
      v-model="dialogVisible"
      :title="dialogType === 'add' ? '新增客户' : '编辑客户'"
      width="700px"
    >
      <el-form
        :model="customerForm"
        :rules="customerRules"
        ref="customerFormRef"
        label-width="100px"
      >
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item prop="customerName">
              <el-input v-model="customerForm.customerName" placeholder="请输入客户名称"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item prop="customerType">
              <el-select v-model="customerForm.customerType" placeholder="请选择客户分类">
                <el-option label="潜在客户" value="0"></el-option>
                <el-option label="意向客户" value="1"></el-option>
                <el-option label="合作客户" value="2"></el-option>
                <el-option label="流失客户" value="3"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item prop="contactName">
              <el-input v-model="customerForm.contactName" placeholder="请输入联系人"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item prop="phone">
              <el-input v-model="customerForm.phone" placeholder="请输入电话"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item prop="email">
              <el-input v-model="customerForm.email" placeholder="请输入邮箱"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item prop="address">
              <el-input v-model="customerForm.address" placeholder="请输入地址"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item prop="industry">
              <el-input v-model="customerForm.industry" placeholder="请输入所属行业"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item prop="source">
              <el-input v-model="customerForm.source" placeholder="请输入客户来源"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item prop="description">
              <el-input v-model="customerForm.description" type="textarea" placeholder="请输入客户描述"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item prop="status">
              <el-select v-model="customerForm.status" placeholder="请选择状态">
                <el-option label="启用" value="1"></el-option>
                <el-option label="禁用" value="0"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="handleSubmit">确定</el-button>
        </span>
      </template>
    </el-dialog>
    
    <!-- 客户详情对话框 -->
    <el-dialog
      v-model="detailVisible"
      title="客户详情"
      width="900px"
    >
      <el-tabs>
        <el-tab-pane label="基本信息">
          <el-descriptions :column="2" border>
            <el-descriptions-item label="客户名称">{{ currentCustomer.customerName }}</el-descriptions-item>
            <el-descriptions-item label="客户分类">{{ currentCustomer.customerType ? customerTypeMap[currentCustomer.customerType].label : '' }}</el-descriptions-item>
            <el-descriptions-item label="联系人">{{ currentCustomer.contactName }}</el-descriptions-item>
            <el-descriptions-item label="电话">{{ currentCustomer.phone }}</el-descriptions-item>
            <el-descriptions-item label="邮箱">{{ currentCustomer.email }}</el-descriptions-item>
            <el-descriptions-item label="地址">{{ currentCustomer.address }}</el-descriptions-item>
            <el-descriptions-item label="所属行业">{{ currentCustomer.industry }}</el-descriptions-item>
            <el-descriptions-item label="客户来源">{{ currentCustomer.source }}</el-descriptions-item>
            <el-descriptions-item label="状态">{{ currentCustomer.status === '1' ? '启用' : '禁用' }}</el-descriptions-item>
            <el-descriptions-item label="创建时间">{{ currentCustomer.createTime }}</el-descriptions-item>
            <el-descriptions-item label="客户描述" :span="2">{{ currentCustomer.description }}</el-descriptions-item>
          </el-descriptions>
        </el-tab-pane>
        <el-tab-pane label="联系人">
          <el-table :data="contactList" style="width: 100%" border>
            <el-table-column prop="id" label="联系人ID" width="80"></el-table-column>
            <el-table-column prop="contactName" label="联系人姓名" width="120"></el-table-column>
            <el-table-column prop="phone" label="电话" width="150"></el-table-column>
            <el-table-column prop="email" label="邮箱" width="180"></el-table-column>
            <el-table-column prop="position" label="职位" width="120"></el-table-column>
            <el-table-column prop="remark" label="备注"></el-table-column>
          </el-table>
        </el-tab-pane>
        <el-tab-pane label="跟进记录">
          <el-table :data="followRecordList" style="width: 100%" border>
            <el-table-column prop="id" label="记录ID" width="80"></el-table-column>
            <el-table-column prop="followMethod" label="跟进方式" width="100">
              <template #default="scope">
                {{ followMethodMap[scope.row.followMethod] }}
              </template>
            </el-table-column>
            <el-table-column prop="followContent" label="跟进内容"></el-table-column>
            <el-table-column prop="nextFollowTime" label="下次跟进时间" width="180"></el-table-column>
            <el-table-column prop="createTime" label="创建时间" width="180"></el-table-column>
          </el-table>
        </el-tab-pane>
      </el-tabs>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="detailVisible = false">关闭</el-button>
        </span>
      </template>
    </el-dialog>
    
    <!-- 导入客户对话框 -->
    <el-dialog
      v-model="importVisible"
      title="导入客户"
      width="500px"
    >
      <el-upload
        class="upload-demo"
        drag
        action="#"
        multiple
        :auto-upload="false"
        :on-change="handleFileChange"
      >
        <el-icon class="el-icon--upload"><Upload /></el-icon>
        <div class="el-upload__text">
          将文件拖到此处，或 <em>点击上传</em>
        </div>
        <template #tip>
          <div class="el-upload__tip">
            只能上传 Excel 文件，且不超过 5MB
          </div>
        </template>
      </el-upload>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="importVisible = false">取消</el-button>
          <el-button type="primary" @click="handleImportSubmit">确定</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, computed, watch } from 'vue'
import { useRoute } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus, Search, Refresh, Edit, View, ChatDotSquare, Delete, Upload, Download } from '@element-plus/icons-vue'
import customerApi from '@/api/customer/customer'
import contactApi from '@/api/customer/contact'
import followRecordApi from '@/api/customer/followRecord'

// 获取当前路由
const route = useRoute()

// 根据路由计算当前页面类型
const currentPage = computed(() => {
  const path = route.path
  if (path.includes('/contact')) {
    return 'contact'
  } else if (path.includes('/follow')) {
    return 'follow'
  } else {
    return 'index'
  }
})

// 搜索表单
const searchForm = reactive({
  customerName: '',
  contactName: '',
  phone: '',
  customerType: ''
})

// 分页信息
const pagination = reactive({
  current: 1,
  size: 10,
  total: 0
})

// 加载状态
const loading = ref(false)

// 客户列表
const customerList = ref([])

// 客户类型映射
const customerTypeMap = {
  '0': { label: '潜在客户', type: 'info', effect: 'dark' },
  '1': { label: '意向客户', type: 'warning', effect: 'dark' },
  '2': { label: '合作客户', type: 'success', effect: 'dark' },
  '3': { label: '流失客户', type: 'danger', effect: 'dark' }
}

// 跟进方式映射
const followMethodMap = {
  '0': '电话',
  '1': '微信',
  '2': '面谈',
  '3': '邮件',
  '4': '其他'
}

// 对话框状态
const dialogVisible = ref(false)
const detailVisible = ref(false)
const importVisible = ref(false)
const dialogType = ref('add')

// 表单引用
const customerFormRef = ref(null)

// 客户表单
const customerForm = reactive({
  id: '',
  customerName: '',
  customerType: '0',
  contactName: '',
  phone: '',
  email: '',
  address: '',
  industry: '',
  source: '',
  description: '',
  status: '1'
})

// 当前客户
const currentCustomer = ref({})

// 联系人列表
const contactList = ref([
  {
    id: 1,
    contactName: '张三',
    phone: '13800138001',
    email: 'zhangsan@example.com',
    position: '总经理',
    customerName: '腾讯科技'
  },
  {
    id: 2,
    contactName: '李四',
    phone: '13900139002',
    email: 'lisi@example.com',
    position: '技术总监',
    customerName: '阿里巴巴'
  },
  {
    id: 3,
    contactName: '王五',
    phone: '13700137003',
    email: 'wangwu@example.com',
    position: '市场经理',
    customerName: '百度在线'
  }
])

// 跟进记录列表
const followRecordList = ref([
  {
    id: 1,
    customerName: '腾讯科技',
    followMethod: '0',
    followContent: '讨论智能办公系统升级方案',
    nextFollowTime: '2026-02-10 14:00:00',
    createTime: '2026-02-04 10:30:00'
  },
  {
    id: 2,
    customerName: '阿里巴巴',
    followMethod: '1',
    followContent: '微信沟通新项目需求',
    nextFollowTime: '2026-02-15 10:00:00',
    createTime: '2026-02-04 09:15:00'
  },
  {
    id: 3,
    customerName: '百度在线',
    followMethod: '2',
    followContent: '面谈搜索引擎优化服务',
    nextFollowTime: '2026-02-20 16:00:00',
    createTime: '2026-02-04 14:20:00'
  }
])

// 表单验证规则
const customerRules = {
  customerName: [
    { required: true, message: '请输入客户名称', trigger: 'blur' },
    { min: 2, max: 50, message: '客户名称长度在 2 到 50 个字符', trigger: 'blur' }
  ],
  contactName: [
    { required: true, message: '请输入联系人', trigger: 'blur' },
    { min: 2, max: 20, message: '联系人长度在 2 到 20 个字符', trigger: 'blur' }
  ],
  phone: [
    { required: true, message: '请输入电话', trigger: 'blur' },
    { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号格式', trigger: 'blur' }
  ],
  email: [
    { type: 'email', message: '请输入正确的邮箱格式', trigger: 'blur' }
  ],
  customerType: [
    { required: true, message: '请选择客户分类', trigger: 'blur' }
  ],
  status: [
    { required: true, message: '请选择状态', trigger: 'blur' }
  ]
}

// 获取客户列表
const getCustomerList = async () => {
  loading.value = true
  try {
    const params = {
            ...searchForm,
            pageNum: pagination.current,
            pageSize: pagination.size
        }
    const response = await customerApi.getCustomerList(params)
    customerList.value = response.data.records
    pagination.total = response.data.total
  } catch (error) {
    console.error('获取客户列表失败:', error)
    ElMessage.error('获取客户列表失败')
  } finally {
    loading.value = false
  }
}

// 搜索
const handleSearch = () => {
  pagination.current = 1
  getCustomerList()
}

// 重置搜索
const handleReset = () => {
  Object.keys(searchForm).forEach(key => {
    searchForm[key] = ''
  })
  pagination.current = 1
  getCustomerList()
}

// 分页大小变化
const handleSizeChange = (size) => {
  pagination.size = size
  getCustomerList()
}

// 分页当前页变化
const handleCurrentChange = (current) => {
  pagination.current = current
  getCustomerList()
}

// 新增客户
const handleAddCustomer = () => {
  dialogType.value = 'add'
  Object.keys(customerForm).forEach(key => {
    customerForm[key] = ''
  })
  customerForm.customerType = '0'
  customerForm.status = '1'
  dialogVisible.value = true
}

// 编辑客户
const handleEditCustomer = (customer) => {
  dialogType.value = 'edit'
  Object.assign(customerForm, customer)
  dialogVisible.value = true
}

// 提交表单
const handleSubmit = async () => {
  if (!customerFormRef.value) return
  
  try {
    await customerFormRef.value.validate()
    
    if (dialogType.value === 'add') {
      await customerApi.createCustomer(customerForm)
    } else {
      await customerApi.updateCustomer(customerForm.id, customerForm)
    }
    
    dialogVisible.value = false
    ElMessage.success(dialogType.value === 'add' ? '新增客户成功' : '编辑客户成功')
    getCustomerList()
  } catch (error) {
    console.error('提交失败:', error)
    ElMessage.error('提交失败')
  }
}

// 查看客户详情
const handleViewCustomer = async (customer) => {
  try {
    // 获取客户详情
    const customerResponse = await customerApi.getCustomerInfo(customer.id)
    currentCustomer.value = customerResponse.data
    
    // 获取联系人列表
    const contactResponse = await contactApi.getContactList(customer.id)
    contactList.value = contactResponse.data
    
    // 获取跟进记录列表
    const followResponse = await followRecordApi.getFollowRecordList(customer.id)
    followRecordList.value = followResponse.data
    
    detailVisible.value = true
  } catch (error) {
    console.error('获取客户详情失败:', error)
    ElMessage.error('获取客户详情失败')
  }
}

// 行点击事件
const handleRowClick = (row, column, event) => {
  console.log('行点击事件:', row, column, event)
}

// 状态变更
const handleStatusChange = (customer) => {
  // 模拟API请求
  // 实际项目中需要调用后端API
  setTimeout(() => {
    ElMessage.success('状态更新成功')
  }, 500)
}

// 添加跟进记录
const handleAddFollow = (customer) => {
  ElMessage.info('添加跟进记录功能开发中')
}

// 删除客户
const handleDeleteCustomer = (id) => {
  ElMessageBox.confirm('确定要删除该客户吗？', '警告', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await customerApi.deleteCustomer(id)
      ElMessage.success('删除客户成功')
      getCustomerList()
    } catch (error) {
      console.error('删除客户失败:', error)
      ElMessage.error('删除客户失败')
    }
  }).catch(() => {
    // 取消删除
  })
}

// 导入客户
const handleImportCustomer = () => {
  importVisible.value = true
}

// 文件变更
const handleFileChange = (file, fileList) => {
  console.log('文件变更:', file, fileList)
}

// 提交导入
const handleImportSubmit = () => {
  // 模拟API请求
  // 实际项目中需要调用后端API
  setTimeout(() => {
    importVisible.value = false
    ElMessage.success('导入客户成功')
    getCustomerList()
  }, 500)
}

// 导出客户
const handleExportCustomer = () => {
  // 模拟API请求
  // 实际项目中需要调用后端API
  setTimeout(() => {
    ElMessage.success('导出客户成功')
  }, 500)
}

// 获取联系人列表
const getContactList = async () => {
  // 使用mock数据，避免权限错误
  console.log('使用mock数据加载联系人列表')
}

// 获取跟进记录列表
const getFollowRecordList = async () => {
  // 使用mock数据，避免权限错误
  console.log('使用mock数据加载跟进记录列表')
}

// 组件挂载时
onMounted(() => {
  if (currentPage.value === 'index') {
    getCustomerList()
  } else if (currentPage.value === 'contact') {
    getContactList()
  } else if (currentPage.value === 'follow') {
    getFollowRecordList()
  }
})

// 监听页面变化
watch(() => currentPage.value, (newPage) => {
  if (newPage === 'index') {
    getCustomerList()
  } else if (newPage === 'contact') {
    getContactList()
  } else if (newPage === 'follow') {
    getFollowRecordList()
  }
})
</script>

<style scoped>
.customer-management {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.header-buttons {
  display: flex;
  gap: 10px;
}

.search-form {
  margin-bottom: 20px;
  padding: 20px;
  background-color: #f9f9f9;
  border-radius: 8px;
}

.search-buttons {
  text-align: right;
  margin-top: 10px;
}

.pagination {
  margin-top: 20px;
  text-align: right;
}

.dialog-footer {
  text-align: right;
}

.upload-demo {
  margin-bottom: 20px;
}
</style>