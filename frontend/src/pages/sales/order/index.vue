<template>
  <div class="order-management">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>订单管理</span>
          <el-button type="primary" @click="handleAddOrder">
            <el-icon><Plus /></el-icon>
            <span>新增订单</span>
          </el-button>
        </div>
      </template>
      
      <!-- 搜索表单 -->
      <el-form :model="searchForm" class="search-form" label-width="80px">
        <el-row :gutter="20">
          <el-col :span="6">
            <el-form-item label="订单编号">
              <el-input v-model="searchForm.orderNo" placeholder="请输入订单编号" clearable></el-input>
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
            <el-form-item label="订单状态">
              <el-select v-model="searchForm.status" placeholder="请选择订单状态" clearable>
                <el-option label="待付款" value="0"></el-option>
                <el-option label="已付款" value="1"></el-option>
                <el-option label="待发货" value="2"></el-option>
                <el-option label="已完成" value="3"></el-option>
                <el-option label="已取消" value="4"></el-option>
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
              <span>导出订单</span>
            </el-button>
          </el-col>
        </el-row>
      </el-form>
      
      <!-- 订单列表 -->
      <el-table
        v-loading="loading"
        :data="orderList"
        style="width: 100%"
        border
      >
        <el-table-column prop="id" label="订单ID" width="80"></el-table-column>
        <el-table-column prop="orderNo" label="订单编号" width="150"></el-table-column>
        <el-table-column prop="orderName" label="订单名称" width="200"></el-table-column>
        <el-table-column prop="customerName" label="客户名称" width="150"></el-table-column>
        <el-table-column prop="contractName" label="关联合同" width="150"></el-table-column>
        <el-table-column prop="amount" label="订单金额" width="120">
          <template #default="scope">
            <span>¥{{ scope.row.amount.toFixed(2) }}</span>
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
        <el-table-column label="操作" width="300" fixed="right">
          <template #default="scope">
            <el-button type="primary" size="small" @click="handleEditOrder(scope.row)">
              <el-icon><Edit /></el-icon>
              <span>编辑</span>
            </el-button>
            <el-button type="success" size="small" @click="handleViewOrder(scope.row)">
              <el-icon><View /></el-icon>
              <span>详情</span>
            </el-button>
            <el-button type="warning" size="small" @click="handleUpdateStatus(scope.row)">
              <el-icon><Top /></el-icon>
              <span>更新状态</span>
            </el-button>
            <el-button type="info" size="small" @click="handleAddPayment(scope.row)">
              <el-icon><Money /></el-icon>
              <span>回款</span>
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
    
    <!-- 新增/编辑订单对话框 -->
    <el-dialog
      v-model="dialogVisible"
      :title="dialogType === 'add' ? '新增订单' : '编辑订单'"
      width="700px"
    >
      <el-form
        :model="orderForm"
        :rules="orderRules"
        ref="orderFormRef"
        label-width="100px"
      >
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item prop="orderNo">
              <el-input v-model="orderForm.orderNo" placeholder="请输入订单编号"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item prop="orderName">
              <el-input v-model="orderForm.orderName" placeholder="请输入订单名称"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item prop="customerId">
              <el-select v-model="orderForm.customerId" placeholder="请选择客户">
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
            <el-form-item prop="contractId">
              <el-select v-model="orderForm.contractId" placeholder="请选择关联合同">
                <el-option
                  v-for="contract in contractOptions"
                  :key="contract.id"
                  :label="contract.contractName"
                  :value="contract.id"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item prop="amount">
              <el-input v-model="orderForm.amount" type="number" placeholder="请输入订单金额"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item prop="orderDate">
              <el-date-picker
                v-model="orderForm.orderDate"
                type="date"
                placeholder="请选择订单日期"
                style="width: 100%"
              ></el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item prop="ownerId">
              <el-select v-model="orderForm.ownerId" placeholder="请选择负责人">
                <el-option
                  v-for="user in userOptions"
                  :key="user.id"
                  :label="user.name"
                  :value="user.id"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item prop="status">
              <el-select v-model="orderForm.status" placeholder="请选择订单状态">
                <el-option label="待付款" value="0"></el-option>
                <el-option label="已付款" value="1"></el-option>
                <el-option label="待发货" value="2"></el-option>
                <el-option label="已完成" value="3"></el-option>
                <el-option label="已取消" value="4"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item prop="description">
              <el-input v-model="orderForm.description" type="textarea" placeholder="请输入订单描述"></el-input>
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
    
    <!-- 更新状态对话框 -->
    <el-dialog
      v-model="updateStatusVisible"
      title="更新订单状态"
      width="400px"
    >
      <el-form :model="updateStatusForm" :rules="updateStatusRules" ref="updateStatusFormRef">
        <el-form-item label="订单状态" prop="status">
          <el-select v-model="updateStatusForm.status" placeholder="请选择订单状态">
            <el-option label="待付款" value="0"></el-option>
            <el-option label="已付款" value="1"></el-option>
            <el-option label="待发货" value="2"></el-option>
            <el-option label="已完成" value="3"></el-option>
            <el-option label="已取消" value="4"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="updateStatusForm.remark" type="textarea" placeholder="请输入备注信息"></el-input>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="updateStatusVisible = false">取消</el-button>
          <el-button type="primary" @click="handleUpdateStatusSubmit">确定</el-button>
        </span>
      </template>
    </el-dialog>
    
    <!-- 新增回款对话框 -->
    <el-dialog
      v-model="addPaymentVisible"
      title="新增回款记录"
      width="500px"
    >
      <el-form
        :model="paymentForm"
        :rules="paymentRules"
        ref="paymentFormRef"
        label-width="100px"
      >
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item prop="paymentAmount">
              <el-input v-model="paymentForm.paymentAmount" type="number" placeholder="请输入回款金额"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item prop="paymentDate">
              <el-date-picker
                v-model="paymentForm.paymentDate"
                type="date"
                placeholder="请选择回款日期"
                style="width: 100%"
              ></el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item prop="paymentMethod">
              <el-select v-model="paymentForm.paymentMethod" placeholder="请选择回款方式">
                <el-option label="银行转账" value="0"></el-option>
                <el-option label="支付宝" value="1"></el-option>
                <el-option label="微信支付" value="2"></el-option>
                <el-option label="现金" value="3"></el-option>
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
          <el-button @click="addPaymentVisible = false">取消</el-button>
          <el-button type="primary" @click="handleAddPaymentSubmit">确定</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus, Search, Refresh, Edit, View, Top, Money, Download } from '@element-plus/icons-vue'

