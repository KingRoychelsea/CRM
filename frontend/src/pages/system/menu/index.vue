<template>
  <div class="menu-management">
    <el-card shadow="hover">
      <template #header>
        <div class="card-header">
          <span>菜单管理</span>
          <div>
            <el-button type="primary" @click="handleAddMenu">
              <el-icon><Plus /></el-icon>
              <span>新增菜单</span>
            </el-button>
            <el-button @click="handleRefresh">
              <el-icon><Refresh /></el-icon>
              <span>刷新</span>
            </el-button>
          </div>
        </div>
      </template>
      
      <!-- 菜单树 -->
      <el-tree
        v-loading="loading"
        :data="menuTree"
        node-key="id"
        default-expand-all
        :expand-on-click-node="false"
        :render-content="renderContent"
        @node-click="handleNodeClick"
      >
        <template #default="{ node, data }">
          <span class="menu-node">
            <span>{{ node.label }}</span>
            <span class="menu-actions">
              <el-button type="primary" size="small" @click.stop="handleAddSubMenu(data)">
                <el-icon><Plus /></el-icon>
              </el-button>
              <el-button type="success" size="small" @click.stop="handleEditMenu(data)">
                <el-icon><Edit /></el-icon>
              </el-button>
              <el-button type="danger" size="small" @click.stop="handleDeleteMenu(data.id)">
                <el-icon><Delete /></el-icon>
              </el-button>
            </span>
          </span>
        </template>
      </el-tree>
    </el-card>
    
    <!-- 菜单表单对话框 -->
    <el-dialog
      v-model="dialogVisible"
      :title="dialogTitle"
      width="600px"
    >
      <el-form
        :model="menuForm"
        :rules="menuRules"
        ref="menuFormRef"
        label-width="100px"
      >
        <el-form-item label="上级菜单" prop="parentId">
          <el-select v-model="menuForm.parentId" placeholder="请选择上级菜单">
            <el-option label="无" value="0" />
            <el-option
              v-for="menu in menuOptions"
              :key="menu.id"
              :label="menu.label"
              :value="menu.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="菜单名称" prop="menuName">
          <el-input v-model="menuForm.menuName" placeholder="请输入菜单名称" />
        </el-form-item>
        <el-form-item label="路由路径" prop="path">
          <el-input v-model="menuForm.path" placeholder="请输入路由路径" />
        </el-form-item>
        <el-form-item label="组件路径" prop="component">
          <el-input v-model="menuForm.component" placeholder="请输入组件路径" />
        </el-form-item>
        <el-form-item label="菜单图标" prop="icon">
          <el-input v-model="menuForm.icon" placeholder="请输入菜单图标" />
        </el-form-item>
        <el-form-item label="排序" prop="orderNum">
          <el-input-number v-model="menuForm.orderNum" :min="0" :max="999" />
        </el-form-item>
        <el-form-item label="菜单类型" prop="menuType">
          <el-radio-group v-model="menuForm.menuType">
            <el-radio label="0">目录</el-radio>
            <el-radio label="1">菜单</el-radio>
            <el-radio label="2">按钮</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="权限标识" prop="perms">
          <el-input v-model="menuForm.perms" placeholder="请输入权限标识" />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-switch v-model="menuForm.status" active-value="1" inactive-value="0" />
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
import menuApi from '@/api/system/menu'

// 响应式数据
const loading = ref(false)
const menuTree = ref([])
const dialogVisible = ref(false)
const dialogTitle = ref('')
const menuFormRef = ref(null)

// 菜单表单
const menuForm = reactive({
  id: '',
  parentId: '0',
  menuName: '',
  path: '',
  component: '',
  icon: '',
  orderNum: 0,
  menuType: '0',
  perms: '',
  status: '1'
})

// 表单验证规则
const menuRules = {
  menuName: [
    { required: true, message: '请输入菜单名称', trigger: 'blur' }
  ],
  menuType: [
    { required: true, message: '请选择菜单类型', trigger: 'change' }
  ],
  orderNum: [
    { required: true, message: '请输入排序', trigger: 'blur' }
  ]
}

