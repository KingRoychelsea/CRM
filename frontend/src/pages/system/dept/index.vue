<template>
  <div class="dept-management">
    <el-card shadow="hover">
      <template #header>
        <div class="card-header">
          <span>部门管理</span>
          <div>
            <el-button type="primary" @click="handleAddDept">
              <el-icon><Plus /></el-icon>
              <span>新增部门</span>
            </el-button>
            <el-button @click="handleRefresh">
              <el-icon><Refresh /></el-icon>
              <span>刷新</span>
            </el-button>
          </div>
        </div>
      </template>
      
      <!-- 部门树 -->
      <el-tree
        v-loading="loading"
        :data="deptTree"
        node-key="id"
        default-expand-all
        :expand-on-click-node="false"
        @node-click="handleNodeClick"
      >
        <template #default="{ node, data }">
          <span class="dept-node">
            <span>{{ data.deptName }}</span>
            <span class="dept-actions">
              <el-button type="primary" size="small" @click.stop="handleAddSubDept(data)">
                <el-icon><Plus /></el-icon>
              </el-button>
              <el-button type="success" size="small" @click.stop="handleEditDept(data)">
                <el-icon><Edit /></el-icon>
              </el-button>
              <el-button type="danger" size="small" @click.stop="handleDeleteDept(data.id)">
                <el-icon><Delete /></el-icon>
              </el-button>
            </span>
          </span>
        </template>
      </el-tree>
    </el-card>
    
    <!-- 部门表单对话框 -->
    <el-dialog
      v-model="dialogVisible"
      :title="dialogTitle"
      width="500px"
    >
      <el-form
        :model="deptForm"
        :rules="deptRules"
        ref="deptFormRef"
        label-width="100px"
      >
        <el-form-item label="上级部门" prop="parentId">
          <el-select v-model="deptForm.parentId" placeholder="请选择上级部门">
            <el-option label="无" value="0" />
            <el-option
              v-for="dept in deptOptions"
              :key="dept.id"
              :label="dept.label"
              :value="dept.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="部门名称" prop="deptName">
          <el-input v-model="deptForm.deptName" placeholder="请输入部门名称" />
        </el-form-item>
        <el-form-item label="部门编码" prop="deptCode">
          <el-input v-model="deptForm.deptCode" placeholder="请输入部门编码" />
        </el-form-item>
        <el-form-item label="负责人" prop="leader">
          <el-input v-model="deptForm.leader" placeholder="请输入负责人" />
        </el-form-item>
        <el-form-item label="联系电话" prop="phone">
          <el-input v-model="deptForm.phone" placeholder="请输入联系电话" />
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="deptForm.email" placeholder="请输入邮箱" />
        </el-form-item>
        <el-form-item label="排序" prop="orderNum">
          <el-input-number v-model="deptForm.orderNum" :min="0" :max="999" />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-switch v-model="deptForm.status" active-value="1" inactive-value="0" />
        </el-form-item>
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
import { ref, reactive, onMounted, computed } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus, Edit, Delete, Refresh } from '@element-plus/icons-vue'
import deptApi from '@/api/system/dept'

// 响应式数据
const loading = ref(false)
const deptTree = ref([])
const dialogVisible = ref(false)
const dialogTitle = ref('')
const deptFormRef = ref(null)

// 部门表单
const deptForm = reactive({
  id: '',
  parentId: '0',
  deptName: '',
  deptCode: '',
  leader: '',
  phone: '',
  email: '',
  orderNum: 0,
  status: '1'
})

// 表单验证规则
const deptRules = {
  deptName: [
    { required: true, message: '请输入部门名称', trigger: 'blur' }
  ],
  deptCode: [
    { required: true, message: '请输入部门编码', trigger: 'blur' }
  ],
  orderNum: [
    { required: true, message: '请输入排序', trigger: 'blur' }
  ]
}

// 部门选项（用于选择上级部门）
const deptOptions = computed(() => {
  const options = []
  const flattenDept = (deptList, prefix = '') => {
    deptList.forEach(dept => {
      options.push({
        id: dept.id,
        label: prefix + dept.deptName
      })
      if (dept.children && dept.children.length > 0) {
        flattenDept(dept.children, prefix + dept.deptName + ' / ')
      }
    })
  }
  flattenDept(deptTree.value)
  return options
})

// 获取部门树
const getDeptTree = async () => {
  loading.value = true
  try {
    const response = await deptApi.getDeptTree()
    deptTree.value = response.data
  } catch (error) {
    console.error('获取部门树失败:', error)
    ElMessage.error('获取部门树失败')
  } finally {
    loading.value = false
  }
}

// 刷新部门树
const handleRefresh = () => {
  getDeptTree()
}

// 节点点击事件
const handleNodeClick = (data, node, component) => {
  console.log('节点点击:', data)
}

// 新增部门
const handleAddDept = () => {
  dialogTitle.value = '新增部门'
  Object.keys(deptForm).forEach(key => {
    deptForm[key] = key === 'parentId' ? '0' : key === 'orderNum' ? 0 : key === 'status' ? '1' : ''
  })
  dialogVisible.value = true
}

// 新增子部门
const handleAddSubDept = (data) => {
  dialogTitle.value = '新增子部门'
  Object.keys(deptForm).forEach(key => {
    deptForm[key] = key === 'parentId' ? data.id : key === 'orderNum' ? 0 : key === 'status' ? '1' : ''
  })
  dialogVisible.value = true
}

// 编辑部门
const handleEditDept = async (data) => {
  dialogTitle.value = '编辑部门'
  try {
    const response = await deptApi.getDeptInfo(data.id)
    Object.assign(deptForm, response.data)
    dialogVisible.value = true
  } catch (error) {
    console.error('获取部门详情失败:', error)
    ElMessage.error('获取部门详情失败')
  }
}

// 提交表单
const handleSubmit = async () => {
  if (!deptFormRef.value) return
  
  try {
    await deptFormRef.value.validate()
    
    if (deptForm.id) {
      await deptApi.updateDept(deptForm.id, deptForm)
    } else {
      await deptApi.createDept(deptForm)
    }
    
    dialogVisible.value = false
    getDeptTree()
    ElMessage.success(deptForm.id ? '更新成功' : '创建成功')
  } catch (error) {
    console.error('提交失败:', error)
    ElMessage.error('提交失败')
  }
}

// 删除部门
const handleDeleteDept = (id) => {
  ElMessageBox.confirm('确定要删除该部门吗？', '警告', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await deptApi.deleteDept(id)
      getDeptTree()
      ElMessage.success('删除成功')
    } catch (error) {
      console.error('删除失败:', error)
      ElMessage.error('删除失败')
    }
  }).catch(() => {
    // 取消删除
  })
}

// 组件挂载时
onMounted(() => {
  getDeptTree()
})
</script>

<style scoped>
.dept-management {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.dept-node {
  display: flex;
  justify-content: space-between;
  align-items: center;
  width: 100%;
}

.dept-actions {
  display: flex;
  gap: 8px;
}

.dialog-footer {
  text-align: right;
}
</style>