// 搜索表单
const searchForm = reactive({
  orderNo: '',
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

// 订单列表
const orderList = ref([])

// 客户选项
const customerOptions = ref([
  { id: 1, customerName: '阿里巴巴集团' },
  { id: 2, customerName: '腾讯科技' },
  { id: 3, customerName: '百度在线' },
  { id: 4, customerName: '字节跳动' }
])

// 合同选项
const contractOptions = ref([
  { id: 1, contractName: '阿里云服务器采购合同' },
  { id: 2, contractName: '腾讯云存储服务合同' },
  { id: 3, contractName: '百度推广服务合同' },
  { id: 4, contractName: '字节跳动广告投放合同' }
])

// 用户选项
const userOptions = ref([
  { id: 1, name: '管理员' },
  { id: 2, name: '销售1' },
  { id: 3, name: '销售2' }
])

// 订单状态映射
const statusMap = {
  '0': { label: '待付款', type: 'warning', effect: 'dark' },
  '1': { label: '已付款', type: 'success', effect: 'dark' },
  '2': { label: '待发货', type: 'info', effect: 'dark' },
  '3': { label: '已完成', type: 'primary', effect: 'dark' },
  '4': { label: '已取消', type: 'danger', effect: 'dark' }
}

// 对话框状态
const dialogVisible = ref(false)
const updateStatusVisible = ref(false)
const addPaymentVisible = ref(false)
const dialogType = ref('add')

// 表单引用
const orderFormRef = ref(null)
const updateStatusFormRef = ref(null)
const paymentFormRef = ref(null)

// 订单表单
const orderForm = reactive({
  id: '',
  orderNo: '',
  orderName: '',
  customerId: '',
  contractId: '',
  amount: 0,
  orderDate: '',
  ownerId: '',
  status: '0',
  description: ''
})

// 更新状态表单
const updateStatusForm = reactive({
  status: '',
  remark: ''
})

// 回款表单
const paymentForm = reactive({
  paymentAmount: 0,
  paymentDate: '',
  paymentMethod: '0',
  remark: ''
})

// 表单验证规则
const orderRules = {
  orderNo: [
    { required: true, message: '请输入订单编号', trigger: 'blur' },
    { min: 2, max: 50, message: '订单编号长度在 2 到 50 个字符', trigger: 'blur' }
  ],
  orderName: [
    { required: true, message: '请输入订单名称', trigger: 'blur' },
    { min: 2, max: 100, message: '订单名称长度在 2 到 100 个字符', trigger: 'blur' }
  ],
  customerId: [
    { required: true, message: '请选择客户', trigger: 'blur' }
  ],
  contractId: [
    { required: true, message: '请选择关联合同', trigger: 'blur' }
  ],
  amount: [
    { required: true, message: '请输入订单金额', trigger: 'blur' },
    { type: 'number', min: 0, message: '订单金额必须大于等于0', trigger: 'blur' }
  ],
  orderDate: [
    { required: true, message: '请选择订单日期', trigger: 'blur' }
  ],
  ownerId: [
    { required: true, message: '请选择负责人', trigger: 'blur' }
  ],
  status: [
    { required: true, message: '请选择订单状态', trigger: 'blur' }
  ]
}

// 更新状态验证规则
const updateStatusRules = {
  status: [
    { required: true, message: '请选择订单状态', trigger: 'blur' }
  ]
}

// 回款验证规则
const paymentRules = {
  paymentAmount: [
    { required: true, message: '请输入回款金额', trigger: 'blur' },
    { type: 'number', min: 0, message: '回款金额必须大于等于0', trigger: 'blur' }
  ],
  paymentDate: [
    { required: true, message: '请选择回款日期', trigger: 'blur' }
  ],
  paymentMethod: [
    { required: true, message: '请选择回款方式', trigger: 'blur' }
  ]
}

// 获取订单列表
const getOrderList = async () => {
  loading.value = true
  try {
    // 模拟API请求
    // 实际项目中需要调用后端API
    setTimeout(() => {
      orderList.value = [
        {
          id: 1,
          orderNo: 'OD2023001',
          orderName: '阿里云服务器采购订单',
          customerId: 1,
          customerName: '阿里巴巴集团',
          contractId: 1,
          contractName: '阿里云服务器采购合同',
          amount: 1000000,
          status: '1',
          ownerId: 1,
          ownerName: '管理员',
          createTime: '2023-01-01 10:00:00'
        },
        {
          id: 2,
          orderNo: 'OD2023002',
          orderName: '腾讯云存储服务订单',
          customerId: 2,
          customerName: '腾讯科技',
          contractId: 2,
          contractName: '腾讯云存储服务合同',
          amount: 500000,
          status: '0',
          ownerId: 2,
          ownerName: '销售1',
          createTime: '2023-01-02 10:00:00'
        },
        {
          id: 3,
          orderNo: 'OD2023003',
          orderName: '百度推广服务订单',
          customerId: 3,
          customerName: '百度在线',
          contractId: 3,
          contractName: '百度推广服务合同',
          amount: 300000,
          status: '2',
          ownerId: 3,
          ownerName: '销售2',
          createTime: '2023-01-03 10:00:00'
        },
        {
          id: 4,
          orderNo: 'OD2023004',
          orderName: '字节跳动广告投放订单',
          customerId: 4,
          customerName: '字节跳动',
          contractId: 4,
          contractName: '字节跳动广告投放合同',
          amount: 2000000,
          status: '3',
          ownerId: 1,
          ownerName: '管理员',
          createTime: '2023-01-04 10:00:00'
        }
      ]
      pagination.total = 4
      loading.value = false
    }, 500)
  } catch (error) {
    console.error('获取订单列表失败:', error)
    ElMessage.error('获取订单列表失败')
    loading.value = false
  }
}

// 搜索
const handleSearch = () => {
  pagination.current = 1
  getOrderList()
}

// 重置搜索
const handleReset = () => {
  Object.keys(searchForm).forEach(key => {
    searchForm[key] = ''
  })
  pagination.current = 1
  getOrderList()
}

// 分页大小变化
const handleSizeChange = (size) => {
  pagination.size = size
  getOrderList()
}

// 分页当前页变化
const handleCurrentChange = (current) => {
  pagination.current = current
  getOrderList()
}

// 新增订单
const handleAddOrder = () => {
  dialogType.value = 'add'
  Object.keys(orderForm).forEach(key => {
    orderForm[key] = ''
  })
  orderForm.status = '0'
  orderForm.amount = 0
  dialogVisible.value = true
}

// 编辑订单
const handleEditOrder = (order) => {
  dialogType.value = 'edit'
  Object.assign(orderForm, order)
  dialogVisible.value = true
}

// 提交表单
const handleSubmit = async () => {
  if (!orderFormRef.value) return
  
  try {
    await orderFormRef.value.validate()
    
    // 模拟API请求
    // 实际项目中需要调用后端API
    setTimeout(() => {
      dialogVisible.value = false
      ElMessage.success(dialogType.value === 'add' ? '新增订单成功' : '编辑订单成功')
      getOrderList()
    }, 500)
  } catch (error) {
    console.error('表单验证失败:', error)
  }
}

// 查看订单详情
const handleViewOrder = (order) => {
  ElMessage.info('查看订单详情功能开发中')
}

// 更新订单状态
const handleUpdateStatus = (order) => {
  updateStatusForm.status = order.status
  updateStatusForm.remark = ''
  updateStatusVisible.value = true
}

// 提交更新状态
const handleUpdateStatusSubmit = async () => {
  if (!updateStatusFormRef.value) return
  
  try {
    await updateStatusFormRef.value.validate()
    
    // 模拟API请求
    // 实际项目中需要调用后端API
    setTimeout(() => {
      updateStatusVisible.value = false
      ElMessage.success('更新订单状态成功')
      getOrderList()
    }, 500)
  } catch (error) {
    console.error('表单验证失败:', error)
  }
}

// 新增回款
const handleAddPayment = (order) => {
  Object.keys(paymentForm).forEach(key => {
    paymentForm[key] = ''
  })
  paymentForm.paymentAmount = 0
  paymentForm.paymentMethod = '0'
  addPaymentVisible.value = true
}

// 提交回款
const handleAddPaymentSubmit = async () => {
  if (!paymentFormRef.value) return
  
  try {
    await paymentFormRef.value.validate()
    
    // 模拟API请求
    // 实际项目中需要调用后端API
    setTimeout(() => {
      addPaymentVisible.value = false
      ElMessage.success('新增回款记录成功')
    }, 500)
  } catch (error) {
    console.error('表单验证失败:', error)
  }
}

// 导出订单
const handleExport = () => {
  // 模拟API请求
  // 实际项目中需要调用后端API
  setTimeout(() => {
    ElMessage.success('导出订单成功')
  }, 500)
}

// 组件挂载时
onMounted(() => {
  getOrderList()
})
</script>

<style scoped>
.order-management {
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