<template>
  <div class="contract-management">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>合同管理</span>
          <el-button type="primary" @click="handleAddContract">
            <el-icon><Plus /></el-icon>
            <span>新增合同</span>
          </el-button>
        </div>
      </template>
      
      <!-- 搜索表单 -->
      <el-form :model="searchForm" class="search-form" label-width="80px">
        <el-row :gutter="20">
          <el-col :span="6">
            <el-form-item label="合同编号">
              <el-input v-model="searchForm.contractNo" placeholder="请输入合同编号" clearable></el-input>
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
            <el-form-item label="合同状态">
              <el-select v-model="searchForm.status" placeholder="请选择合同状态" clearable>
                <el-option label="草稿" value="0"></el-option>
                <el-option label="生效" value="1"></el-option>
                <el-option label="终止" value="2"></el-option>
                <el-option label="过期" value="3"></el-option>
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
              <span>导出合同</span>
            </el-button>
          </el-col>
        </el-row>
      </el-form>
      
      <!-- 合同列表 -->
      <el-table
        v-loading="loading"
        :data="contractList"
        style="width: 100%"
        border
      >
        <el-table-column prop="id" label="合同ID" width="80"></el-table-column>
        <el-table-column prop="contractNo" label="合同编号" width="150"></el-table-column>
        <el-table-column prop="contractName" label="合同名称" width="200"></el-table-column>
        <el-table-column prop="customerName" label="客户名称" width="150"></el-table-column>
        <el-table-column prop="amount" label="合同金额" width="120">
          <template #default="scope">
            <span>¥{{ scope.row.amount.toFixed(2) }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="startDate" label="开始日期" width="150"></el-table-column>
        <el-table-column prop="endDate" label="结束日期" width="150"></el-table-column>
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
            <el-button type="primary" size="small" @click="handleEditContract(scope.row)">
              <el-icon><Edit /></el-icon>
              <span>编辑</span>
            </el-button>
            <el-button type="success" size="small" @click="handleViewContract(scope.row)">
              <el-icon><View /></el-icon>
              <span>详情</span>
            </el-button>
            <el-button type="warning" size="small" @click="handleUpdateStatus(scope.row)">
              <el-icon><Top /></el-icon>
              <span>更新状态</span>
            </el-button>
            <el-button type="info" size="small" @click="handleUploadAttachment(scope.row)">
              <el-icon><Upload /></el-icon>
              <span>附件</span>
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
    
    <!-- 新增/编辑合同对话框 -->
    <el-dialog
      v-model="dialogVisible"
      :title="dialogType === 'add' ? '新增合同' : '编辑合同'"
      width="700px"
    >
      <el-form
        :model="contractForm"
        :rules="contractRules"
        ref="contractFormRef"
        label-width="100px"
      >
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item prop="contractNo">
              <el-input v-model="contractForm.contractNo" placeholder="请输入合同编号"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item prop="contractName">
              <el-input v-model="contractForm.contractName" placeholder="请输入合同名称"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item prop="customerId">
              <el-select v-model="contractForm.customerId" placeholder="请选择客户">
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
            <el-form-item prop="opportunityId">
              <el-select v-model="contractForm.opportunityId" placeholder="请选择关联商机">
                <el-option
                  v-for="opportunity in opportunityOptions"
                  :key="opportunity.id"
                  :label="opportunity.opportunityName"
                  :value="opportunity.id"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item prop="amount">
              <el-input v-model="contractForm.amount" type="number" placeholder="请输入合同金额"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item prop="startDate">
              <el-date-picker
                v-model="contractForm.startDate"
                type="date"
                placeholder="请选择开始日期"
                style="width: 100%"
              ></el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item prop="endDate">
              <el-date-picker
                v-model="contractForm.endDate"
                type="date"
                placeholder="请选择结束日期"
                style="width: 100%"
              ></el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item prop="ownerId">
              <el-select v-model="contractForm.ownerId" placeholder="请选择负责人">
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
              <el-select v-model="contractForm.status" placeholder="请选择状态">
                <el-option label="草稿" value="0"></el-option>
                <el-option label="生效" value="1"></el-option>
                <el-option label="终止" value="2"></el-option>
                <el-option label="过期" value="3"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item prop="description">
              <el-input v-model="contractForm.description" type="textarea" placeholder="请输入合同描述"></el-input>
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
      title="更新合同状态"
      width="400px"
    >
      <el-form :model="updateStatusForm" :rules="updateStatusRules" ref="updateStatusFormRef">
        <el-form-item label="合同状态" prop="status">
          <el-select v-model="updateStatusForm.status" placeholder="请选择合同状态">
            <el-option label="草稿" value="0"></el-option>
            <el-option label="生效" value="1"></el-option>
            <el-option label="终止" value="2"></el-option>
            <el-option label="过期" value="3"></el-option>
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
    
    <!-- 附件上传对话框 -->
    <el-dialog
      v-model="uploadAttachmentVisible"
      title="上传合同附件"
      width="500px"
    >
      <el-upload
        class="upload-demo"
        action="#"
        :on-preview="handlePreview"
        :on-remove="handleRemove"
        :file-list="fileList"
        :auto-upload="false"
        :limit="5"
        :on-exceed="handleExceed"
      >
        <el-button type="primary" @click="$refs.upload.$refs.input.click()">
          <el-icon><Upload /></el-icon>
          <span>选择文件</span>
        </el-button>
        <template #tip>
          <div class="el-upload__tip">
            只能上传jpg/png文件，且不超过5MB
          </div>
        </template>
      </el-upload>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="uploadAttachmentVisible = false">取消</el-button>
          <el-button type="primary" @click="handleUploadFiles">上传</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus, Search, Refresh, Edit, View, Top, Upload, Download } from '@element-plus/icons-vue'

