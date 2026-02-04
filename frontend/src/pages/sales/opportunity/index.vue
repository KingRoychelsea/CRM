<template>
  <div class="opportunity-management">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>商机管理</span>
          <el-button type="primary" @click="handleAddOpportunity">
            <el-icon><Plus /></el-icon>
            <span>新增商机</span>
          </el-button>
        </div>
      </template>
      
      <!-- 搜索表单 -->
      <el-form :model="searchForm" class="search-form" label-width="80px">
        <el-row :gutter="20">
          <el-col :span="6">
            <el-form-item label="商机名称">
              <el-input v-model="searchForm.opportunityName" placeholder="请输入商机名称" clearable></el-input>
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
            <el-form-item label="商机阶段">
              <el-select v-model="searchForm.stage" placeholder="请选择商机阶段" clearable>
                <el-option label="初步接洽" value="0"></el-option>
                <el-option label="需求确认" value="1"></el-option>
                <el-option label="方案报价" value="2"></el-option>
                <el-option label="合同签订" value="3"></el-option>
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
          </el-col>
        </el-row>
      </el-form>
      
      <!-- 商机列表 -->
      <el-table
        v-loading="loading"
        :data="opportunityList"
        style="width: 100%"
        border
      >
        <el-table-column prop="id" label="商机ID" width="80"></el-table-column>
        <el-table-column prop="opportunityName" label="商机名称" width="200"></el-table-column>
        <el-table-column prop="customerName" label="客户名称" width="150"></el-table-column>
        <el-table-column prop="stage" label="商机阶段" width="120">
          <template #default="scope">
            <el-tag
              :type="stageMap[scope.row.stage] ? stageMap[scope.row.stage].type : 'info'"
              :effect="stageMap[scope.row.stage] ? stageMap[scope.row.stage].effect : 'dark'"
            >
              {{ stageMap[scope.row.stage] ? stageMap[scope.row.stage].label : '未知阶段' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="expectedAmount" label="商机金额" width="120">
          <template #default="scope">
            <span>¥{{ scope.row.expectedAmount ? scope.row.expectedAmount.toFixed(2) : '0.00' }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="ownerName" label="负责人" width="100"></el-table-column>
        <el-table-column prop="nextFollowTime" label="下次跟进" width="180"></el-table-column>
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
        <el-table-column label="操作" width="300" fixed="right">
          <template #default="scope">
            <el-button type="primary" size="small" @click="handleEditOpportunity(scope.row)">
              <el-icon><Edit /></el-icon>
              <span>编辑</span>
            </el-button>
            <el-button type="success" size="small" @click="handleViewOpportunity(scope.row)">
              <el-icon><View /></el-icon>
              <span>详情</span>
            </el-button>
            <el-button type="warning" size="small" @click="handleUpdateStage(scope.row)">
              <el-icon><Top /></el-icon>
              <span>更新阶段</span>
            </el-button>
            <el-button type="info" size="small" @click="handleAddFollow(scope.row)">
              <el-icon><ChatDotSquare /></el-icon>
              <span>跟进</span>
            </el-button>
            <el-button type="danger" size="small" @click="handleCloseOpportunity(scope.row)">
              <el-icon><SwitchButton /></el-icon>
              <span>关闭</span>
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
    
    <!-- 新增/编辑商机对话框 -->
    <el-dialog
      v-model="dialogVisible"
      :title="dialogType === 'add' ? '新增商机' : '编辑商机'"
      width="700px"
    >
      <el-form
        :model="opportunityForm"
        :rules="opportunityRules"
        ref="opportunityFormRef"
        label-width="100px"
      >
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item prop="opportunityName">
              <el-input v-model="opportunityForm.opportunityName" placeholder="请输入商机名称"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item prop="customerId">
              <el-select v-model="opportunityForm.customerId" placeholder="请选择客户">
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
            <el-form-item prop="stage">
              <el-select v-model="opportunityForm.stage" placeholder="请选择商机阶段">
                <el-option label="初步接洽" value="0"></el-option>
                <el-option label="需求确认" value="1"></el-option>
                <el-option label="方案报价" value="2"></el-option>
                <el-option label="合同签订" value="3"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item prop="amount">
              <el-input v-model="opportunityForm.amount" type="number" placeholder="请输入商机金额"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item prop="ownerId">
              <el-select v-model="opportunityForm.ownerId" placeholder="请选择负责人">
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
            <el-form-item prop="nextFollowTime">
              <el-date-picker
                v-model="opportunityForm.nextFollowTime"
                type="datetime"
                placeholder="请选择下次跟进时间"
                style="width: 100%"
              ></el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item prop="description">
              <el-input v-model="opportunityForm.description" type="textarea" placeholder="请输入商机描述"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item prop="status">
              <el-select v-model="opportunityForm.status" placeholder="请选择状态">
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
    
    <!-- 更新阶段对话框 -->
    <el-dialog
      v-model="updateStageVisible"
      title="更新商机阶段"
      width="400px"
    >
      <el-form :model="updateStageForm" :rules="updateStageRules" ref="updateStageFormRef">
        <el-form-item label="商机阶段" prop="stage">
          <el-select v-model="updateStageForm.stage" placeholder="请选择商机阶段">
            <el-option label="初步接洽" value="0"></el-option>
            <el-option label="需求确认" value="1"></el-option>
            <el-option label="方案报价" value="2"></el-option>
            <el-option label="合同签订" value="3"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="updateStageForm.remark" type="textarea" placeholder="请输入备注信息"></el-input>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="updateStageVisible = false">取消</el-button>
          <el-button type="primary" @click="handleUpdateStageSubmit">确定</el-button>
        </span>
      </template>
    </el-dialog>
    
    <!-- 关闭商机对话框 -->
    <el-dialog
      v-model="closeOpportunityVisible"
      title="关闭商机"
      width="400px"
    >
      <el-form :model="closeOpportunityForm" :rules="closeOpportunityRules" ref="closeOpportunityFormRef">
        <el-form-item label="关闭原因" prop="closeReason">
          <el-input v-model="closeOpportunityForm.closeReason" type="textarea" placeholder="请输入关闭原因"></el-input>
        </el-form-item>
        <el-form-item label="是否转化为合同">
          <el-checkbox v-model="closeOpportunityForm.convertToContract"></el-checkbox>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="closeOpportunityVisible = false">取消</el-button>
          <el-button type="primary" @click="handleCloseOpportunitySubmit">确定</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, computed } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus, Search, Refresh, Edit, View, Top, ChatDotSquare, SwitchButton, Delete } from '@element-plus/icons-vue'
import businessOpportunityApi from '@/api/sales/businessOpportunity'

// 搜索表单
const searchForm = reactive({
  opportunityName: '',
  customerId: '',
  stage: '',
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

// 商机列表
const opportunityList = ref([])

// 客户选项
const customerOptions = ref([
  { id: 1, customerName: '阿里巴巴集团' },
  { id: 2, customerName: '腾讯科技' },
  { id: 3, customerName: '百度在线' },
  { id: 4, customerName: '字节跳动' }
])

// 用户选项
const userOptions = ref([
  { id: 1, name: '管理员' },
  { id: 2, name: '销售1' },
  { id: 3, name: '销售2' }
])

// 商机阶段映射
const stageMap = {
  1: { label: '初步接洽', type: 'info', effect: 'dark' },
  2: { label: '需求确认', type: 'warning', effect: 'dark' },
  3: { label: '方案报价', type: 'success', effect: 'dark' },
  4: { label: '合同签订', type: 'primary', effect: 'dark' }
}

// 对话框状态
const dialogVisible = ref(false)
const updateStageVisible = ref(false)
const closeOpportunityVisible = ref(false)
const dialogType = ref('add')

// 表单引用
const opportunityFormRef = ref(null)
const updateStageFormRef = ref(null)
const closeOpportunityFormRef = ref(null)

// 商机表单
const opportunityForm = reactive({
  id: '',
  opportunityName: '',
  customerId: '',
  stage: '0',
  amount: 0,
  ownerId: '',
  nextFollowTime: '',
  description: '',
  status: '1'
})

// 更新阶段表单
const updateStageForm = reactive({
  stage: '',
  remark: ''
})

// 关闭商机表单
const closeOpportunityForm = reactive({
  closeReason: '',
  convertToContract: false
})

// 表单验证规则
const opportunityRules = {
  opportunityName: [
    { required: true, message: '请输入商机名称', trigger: 'blur' },
    { min: 2, max: 50, message: '商机名称长度在 2 到 50 个字符', trigger: 'blur' }
  ],
  customerId: [
    { required: true, message: '请选择客户', trigger: 'blur' }
  ],
  stage: [
    { required: true, message: '请选择商机阶段', trigger: 'blur' }
  ],
  amount: [
    { required: true, message: '请输入商机金额', trigger: 'blur' },
    { type: 'number', min: 0, message: '商机金额必须大于等于0', trigger: 'blur' }
  ],
  ownerId: [
    { required: true, message: '请选择负责人', trigger: 'blur' }
  ],
  nextFollowTime: [
    { required: true, message: '请选择下次跟进时间', trigger: 'blur' }
  ],
  status: [
    { required: true, message: '请选择状态', trigger: 'blur' }
  ]
}

// 更新阶段验证规则
const updateStageRules = {
  stage: [
    { required: true, message: '请选择商机阶段', trigger: 'blur' }
  ]
}

// 关闭商机验证规则
const closeOpportunityRules = {
  closeReason: [
    { required: true, message: '请输入关闭原因', trigger: 'blur' },
    { min: 5, max: 200, message: '关闭原因长度在 5 到 200 个字符', trigger: 'blur' }
  ]
}

// 获取商机列表
const getOpportunityList = async () => {
  loading.value = true
  try {
    const params = {
      ...searchForm,
      pageNum: pagination.current,
      pageSize: pagination.size
    }
    const response = await businessOpportunityApi.getBusinessOpportunityList(params)
    opportunityList.value = response.data.records
    pagination.total = response.data.total
  } catch (error) {
    console.error('获取商机列表失败:', error)
    ElMessage.error('获取商机列表失败')
  } finally {
    loading.value = false
  }
}

// 搜索
const handleSearch = () => {
  pagination.current = 1
  getOpportunityList()
}

// 重置搜索
const handleReset = () => {
  Object.keys(searchForm).forEach(key => {
    searchForm[key] = ''
  })
  pagination.current = 1
  getOpportunityList()
}

// 分页大小变化
const handleSizeChange = (size) => {
  pagination.size = size
  getOpportunityList()
}

// 分页当前页变化
const handleCurrentChange = (current) => {
  pagination.current = current
  getOpportunityList()
}

// 新增商机
const handleAddOpportunity = () => {
  dialogType.value = 'add'
  Object.keys(opportunityForm).forEach(key => {
    opportunityForm[key] = ''
  })
  opportunityForm.stage = '0'
  opportunityForm.amount = 0
  opportunityForm.status = '1'
  dialogVisible.value = true
}

// 编辑商机
const handleEditOpportunity = (opportunity) => {
  dialogType.value = 'edit'
  Object.assign(opportunityForm, opportunity)
  dialogVisible.value = true
}

// 提交表单
const handleSubmit = async () => {
  if (!opportunityFormRef.value) return
  
  try {
    await opportunityFormRef.value.validate()
    
    if (dialogType.value === 'add') {
      await businessOpportunityApi.createBusinessOpportunity(opportunityForm)
    } else {
      await businessOpportunityApi.updateBusinessOpportunity(opportunityForm.id, opportunityForm)
    }
    
    dialogVisible.value = false
    ElMessage.success(dialogType.value === 'add' ? '新增商机成功' : '编辑商机成功')
    getOpportunityList()
  } catch (error) {
    console.error('提交失败:', error)
    ElMessage.error('提交失败')
  }
}

// 查看商机详情
const handleViewOpportunity = (opportunity) => {
  ElMessage.info('查看商机详情功能开发中')
}

// 状态变更
const handleStatusChange = (opportunity) => {
  // 模拟API请求
  // 实际项目中需要调用后端API
  setTimeout(() => {
    ElMessage.success('状态更新成功')
  }, 500)
}

// 更新商机阶段
const handleUpdateStage = (opportunity) => {
  updateStageForm.stage = opportunity.stage
  updateStageForm.remark = ''
  updateStageVisible.value = true
}

// 提交更新阶段
const handleUpdateStageSubmit = async () => {
  if (!updateStageFormRef.value) return
  
  try {
    await updateStageFormRef.value.validate()
    
    // 模拟API请求
    // 实际项目中需要调用后端API
    setTimeout(() => {
      updateStageVisible.value = false
      ElMessage.success('更新商机阶段成功')
      getOpportunityList()
    }, 500)
  } catch (error) {
    console.error('表单验证失败:', error)
  }
}

// 添加跟进记录
const handleAddFollow = (opportunity) => {
  ElMessage.info('添加跟进记录功能开发中')
}

// 关闭商机
const handleCloseOpportunity = (opportunity) => {
  closeOpportunityForm.closeReason = ''
  closeOpportunityForm.convertToContract = false
  closeOpportunityVisible.value = true
}

// 提交关闭商机
const handleCloseOpportunitySubmit = async () => {
  if (!closeOpportunityFormRef.value) return
  
  try {
    await closeOpportunityFormRef.value.validate()
    
    // 模拟API请求
    // 实际项目中需要调用后端API
    setTimeout(() => {
      closeOpportunityVisible.value = false
      ElMessage.success('关闭商机成功')
      getOpportunityList()
    }, 500)
  } catch (error) {
    console.error('表单验证失败:', error)
  }
}

// 组件挂载时
onMounted(() => {
  getOpportunityList()
})
</script>

<style scoped>
.opportunity-management {
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
