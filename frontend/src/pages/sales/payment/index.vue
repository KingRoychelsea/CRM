<template>
  <div class="payment-management">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>回款管理</span>
          <el-button type="primary" @click="handleAddPayment">
            <el-icon><Plus /></el-icon>
            <span>新增回款</span>
          </el-button>
        </div>
      </template>
      
      <!-- 搜索表单 -->
      <el-form :model="searchForm" class="search-form" label-width="80px">
        <el-row :gutter="20">
          <el-col :span="6">
            <el-form-item label="回款编号">
              <el-input v-model="searchForm.paymentNo" placeholder="请输入回款编号" clearable></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="客户名称">
              <el-select v-model="searchForm.customerId" placeholder="请选择客户" clearable>
                <el-option
                  v-for="customer in customerOptions"
                  :key="customer.id"
                  :label="customer.customerName"
                  :value="customer.id"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="回款状态">
              <el-select v-model="searchForm.status" placeholder="请选择回款状态" clearable>
                <el-option label="待回款" value="0"></el-option>
                <el-option label="已回款" value="1"></el-option>
                <el-option label="部分回款" value="2"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="负责人">
              <el-select v-model="searchForm.ownerId" placeholder="请选择负责人" clearable>
                <el-option
                  v-for="user in userOptions"
                  :key="user.id"
                  :label="user.name"
                  :value="user.id"
                ></el-option>
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
            <el-button type="success" @click="handleExport">
              <el-icon><Download /></el-icon>
              <span>导出回款</span>
            </el-button>
          </el-col>
        </el-row>
      </el-form>
      
      <!-- 回款列表 -->
      <el-table
        v-loading="loading"
        :data="paymentList"
        style="width: 100%"
        border
      >
        <el-table-column prop="id" label="回款ID" width="80"></el-table-column>
        <el-table-column prop="paymentNo" label="回款编号" width="150"></el-table-column>
        <el-table-column prop="customerName" label="客户名称" width="150"></el-table-column>
        <el-table-column prop="orderName" label="关联订单" width="150"></el-table-column>
        <el-table-column prop="paymentAmount" label="回款金额" width="120">
          <template #default="scope">
            <span>¥{{ scope.row.paymentAmount.toFixed(2) }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="paymentDate" label="回款日期" width="150"></el-table-column>
        <el-table-column prop="paymentMethod" label="回款方式" width="120">
          <template #default="scope">
            <span>{{ paymentMethodMap[scope.row.paymentMethod] }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100">
          <template #default="scope">
            <el-tag
              :type="statusMap[scope.row.status].type"
              :effect="statusMap[scope.row.status].effect"
            >
              {{ statusMap[scope.row.status].label }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="ownerName" label="负责人" width="100"></el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="180"></el-table-column>
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="scope">
            <el-button type="primary" size="small" @click="handleEditPayment(scope.row)">
              <el-icon><Edit /></el-icon>
              <span>编辑</span>
            </el-button>
            <el-button type="success" size="small" @click="handleViewPayment(scope.row)">
              <el-icon><View /></el-icon>
              <span>详情</span>
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
    
    <!-- 新增/编辑回款对话框 -->
    <el-dialog
      v-model="dialogVisible"
      :title="dialogType === 'add' ? '新增回款' : '编辑回款'"
      width="700px"
    >
      <el-form
        :model="paymentForm"
        :rules="paymentRules"
        ref="paymentFormRef"
        label-width="100px"
      >
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item prop="paymentNo">
              <el-input v-model="paymentForm.paymentNo" placeholder="请输入回款编号"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item prop="customerId">
              <el-select v-model="paymentForm.customerId" placeholder="请选择客户">
                <el-option
                  v-for="customer in customerOptions"
                  :key="customer.id"
                  :label="customer.customerName"
                  :value="customer.id"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item prop="orderId">
              <el-select v-model="paymentForm.orderId" placeholder="请选择关联订单">
                <el-option
                  v-for="order in orderOptions"
                  :key="order.id"
                  :label="order.orderName"
                  :value="order.id"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item prop="paymentAmount">
              <el-input v-model="paymentForm.paymentAmount" type="number" placeholder="请输入回款金额"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item prop="paymentDate">
              <el-date-picker
                v-model="paymentForm.paymentDate"
                type="date"
                placeholder="请选择回款日期"
                style="width: 100%"
              ></el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item prop="paymentMethod">
              <el-select v-model="paymentForm.paymentMethod" placeholder="请选择回款方式">
                <el-option label="银行转账" value="0"></el-option>
                <el-option label="支付宝" value="1"></el-option>
                <el-option label="微信支付" value="2"></el-option>
                <el-option label="现金" value="3"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item prop="status">
              <el-select v-model="paymentForm.status" placeholder="请选择回款状态">
                <el-option label="待回款" value="0"></el-option>
                <el-option label="已回款" value="1"></el-option>
                <el-option label="部分回款" value="2"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item prop="ownerId">
              <el-select v-model="paymentForm.ownerId" placeholder="请选择负责人">
                <el-option
                  v-for="user in userOptions"
                  :key="user.id"
                  :label="user.name"
                  :value="user.id"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item prop="remark">
              <el-input v-model="paymentForm.remark" type="textarea" placeholder="请输入备注信息"></el-input>
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
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus, Search, Refresh, Edit, View, Download } from '@element-plus/icons-vue'

// 搜索表单
const searchForm = reactive({
  paymentNo: '',
  customerId: '',
  status: '',
  ownerId: ''
})

// 分页信息
const pagination = reactive({
  current: 1,
  size: 10,
  total: 0
})

// 加载状态
const loading = ref(false)

// 回款列表
const paymentList = ref([])

// 客户选项
const customerOptions = ref([
  { id: 1, customerName: '阿里巴巴集团' },
  { id: 2, customerName: '腾讯科技' },
  { id: 3, customerName: '百度在线' },
  { id: 4, customerName: '字节跳动' }
])

// 订单选项
const orderOptions = ref([
  { id: 1, orderName: '阿里云服务器采购订单' },
  { id: 2, orderName: '腾讯云存储服务订单' },
  { id: 3, orderName: '百度推广服务订单' },
  { id: 4, orderName: '字节跳动广告投放订单' }
])

// 用户选项
const userOptions = ref([
  { id: 1, name: '管理员' },
  { id: 2, name: '销售1' },
  { id: 3, name: '销售2' }
])

// 回款方式映射
const paymentMethodMap = {
  '0': '银行转账',
  '1': '支付宝',
  '2': '微信支付',
  '3': '现金'
}

// 回款状态映射
const statusMap = {
  '0': { label: '待回款', type: 'warning', effect: 'dark' },
  '1': { label: '已回款', type: 'success', effect: 'dark' },
  '2': { label: '部分回款', type: 'info', effect: 'dark' }
}

// 对话框状态
const dialogVisible = ref(false)
const dialogType = ref('add')

// 表单引用
const paymentFormRef = ref(null)

// 回款表单
const paymentForm = reactive({
  id: '',
  paymentNo: '',
  customerId: '',
  orderId: '',
  paymentAmount: 0,
  paymentDate: '',
  paymentMethod: '0',
  status: '0',
  ownerId: '',
  remark: ''
})

// 表单验证规则
const paymentRules = {
  paymentNo: [
    { required: true, message: '请输入回款编号', trigger: 'blur' },
    { min: 2, max: 50, message: '回款编号长度在 2 到 50 个字符', trigger: 'blur' }
  ],
  customerId: [
    { required: true, message: '请选择客户', trigger: 'blur' }
  ],
  orderId: [
    { required: true, message: '请选择关联订单', trigger: 'blur' }
  ],
  paymentAmount: [
    { required: true, message: '请输入回款金额', trigger: 'blur' },
    { type: 'number', min: 0, message: '回款金额必须大于等于0', trigger: 'blur' }
  ],
  paymentDate: [
    { required: true, message: '请选择回款日期', trigger: 'blur' }
  ],
  paymentMethod: [
    { required: true, message: '请选择回款方式', trigger: 'blur' }
  ],
  status: [
    { required: true, message: '请选择回款状态', trigger: 'blur' }
  ],
  ownerId: [
    { required: true, message: '请选择负责人', trigger: 'blur' }
  ]
}

// 获取回款列表
const getPaymentList = async () => {
  loading.value = true
  try {
    // 模拟API请求
    // 实际项目中需要调用后端API
    setTimeout(() => {
      paymentList.value = [
        {
          id: 1,
          paymentNo: 'PY2023001',
          customerId: 1,
          customerName: '阿里巴巴集团',
          orderId: 1,
          orderName: '阿里云服务器采购订单',
          paymentAmount: 1000000,
          paymentDate: '2023-01-05',
          paymentMethod: '0',
          status: '1',
          ownerId: 1,
          ownerName: '管理员',
          createTime: '2023-01-05 10:00:00'
        },
        {
          id: 2,
          paymentNo: 'PY2023002',
          customerId: 2,
          customerName: '腾讯科技',
          orderId: 2,
          orderName: '腾讯云存储服务订单',
          paymentAmount: 250000,
          paymentDate: '2023-01-06',
          paymentMethod: '1',
          status: '2',
          ownerId: 2,
          ownerName: '销售1',
          createTime: '2023-01-06 10:00:00'
        },
        {
          id: 3,
          paymentNo: 'PY2023003',
          customerId: 3,
          customerName: '百度在线',
          orderId: 3,
          orderName: '百度推广服务订单',
          paymentAmount: 300000,
          paymentDate: '2023-01-07',
          paymentMethod: '2',
          status: '1',
          ownerId: 3,
          ownerName: '销售2',
          createTime: '2023-01-07 10:00:00'
        },
        {
          id: 4,
          paymentNo: 'PY2023004',
          customerId: 4,
          customerName: '字节跳动',
          orderId: 4,
          orderName: '字节跳动广告投放订单',
          paymentAmount: 1000000,
          paymentDate: '2023-01-08',
          paymentMethod: '0',
          status: '0',
          ownerId: 1,
          ownerName: '管理员',
          createTime: '2023-01-08 10:00:00'
        }
      ]
      pagination.total = 4
      loading.value = false
    }, 500)
  } catch (error) {
    console.error('获取回款列表失败:', error)
    ElMessage.error('获取回款列表失败')
    loading.value = false
  }
}

// 搜索
const handleSearch = () => {
  pagination.current = 1
  getPaymentList()
}

// 重置搜索
const handleReset = () => {
  Object.keys(searchForm).forEach(key => {
    searchForm[key] = ''
  })
  pagination.current = 1
  getPaymentList()
}

// 分页大小变化
const handleSizeChange = (size) => {
  pagination.size = size
  getPaymentList()
}

// 分页当前页变化
const handleCurrentChange = (current) => {
  pagination.current = current
  getPaymentList()
}

// 新增回款
const handleAddPayment = () => {
  dialogType.value = 'add'
  Object.keys(paymentForm).forEach(key => {
    paymentForm[key] = ''
  })
  paymentForm.paymentMethod = '0'
  paymentForm.status = '0'
  paymentForm.paymentAmount = 0
  dialogVisible.value = true
}

// 编辑回款
const handleEditPayment = (payment) => {
  dialogType.value = 'edit'
  Object.assign(paymentForm, payment)
  dialogVisible.value = true
}

// 提交表单
const handleSubmit = async () => {
  if (!paymentFormRef.value) return
  
  try {
    await paymentFormRef.value.validate()
    
    // 模拟API请求
    // 实际项目中需要调用后端API
    setTimeout(() => {
      dialogVisible.value = false
      ElMessage.success(dialogType.value === 'add' ? '新增回款成功' : '编辑回款成功')
      getPaymentList()
    }, 500)
  } catch (error) {
    console.error('表单验证失败:', error)
  }
}

// 查看回款详情
const handleViewPayment = (payment) => {
  ElMessage.info('查看回款详情功能开发中')
}

// 导出回款
const handleExport = () => {
  // 模拟API请求
  // 实际项目中需要调用后端API
  setTimeout(() => {
    ElMessage.success('导出回款成功')
  }, 500)
}

// 组件挂载时
onMounted(() => {
  getPaymentList()
})
</script>

<style scoped>
.payment-management {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
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
</style>