// 搜索表单
const searchForm = reactive({
  contractNo: '',
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

// 合同列表
const contractList = ref([])

// 客户选项
const customerOptions = ref([
  { id: 1, customerName: '阿里巴巴集团' },
  { id: 2, customerName: '腾讯科技' },
  { id: 3, customerName: '百度在线' },
  { id: 4, customerName: '字节跳动' }
])

// 商机选项
const opportunityOptions = ref([
  { id: 1, opportunityName: '阿里云服务器采购' },
  { id: 2, opportunityName: '腾讯云存储服务' },
  { id: 3, opportunityName: '百度推广服务' },
  { id: 4, opportunityName: '字节跳动广告投放' }
])

// 用户选项
const userOptions = ref([
  { id: 1, name: '管理员' },
  { id: 2, name: '销售1' },
  { id: 3, name: '销售2' }
])

// 合同状态映射
const statusMap = {
  '0': { label: '草稿', type: 'info', effect: 'dark' },
  '1': { label: '生效', type: 'success', effect: 'dark' },
  '2': { label: '终止', type: 'danger', effect: 'dark' },
  '3': { label: '过期', type: 'warning', effect: 'dark' }
}

// 对话框状态
const dialogVisible = ref(false)
const updateStatusVisible = ref(false)
const uploadAttachmentVisible = ref(false)
const dialogType = ref('add')

// 表单引用
const contractFormRef = ref(null)
const updateStatusFormRef = ref(null)

// 合同表单
const contractForm = reactive({
  id: '',
  contractNo: '',
  contractName: '',
  customerId: '',
  opportunityId: '',
  amount: 0,
  startDate: '',
  endDate: '',
  ownerId: '',
  status: '0',
  description: ''
})

// 更新状态表单
const updateStatusForm = reactive({
  status: '',
  remark: ''
})

// 文件列表
const fileList = ref([])

// 表单验证规则
const contractRules = {
  contractNo: [
    { required: true, message: '请输入合同编号', trigger: 'blur' },
    { min: 2, max: 50, message: '合同编号长度在 2 到 50 个字符', trigger: 'blur' }
  ],
  contractName: [
    { required: true, message: '请输入合同名称', trigger: 'blur' },
    { min: 2, max: 100, message: '合同名称长度在 2 到 100 个字符', trigger: 'blur' }
  ],
  customerId: [
    { required: true, message: '请选择客户', trigger: 'blur' }
  ],
  amount: [
    { required: true, message: '请输入合同金额', trigger: 'blur' },
    { type: 'number', min: 0, message: '合同金额必须大于等于0', trigger: 'blur' }
  ],
  startDate: [
    { required: true, message: '请选择开始日期', trigger: 'blur' }
  ],
  endDate: [
    { required: true, message: '请选择结束日期', trigger: 'blur' }
  ],
  ownerId: [
    { required: true, message: '请选择负责人', trigger: 'blur' }
  ],
  status: [
    { required: true, message: '请选择状态', trigger: 'blur' }
  ]
}

// 更新状态验证规则
const updateStatusRules = {
  status: [
    { required: true, message: '请选择合同状态', trigger: 'blur' }
  ]
}

// 获取合同列表
const getContractList = async () => {
  loading.value = true
  try {
    // 模拟API请求
    // 实际项目中需要调用后端API
    setTimeout(() => {
      contractList.value = [
        {
          id: 1,
          contractNo: 'HT2023001',
          contractName: '阿里云服务器采购合同',
          customerId: 1,
          customerName: '阿里巴巴集团',
          opportunityId: 1,
          amount: 1000000,
          startDate: '2023-01-01',
          endDate: '2024-01-01',
          status: '1',
          ownerId: 1,
          ownerName: '管理员',
          createTime: '2023-01-01 10:00:00'
        },
        {
          id: 2,
          contractNo: 'HT2023002',
          contractName: '腾讯云存储服务合同',
          customerId: 2,
          customerName: '腾讯科技',
          opportunityId: 2,
          amount: 500000,
          startDate: '2023-01-02',
          endDate: '2024-01-02',
          status: '0',
          ownerId: 2,
          ownerName: '销售1',
          createTime: '2023-01-02 10:00:00'
        },
        {
          id: 3,
          contractNo: 'HT2023003',
          contractName: '百度推广服务合同',
          customerId: 3,
          customerName: '百度在线',
          opportunityId: 3,
          amount: 300000,
          startDate: '2023-01-03',
          endDate: '2024-01-03',
          status: '1',
          ownerId: 3,
          ownerName: '销售2',
          createTime: '2023-01-03 10:00:00'
        },
        {
          id: 4,
          contractNo: 'HT2023004',
          contractName: '字节跳动广告投放合同',
          customerId: 4,
          customerName: '字节跳动',
          opportunityId: 4,
          amount: 2000000,
          startDate: '2023-01-04',
          endDate: '2024-01-04',
          status: '2',
          ownerId: 1,
          ownerName: '管理员',
          createTime: '2023-01-04 10:00:00'
        }
      ]
      pagination.total = 4
      loading.value = false
    }, 500)
  } catch (error) {
    console.error('获取合同列表失败:', error)
    ElMessage.error('获取合同列表失败')
    loading.value = false
  }
}

// 搜索
const handleSearch = () => {
  pagination.current = 1
  getContractList()
}

// 重置搜索
const handleReset = () => {
  Object.keys(searchForm).forEach(key => {
    searchForm[key] = ''
  })
  pagination.current = 1
  getContractList()
}

// 分页大小变化
const handleSizeChange = (size) => {
  pagination.size = size
  getContractList()
}

// 分页当前页变化
const handleCurrentChange = (current) => {
  pagination.current = current
  getContractList()
}

// 新增合同
const handleAddContract = () => {
  dialogType.value = 'add'
  Object.keys(contractForm).forEach(key => {
    contractForm[key] = ''
  })
  contractForm.status = '0'
  contractForm.amount = 0
  dialogVisible.value = true
}

// 编辑合同
const handleEditContract = (contract) => {
  dialogType.value = 'edit'
  Object.assign(contractForm, contract)
  dialogVisible.value = true
}

// 提交表单
const handleSubmit = async () => {
  if (!contractFormRef.value) return
  
  try {
    await contractFormRef.value.validate()
    
    // 模拟API请求
    // 实际项目中需要调用后端API
    setTimeout(() => {
      dialogVisible.value = false
      ElMessage.success(dialogType.value === 'add' ? '新增合同成功' : '编辑合同成功')
      getContractList()
    }, 500)
  } catch (error) {
    console.error('表单验证失败:', error)
  }
}

// 查看合同详情
const handleViewContract = (contract) => {
  ElMessage.info('查看合同详情功能开发中')
}

// 更新合同状态
const handleUpdateStatus = (contract) => {
  updateStatusForm.status = contract.status
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
      ElMessage.success('更新合同状态成功')
      getContractList()
    }, 500)
  } catch (error) {
    console.error('表单验证失败:', error)
  }
}

// 上传附件
const handleUploadAttachment = (contract) => {
  fileList.value = []
  uploadAttachmentVisible.value = true
}

// 处理文件预览
const handlePreview = (file) => {
  console.log('预览文件:', file)
}

// 处理文件移除
const handleRemove = (file, fileList) => {
  console.log('移除文件:', file, fileList)
}

// 处理文件超出限制
const handleExceed = (files, fileList) => {
  ElMessage.warning('最多只能上传5个文件')
}

// 上传文件
const handleUploadFiles = () => {
  // 模拟API请求
  // 实际项目中需要调用后端API
  setTimeout(() => {
    uploadAttachmentVisible.value = false
    ElMessage.success('上传附件成功')
  }, 500)
}

// 导出合同
const handleExport = () => {
  // 模拟API请求
  // 实际项目中需要调用后端API
  setTimeout(() => {
    ElMessage.success('导出合同成功')
  }, 500)
}

// 组件挂载时
onMounted(() => {
  getContractList()
})
</script>

<style scoped>
.contract-management {
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

.upload-demo {
  margin-bottom: 20px;
}
</style>