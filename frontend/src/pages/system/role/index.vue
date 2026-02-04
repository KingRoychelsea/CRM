<template>
  <div class="role-management">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>角色管理</span>
          <el-button type="primary" @click="handleAddRole">
            <el-icon><Plus /></el-icon>
            <span>新增角色</span>
          </el-button>
        </div>
      </template>
      
      <!-- 搜索表单 -->
      <el-form :model="searchForm" class="search-form" label-width="80px">
        <el-row :gutter="20">
          <el-col :span="6">
            <el-form-item label="角色名称">
              <el-input v-model="searchForm.roleName" placeholder="请输入角色名称" clearable></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="角色代码">
              <el-input v-model="searchForm.roleKey" placeholder="请输入角色代码" clearable></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="状态">
              <el-select v-model="searchForm.status" placeholder="请选择状态" clearable>
                <el-option label="启用" value="1"></el-option>
                <el-option label="禁用" value="0"></el-option>
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
      
      <!-- 角色列表 -->
      <el-table
        v-loading="loading"
        :data="roleList"
        style="width: 100%"
        border
      >
        <el-table-column prop="id" label="角色ID" width="80"></el-table-column>
        <el-table-column prop="roleName" label="角色名称" width="150"></el-table-column>
        <el-table-column prop="roleKey" label="角色代码" width="150"></el-table-column>
        <el-table-column prop="description" label="角色描述"></el-table-column>
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
            <el-button type="primary" size="small" @click="handleEditRole(scope.row)">
              <el-icon><Edit /></el-icon>
              <span>编辑</span>
            </el-button>
            <el-button type="success" size="small" @click="handleAuthMenu(scope.row)">
              <el-icon><Setting /></el-icon>
              <span>菜单权限</span>
            </el-button>
            <el-button type="warning" size="small" @click="handleAuthUser(scope.row)">
              <el-icon><User /></el-icon>
              <span>用户授权</span>
            </el-button>
            <el-button type="danger" size="small" @click="handleDeleteRole(scope.row.id)">
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
    
    <!-- 新增/编辑角色对话框 -->
    <el-dialog
      v-model="dialogVisible"
      :title="dialogType === 'add' ? '新增角色' : '编辑角色'"
      width="500px"
    >
      <el-form
        :model="roleForm"
        :rules="roleRules"
        ref="roleFormRef"
        label-width="100px"
      >
        <el-form-item prop="roleName">
          <el-input v-model="roleForm.roleName" placeholder="请输入角色名称"></el-input>
        </el-form-item>
        <el-form-item prop="roleKey">
          <el-input v-model="roleForm.roleKey" placeholder="请输入角色代码"></el-input>
        </el-form-item>
        <el-form-item prop="description">
          <el-input v-model="roleForm.description" type="textarea" placeholder="请输入角色描述"></el-input>
        </el-form-item>
        <el-form-item prop="status">
          <el-select v-model="roleForm.status" placeholder="请选择状态">
            <el-option label="启用" value="1"></el-option>
            <el-option label="禁用" value="0"></el-option>
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
    
    <!-- 菜单权限对话框 -->
    <el-dialog
      v-model="authMenuVisible"
      title="菜单权限设置"
      width="600px"
    >
      <el-tree
        :data="menuTree"
        show-checkbox
        node-key="id"
        default-expand-all
        :expand-on-click-node="false"
        :default-checked-keys="checkedMenuIds"
        @check-change="handleMenuCheckChange"
      ></el-tree>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="authMenuVisible = false">取消</el-button>
          <el-button type="primary" @click="handleAuthMenuSubmit">确定</el-button>
        </span>
      </template>
    </el-dialog>
    
    <!-- 用户授权对话框 -->
    <el-dialog
      v-model="authUserVisible"
      title="用户授权设置"
      width="600px"
    >
      <el-transfer
        v-model="authUserIds"
        :data="userOptions"
        :titles="['可选用户', '已选用户']"
        :button-texts="['添加', '移除']"
        :format="{
          noMatch: '无匹配数据',
          noData: '无数据',
          total: '共 {total} 项',
          filteredTotal: '共 {filteredTotal} 项'
        }"
        filterable
        filter-method="filterMethod"
        :filter-placeholder="'请输入搜索内容'"
        @change="handleUserTransferChange"
      ></el-transfer>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="authUserVisible = false">取消</el-button>
          <el-button type="primary" @click="handleAuthUserSubmit">确定</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus, Search, Refresh, Edit, Setting, User, Delete } from '@element-plus/icons-vue'
