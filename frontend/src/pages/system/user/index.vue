<template>
  <div class="user-management">
    <el-card shadow="hover">
      <template #header>
        <div class="card-header">
          <span>用户管理</span>
          <el-button type="primary" @click="handleAddUser">新增用户</el-button>
        </div>
      </template>
      
      <!-- 搜索表单 -->
      <el-form :inline="true" :model="searchForm" class="search-form">
        <el-form-item label="用户账号">
          <el-input v-model="searchForm.username" placeholder="请输入用户账号" style="width: 180px" />
        </el-form-item>
        <el-form-item label="用户姓名">
          <el-input v-model="searchForm.name" placeholder="请输入用户姓名" style="width: 180px" />
        </el-form-item>
        <el-form-item label="部门">
          <el-select v-model="searchForm.deptId" placeholder="请选择部门" style="width: 180px">
            <el-option
              v-for="dept in deptOptions"
              :key="dept.id"
              :label="dept.name"
              :value="dept.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="searchForm.status" placeholder="请选择状态" style="width: 120px">
            <el-option label="启用" value="1" />
            <el-option label="禁用" value="0" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">查询</el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
      
      <!-- 用户列表 -->
      <el-table :data="userList" style="width: 100%" border>
        <el-table-column prop="id" label="用户ID" width="80" />
        <el-table-column prop="username" label="用户账号" width="180" />
        <el-table-column prop="name" label="用户姓名" width="180" />
        <el-table-column prop="deptName" label="所属部门" width="180" />
        <el-table-column prop="email" label="邮箱" width="200" />
        <el-table-column prop="phone" label="电话" width="150" />
        <el-table-column prop="status" label="状态" width="100">
          <template #default="scope">
            <el-tag :type="scope.row.status === 1 ? 'success' : 'danger'">
              {{ scope.row.status === 1 ? '启用' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="200" />
        <el-table-column label="操作" width="250" fixed="right">
          <template #default="scope">
            <el-button type="primary" size="small" @click="handleEditUser(scope.row)">
              编辑
            </el-button>
            <el-button type="warning" size="small" @click="handleResetPassword(scope.row)">
              重置密码
            </el-button>
            <el-button type="danger" size="small" @click="handleDeleteUser(scope.row.id)">
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>
      
      <!-- 分页 -->
      <div class="pagination">
        <el-pagination
          v-model:current-page="pagination.currentPage"
          v-model:page-size="pagination.pageSize"
          :page-sizes="[10, 20, 50, 100]"
          layout="total, sizes, prev, pager, next, jumper"
          :total="pagination.total"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </el-card>
    
    <!-- 用户表单对话框 -->
    <el-dialog
      v-model="dialogVisible"
      :title="dialogTitle"
      width="600px"
    >
      <el-form
        :model="userForm"
        :rules="userRules"
        ref="userFormRef"
        label-width="100px"
      >
        <el-form-item label="用户账号" prop="username">
          <el-input v-model="userForm.username" placeholder="请输入用户账号" />
        </el-form-item>
        <el-form-item label="用户姓名" prop="name">
          <el-input v-model="userForm.name" placeholder="请输入用户姓名" />
        </el-form-item>
        <el-form-item label="密码" v-if="!userForm.id">
          <el-input v-model="userForm.password" type="password" placeholder="请输入密码" />
        </el-form-item>
        <el-form-item label="部门" prop="deptId">
          <el-select v-model="userForm.deptId" placeholder="请选择部门">
            <el-option
              v-for="dept in deptOptions"
              :key="dept.id"
              :label="dept.name"
              :value="dept.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="userForm.email" placeholder="请输入邮箱" />
        </el-form-item>
        <el-form-item label="电话" prop="phone">
          <el-input v-model="userForm.phone" placeholder="请输入电话" />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-switch v-model="userForm.status" active-value="1" inactive-value="0" />
        </el-form-item>
        <el-form-item label="角色" prop="roleIds">
          <el-select
            v-model="userForm.roleIds"
            multiple
            placeholder="请选择角色"
          >
            <el-option
              v-for="role in roleOptions"
              :key="role.id"
              :label="role.roleName"
              :value="role.id"
            />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="handleSubmit">确定</el-button>
        </span>
      </template>
    </el-dialog>
    
    <!-- 重置密码对话框 -->
    <el-dialog
      v-model="resetPasswordVisible"
      title="重置密码"
      width="400px"
    >
      <el-form
        :model="resetPasswordForm"
        :rules="resetPasswordRules"
        ref="resetPasswordFormRef"
        label-width="100px"
      >
        <el-form-item label="新密码" prop="password">
          <el-input v-model="resetPasswordForm.password" type="password" placeholder="请输入新密码" />
        </el-form-item>
        <el-form-item label="确认密码" prop="confirmPassword">
          <el-input v-model="resetPasswordForm.confirmPassword" type="password" placeholder="请确认新密码" />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="resetPasswordVisible = false">取消</el-button>
          <el-button type="primary" @click="handleResetPasswordSubmit">确定</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import userApi from '@/api/system/user'

// 响应式数据
const userList = ref([])
const deptOptions = ref([])
const roleOptions = ref([])
const searchForm = reactive({
  username: '',
  name: '',
  deptId: '',
  status: ''
})
const pagination = reactive({
  currentPage: 1,
  pageSize: 10,
  total: 0
})
const dialogVisible = ref(false)
const resetPasswordVisible = ref(false)
const dialogTitle = ref('')
const userForm = reactive({
  id: '',
  username: '',
  name: '',
  password: '',
  deptId: '',
  email: '',
  phone: '',
  status: 1,
  roleIds: []
})
const resetPasswordForm = reactive({
  password: '',
  confirmPassword: ''
})
const userFormRef = ref(null)
const resetPasswordFormRef = ref(null)

// 表单验证规则
const userRules = {
  username: [
    { required: true, message: '请输入用户账号', trigger: 'blur' }
  ],
  name: [
    { required: true, message: '请输入用户姓名', trigger: 'blur' }
  ],
  deptId: [
    { required: true, message: '请选择部门', trigger: 'change' }
  ],
  email: [
    { type: 'email', message: '请输入正确的邮箱格式', trigger: 'blur' }
  ],
  phone: [
    { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号格式', trigger: 'blur' }
  ]
}

const resetPasswordRules = {
  password: [
    { required: true, message: '请输入新密码', trigger: 'blur' },
    { min: 6, message: '密码长度至少6位', trigger: 'blur' }
  ],
  confirmPassword: [
    { required: true, message: '请确认新密码', trigger: 'blur' },
    {
      validator: (rule, value, callback) => {
        if (value !== resetPasswordForm.password) {
          callback(new Error('两次输入密码不一致'))
        } else {
          callback()
        }
      },
      trigger: 'blur'
    }
  ]
}

// 生命周期
onMounted(() => {
  fetchUserList()
  fetchDeptTree()
  fetchRoleList()
})

// 方法
const fetchUserList = async () => {
  try {
    const params = {
      ...searchForm,
      page: pagination.currentPage,
      pageSize: pagination.pageSize
    }
    const response = await userApi.getUserList(params)
    userList.value = response.data.records
    pagination.total = response.data.total
  } catch (error) {
    console.error('获取用户列表失败:', error)
  }
}

const fetchDeptTree = async () => {
  try {
    const response = await userApi.getDeptTree()
    deptOptions.value = response.data
  } catch (error) {
    console.error('获取部门树失败:', error)
  }
}

const fetchRoleList = async () => {
  try {
    const response = await userApi.getRoleList()
    roleOptions.value = response.data
  } catch (error) {
    console.error('获取角色列表失败:', error)
  }
}

const handleSearch = () => {
  pagination.currentPage = 1
  fetchUserList()
}

const handleReset = () => {
  Object.keys(searchForm).forEach(key => {
    searchForm[key] = ''
  })
  fetchUserList()
}

const handleSizeChange = (size) => {
  pagination.pageSize = size
  fetchUserList()
}

const handleCurrentChange = (current) => {
  pagination.currentPage = current
  fetchUserList()
}

const handleAddUser = () => {
  dialogTitle.value = '新增用户'
  Object.keys(userForm).forEach(key => {
    userForm[key] = key === 'status' ? 1 : ''
  })
  userForm.roleIds = []
  dialogVisible.value = true
}

const handleEditUser = (row) => {
  dialogTitle.value = '编辑用户'
  Object.assign(userForm, row)
  userForm.roleIds = row.roleIds || []
  dialogVisible.value = true
}

const handleSubmit = async () => {
  if (!userFormRef.value) return
  
  try {
    await userFormRef.value.validate()
    
    if (userForm.id) {
      await userApi.updateUser(userForm.id, userForm)
    } else {
      await userApi.createUser(userForm)
    }
    
    dialogVisible.value = false
    fetchUserList()
    ElMessage.success(userForm.id ? '更新成功' : '创建成功')
  } catch (error) {
    console.error('提交失败:', error)
    ElMessage.error('提交失败')
  }
}

const handleResetPassword = (row) => {
  userForm.id = row.id
  resetPasswordForm.password = ''
  resetPasswordForm.confirmPassword = ''
  resetPasswordVisible.value = true
}

const handleResetPasswordSubmit = async () => {
  if (!resetPasswordFormRef.value) return
  
  try {
    await resetPasswordFormRef.value.validate()
    await userApi.resetPassword(userForm.id, { password: resetPasswordForm.password })
    resetPasswordVisible.value = false
    ElMessage.success('密码重置成功')
  } catch (error) {
    console.error('密码重置失败:', error)
    ElMessage.error('密码重置失败')
  }
}

const handleDeleteUser = (id) => {
  ElMessageBox.confirm('确定要删除该用户吗？', '警告', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await userApi.deleteUser(id)
      fetchUserList()
      ElMessage.success('删除成功')
    } catch (error) {
      console.error('删除失败:', error)
      ElMessage.error('删除失败')
    }
  }).catch(() => {
    // 取消删除
  })
}
</script>

<style scoped>
.user-management {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.search-form {
  margin-bottom: 20px;
}

.pagination {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}

.dialog-footer {
  text-align: right;
}
</style>