// 菜单选项（用于选择上级菜单）
const menuOptions = computed(() => {
  const options = []
  const flattenMenu = (menuList, prefix = '') => {
    menuList.forEach(menu => {
      options.push({
        id: menu.id,
        label: prefix + menu.menuName
      })
      if (menu.children && menu.children.length > 0) {
        flattenMenu(menu.children, prefix + menu.menuName + ' / ')
      }
    })
  }
  flattenMenu(menuTree.value)
  return options
})

// 渲染菜单节点
const renderContent = (h, { node, data, store }) => {
  return h('span', {
    class: 'menu-node'
  }, [
    h('span', node.label),
    h('span', {
      class: 'menu-actions'
    }, [
      h('el-button', {
        props: {
          type: 'primary',
          size: 'small'
        },
        on: {
          click: (event) => {
            event.stopPropagation()
            handleAddSubMenu(data)
          }
        }
      }, [
        h('el-icon', [h(Plus)])
      ]),
      h('el-button', {
        props: {
          type: 'success',
          size: 'small'
        },
        on: {
          click: (event) => {
            event.stopPropagation()
            handleEditMenu(data)
          }
        }
      }, [
        h('el-icon', [h(Edit)])
      ]),
      h('el-button', {
        props: {
          type: 'danger',
          size: 'small'
        },
        on: {
          click: (event) => {
            event.stopPropagation()
            handleDeleteMenu(data.id)
          }
        }
      }, [
        h('el-icon', [h(Delete)])
      ])
    ])
  ])
}

// 获取菜单树
const getMenuTree = async () => {
  loading.value = true
  try {
    const response = await menuApi.getMenuTree()
    menuTree.value = response.data
  } catch (error) {
    console.error('获取菜单树失败:', error)
    ElMessage.error('获取菜单树失败')
  } finally {
    loading.value = false
  }
}

// 刷新菜单树
const handleRefresh = () => {
  getMenuTree()
}

// 节点点击事件
const handleNodeClick = (data, node, component) => {
  console.log('节点点击:', data)
}

// 新增菜单
const handleAddMenu = () => {
  dialogTitle.value = '新增菜单'
  Object.keys(menuForm).forEach(key => {
    menuForm[key] = key === 'parentId' ? '0' : key === 'orderNum' ? 0 : key === 'menuType' ? '0' : key === 'status' ? '1' : ''
  })
  dialogVisible.value = true
}

// 新增子菜单
const handleAddSubMenu = (data) => {
  dialogTitle.value = '新增子菜单'
  Object.keys(menuForm).forEach(key => {
    menuForm[key] = key === 'parentId' ? data.id : key === 'orderNum' ? 0 : key === 'menuType' ? '1' : key === 'status' ? '1' : ''
  })
  dialogVisible.value = true
}

// 编辑菜单
const handleEditMenu = async (data) => {
  dialogTitle.value = '编辑菜单'
  try {
    const response = await menuApi.getMenuInfo(data.id)
    Object.assign(menuForm, response.data)
    dialogVisible.value = true
  } catch (error) {
    console.error('获取菜单详情失败:', error)
    ElMessage.error('获取菜单详情失败')
  }
}

// 提交表单
const handleSubmit = async () => {
  if (!menuFormRef.value) return
  
  try {
    await menuFormRef.value.validate()
    
    if (menuForm.id) {
      await menuApi.updateMenu(menuForm.id, menuForm)
    } else {
      await menuApi.createMenu(menuForm)
    }
    
    dialogVisible.value = false
    getMenuTree()
    ElMessage.success(menuForm.id ? '更新成功' : '创建成功')
  } catch (error) {
    console.error('提交失败:', error)
    ElMessage.error('提交失败')
  }
}

// 删除菜单
const handleDeleteMenu = (id) => {
  ElMessageBox.confirm('确定要删除该菜单吗？', '警告', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await menuApi.deleteMenu(id)
      getMenuTree()
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
  getMenuTree()
})
</script>

<style scoped>
.menu-management {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.menu-node {
  display: flex;
  justify-content: space-between;
  align-items: center;
  width: 100%;
}

.menu-actions {
  display: flex;
  gap: 8px;
}

.dialog-footer {
  text-align: right;
}
</style>