import roleApi from '@/api/system/role'

// 搜索表单
const searchForm = reactive({
  roleName: '',
  roleKey: '',
  status: ''
})

// 分页信息
const pagination = reactive({
  current: 1,
  size: 10,
  total: 0
})

// 加载状态
const loading = ref(false)

// 角色列表
const roleList = ref([])

// 对话框状态
const dialogVisible = ref(false)
const authMenuVisible = ref(false)
const authUserVisible = ref(false)
const dialogType = ref('add')

// 表单引用
const roleFormRef = ref(null)

// 角色表单
const roleForm = reactive({
  id: '',
  roleName: '',
  roleKey: '',
  description: '',
  status: '1'
})

// 表单验证规则
const roleRules = {
  roleName: [
    { required: true, message: '请输入角色名称', trigger: 'blur' },
    { min: 2, max: 20, message: '角色名称长度在 2 到 20 个字符', trigger: 'blur' }
  ],
  roleKey: [
    { required: true, message: '请输入角色代码', trigger: 'blur' },
    { min: 2, max: 20, message: '角色代码长度在 2 到 20 个字符', trigger: 'blur' }
  ],
  status: [
    { required: true, message: '请选择状态', trigger: 'blur' }
  ]
}

// 菜单树
const menuTree = ref([
  {
    id: 1,
    label: '系统管理',
    children: [
      { id: 11, label: '用户管理' },
      { id: 12, label: '角色管理' },
      { id: 13, label: '菜单管理' },
      { id: 14, label: '部门管理' },
      { id: 15, label: '数据字典' },
      { id: 16, label: '系统日志' }
    ]
  },
  {
    id: 2,
    label: '客户管理',
    children: [
      { id: 21, label: '客户列表' },
      { id: 22, label: '联系人管理' },
      { id: 23, label: '跟进记录' }
    ]
  },
  {
    id: 3,
    label: '销售管理',
    children: [
      { id: 31, label: '商机管理' },
      { id: 32, label: '合同管理' },
      { id: 33, label: '订单管理' },
      { id: 34, label: '回款管理' }
    ]
  },
  {
    id: 4,
    label: '数据统计',
    children: [
      { id: 41, label: '客户统计' },
      { id: 42, label: '销售统计' },
      { id: 43, label: '商机统计' },
      { id: 44, label: '回款统计' }
    ]
  }
])

// 选中的菜单ID
const checkedMenuIds = ref([])

// 用户选项
const userOptions = ref([
  { key: 1, label: '管理员', disabled: false },
  { key: 2, label: '普通用户', disabled: false }
])

// 授权的用户ID
const authUserIds = ref([])

// 获取角色列表
const getRoleList = async () => {
  loading.value = true
  try {
    const params = {
      ...searchForm,
      page: pagination.current,
      pageSize: pagination.size
    }
    const response = await roleApi.getRoleList(params)
    roleList.value = response.data.records
    pagination.total = response.data.total
  } catch (error) {
    console.error('获取角色列表失败:', error)
    ElMessage.error('获取角色列表失败')
  } finally {
    loading.value = false
  }
}

// 搜索
const handleSearch = () => {
  pagination.current = 1
  getRoleList()
}

// 重置搜索
const handleReset = () => {
  Object.keys(searchForm).forEach(key => {
    searchForm[key] = ''
  })
  pagination.current = 1
  getRoleList()
}

// 分页大小变化
const handleSizeChange = (size) => {
  pagination.size = size
  getRoleList()
}

// 分页当前页变化
const handleCurrentChange = (current) => {
  pagination.current = current
  getRoleList()
}

// 新增角色
const handleAddRole = () => {
  dialogType.value = 'add'
  Object.keys(roleForm).forEach(key => {
    roleForm[key] = ''
  })
  roleForm.status = '1'
  dialogVisible.value = true
}

// 编辑角色
const handleEditRole = (role) => {
  dialogType.value = 'edit'
  Object.assign(roleForm, role)
  dialogVisible.value = true
}

// 提交表单
const handleSubmit = async () => {
  if (!roleFormRef.value) return
  
  try {
    await roleFormRef.value.validate()
    
    if (dialogType.value === 'add') {
      await roleApi.createRole(roleForm)
    } else {
      await roleApi.updateRole(roleForm.id, roleForm)
    }
    
    dialogVisible.value = false
    ElMessage.success(dialogType.value === 'add' ? '新增角色成功' : '编辑角色成功')
    getRoleList()
  } catch (error) {
    console.error('提交失败:', error)
    ElMessage.error('提交失败')
  }
}

// 菜单权限设置
const handleAuthMenu = async (role) => {
  try {
    // 获取菜单树
    const menuResponse = await roleApi.getMenuTree()
    menuTree.value = menuResponse.data
    
    // 获取角色详情，包含已选中的菜单ID
    const roleResponse = await roleApi.getRoleInfo(role.id)
    checkedMenuIds.value = roleResponse.data.menuIds || []
    
    // 保存当前操作的角色ID
    roleForm.id = role.id
    
    authMenuVisible.value = true
  } catch (error) {
    console.error('获取菜单权限失败:', error)
    ElMessage.error('获取菜单权限失败')
  }
}

// 菜单勾选变化
const handleMenuCheckChange = (data, checked, indeterminate) => {
  console.log('菜单勾选变化:', data, checked, indeterminate)
}

// 提交菜单权限
const handleAuthMenuSubmit = async () => {
  try {
    await roleApi.assignMenus(roleForm.id, { menuIds: checkedMenuIds.value })
    authMenuVisible.value = false
    ElMessage.success('菜单权限设置成功')
  } catch (error) {
    console.error('设置菜单权限失败:', error)
    ElMessage.error('设置菜单权限失败')
  }
}

// 用户授权设置
const handleAuthUser = (role) => {
  // 模拟获取已授权的用户ID
  authUserIds.value = [1]
  authUserVisible.value = true
}

// 用户转移变化
const handleUserTransferChange = (targetKeys, direction, moveKeys) => {
  console.log('用户转移变化:', targetKeys, direction, moveKeys)
}

// 提交用户授权
const handleAuthUserSubmit = () => {
  // 模拟API请求
  // 实际项目中需要调用后端API
  setTimeout(() => {
    authUserVisible.value = false
    ElMessage.success('用户授权设置成功')
  }, 500)
}

// 状态变更
const handleStatusChange = (role) => {
  // 模拟API请求
  // 实际项目中需要调用后端API
  setTimeout(() => {
    ElMessage.success('状态更新成功')
  }, 500)
}

// 删除角色
const handleDeleteRole = (id) => {
  ElMessageBox.confirm('确定要删除该角色吗？', '警告', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await roleApi.deleteRole(id)
      ElMessage.success('删除角色成功')
      getRoleList()
    } catch (error) {
      console.error('删除角色失败:', error)
      ElMessage.error('删除角色失败')
    }
  }).catch(() => {
    // 取消删除
  })
}

// 组件挂载时
onMounted(() => {
  getRoleList()
})
</script>

<style scoped>
.role-management {
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
