<template>
  <div class="dict-management">
    <el-card shadow="hover">
      <template #header>
        <div class="card-header">
          <span>数据字典</span>
          <el-button type="primary" @click="handleAddDict">
            <el-icon><Plus /></el-icon>
            <span>新增字典类型</span>
          </el-button>
        </div>
      </template>
      
      <!-- 字典类型列表 -->
      <el-table :data="dictList" style="width: 100%" border>
        <el-table-column prop="id" label="字典ID" width="80" />
        <el-table-column prop="dictName" label="字典名称" width="180" />
        <el-table-column prop="dictType" label="字典类型" width="180" />
        <el-table-column prop="status" label="状态" width="100">
          <template #default="scope">
            <el-tag :type="scope.row.status === '1' ? 'success' : 'danger'">
              {{ scope.row.status === '1' ? '启用' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="description" label="描述" />
        <el-table-column prop="createTime" label="创建时间" width="200" />
        <el-table-column label="操作" width="250" fixed="right">
          <template #default="scope">
            <el-button type="primary" size="small" @click="handleEditDict(scope.row)">
              <el-icon><Edit /></el-icon>
              <span>编辑</span>
            </el-button>
            <el-button type="success" size="small" @click="handleDictItems(scope.row)">
              <el-icon><List /></el-icon>
              <span>字典项</span>
            </el-button>
            <el-button type="danger" size="small" @click="handleDeleteDict(scope.row.id)">
              <el-icon><Delete /></el-icon>
              <span>删除</span>
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
    
    <!-- 字典类型对话框 -->
    <el-dialog
      v-model="dictDialogVisible"
      :title="dictDialogTitle"
      width="500px"
    >
      <el-form
        :model="dictForm"
        :rules="dictRules"
        ref="dictFormRef"
        label-width="100px"
      >
        <el-form-item label="字典名称" prop="dictName">
          <el-input v-model="dictForm.dictName" placeholder="请输入字典名称" />
        </el-form-item>
        <el-form-item label="字典类型" prop="dictType">
          <el-input v-model="dictForm.dictType" placeholder="请输入字典类型" />
        </el-form-item>
        <el-form-item label="描述" prop="description">
          <el-input v-model="dictForm.description" type="textarea" placeholder="请输入描述" />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-switch v-model="dictForm.status" active-value="1" inactive-value="0" />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dictDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="handleDictSubmit">确定</el-button>
        </span>
      </template>
    </el-dialog>
    
    <!-- 字典项对话框 -->
    <el-dialog
      v-model="dictItemDialogVisible"
      :title="currentDictName + ' - 字典项管理'"
      width="700px"
    >
      <div class="dict-item-header">
        <el-button type="primary" @click="handleAddDictItem">
          <el-icon><Plus /></el-icon>
          <span>新增字典项</span>
        </el-button>
      </div>
      
      <!-- 字典项列表 -->
      <el-table :data="dictItemList" style="width: 100%" border>
        <el-table-column prop="id" label="项ID" width="80" />
        <el-table-column prop="itemName" label="项名称" width="150" />
        <el-table-column prop="itemValue" label="项值" width="150" />
        <el-table-column prop="sort" label="排序" width="80" />
        <el-table-column prop="status" label="状态" width="100">
          <template #default="scope">
            <el-tag :type="scope.row.status === '1' ? 'success' : 'danger'">
              {{ scope.row.status === '1' ? '启用' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="description" label="描述" />
        <el-table-column label="操作" width="150" fixed="right">
          <template #default="scope">
            <el-button type="primary" size="small" @click="handleEditDictItem(scope.row)">
              <el-icon><Edit /></el-icon>
            </el-button>
            <el-button type="danger" size="small" @click="handleDeleteDictItem(scope.row.id)">
              <el-icon><Delete /></el-icon>
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-dialog>
    
    <!-- 字典项编辑对话框 -->
    <el-dialog
      v-model="dictItemEditDialogVisible"
      :title="dictItemEditDialogTitle"
      width="500px"
    >
      <el-form
        :model="dictItemForm"
        :rules="dictItemRules"
        ref="dictItemFormRef"
        label-width="100px"
      >
        <el-form-item label="项名称" prop="itemName">
          <el-input v-model="dictItemForm.itemName" placeholder="请输入项名称" />
        </el-form-item>
        <el-form-item label="项值" prop="itemValue">
          <el-input v-model="dictItemForm.itemValue" placeholder="请输入项值" />
        </el-form-item>
        <el-form-item label="排序" prop="sort">
          <el-input-number v-model="dictItemForm.sort" :min="0" :max="999" />
        </el-form-item>
        <el-form-item label="描述" prop="description">
          <el-input v-model="dictItemForm.description" type="textarea" placeholder="请输入描述" />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-switch v-model="dictItemForm.status" active-value="1" inactive-value="0" />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dictItemEditDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="handleDictItemSubmit">确定</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus, Edit, Delete, List } from '@element-plus/icons-vue'
import dictApi from '@/api/system/dict'

// 响应式数据
const dictList = ref([])
const dictDialogVisible = ref(false)
const dictDialogTitle = ref('')
const dictFormRef = ref(null)

// 字典类型表单
const dictForm = reactive({
  id: '',
  dictName: '',
  dictType: '',
  description: '',
  status: '1'
})

// 字典类型验证规则
const dictRules = {
  dictName: [
    { required: true, message: '请输入字典名称', trigger: 'blur' }
  ],
  dictType: [
    { required: true, message: '请输入字典类型', trigger: 'blur' }
  ]
}

// 字典项相关
const dictItemDialogVisible = ref(false)
const dictItemEditDialogVisible = ref(false)
const dictItemEditDialogTitle = ref('')
const dictItemFormRef = ref(null)
const dictItemList = ref([])
const currentDictId = ref('')
const currentDictName = ref('')

// 字典项表单
const dictItemForm = reactive({
  id: '',
  dictId: '',
  itemName: '',
  itemValue: '',
  sort: 0,
  description: '',
  status: '1'
})

// 字典项验证规则
const dictItemRules = {
  itemName: [
    { required: true, message: '请输入项名称', trigger: 'blur' }
  ],
  itemValue: [
    { required: true, message: '请输入项值', trigger: 'blur' }
  ],
  sort: [
    { required: true, message: '请输入排序', trigger: 'blur' }
  ]
}

// 获取字典类型列表
const getDictList = async () => {
  try {
    const response = await dictApi.getDictList()
    dictList.value = response.data
  } catch (error) {
    console.error('获取字典类型列表失败:', error)
    ElMessage.error('获取字典类型列表失败')
  }
}

// 新增字典类型
const handleAddDict = () => {
  dictDialogTitle.value = '新增字典类型'
  Object.keys(dictForm).forEach(key => {
    dictForm[key] = key === 'status' ? '1' : ''
  })
  dictDialogVisible.value = true
}

// 编辑字典类型
const handleEditDict = (row) => {
  dictDialogTitle.value = '编辑字典类型'
  Object.assign(dictForm, row)
  dictDialogVisible.value = true
}

// 提交字典类型
const handleDictSubmit = async () => {
  if (!dictFormRef.value) return
  
  try {
    await dictFormRef.value.validate()
    
    if (dictForm.id) {
      await dictApi.updateDict(dictForm.id, dictForm)
    } else {
      await dictApi.createDict(dictForm)
    }
    
    dictDialogVisible.value = false
    getDictList()
    ElMessage.success(dictForm.id ? '更新成功' : '创建成功')
  } catch (error) {
    console.error('提交失败:', error)
    ElMessage.error('提交失败')
  }
}

// 删除字典类型
const handleDeleteDict = (id) => {
  ElMessageBox.confirm('确定要删除该字典类型吗？', '警告', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await dictApi.deleteDict(id)
      getDictList()
      ElMessage.success('删除成功')
    } catch (error) {
      console.error('删除失败:', error)
      ElMessage.error('删除失败')
    }
  }).catch(() => {
    // 取消删除
  })
}

// 查看字典项
const handleDictItems = async (row) => {
  currentDictId.value = row.id
  currentDictName.value = row.dictName
  try {
    const response = await dictApi.getDictItemList(row.id)
    dictItemList.value = response.data
    dictItemDialogVisible.value = true
  } catch (error) {
    console.error('获取字典项列表失败:', error)
    ElMessage.error('获取字典项列表失败')
  }
}

// 新增字典项
const handleAddDictItem = () => {
  dictItemEditDialogTitle.value = '新增字典项'
  Object.keys(dictItemForm).forEach(key => {
    dictItemForm[key] = key === 'dictId' ? currentDictId.value : key === 'sort' ? 0 : key === 'status' ? '1' : ''
  })
  dictItemEditDialogVisible.value = true
}

// 编辑字典项
const handleEditDictItem = (row) => {
  dictItemEditDialogTitle.value = '编辑字典项'
  Object.assign(dictItemForm, row)
  dictItemEditDialogVisible.value = true
}

// 提交字典项
const handleDictItemSubmit = async () => {
  if (!dictItemFormRef.value) return
  
  try {
    await dictItemFormRef.value.validate()
    
    if (dictItemForm.id) {
      await dictApi.updateDictItem(dictItemForm.id, dictItemForm)
    } else {
      await dictApi.createDictItem(dictItemForm)
    }
    
    dictItemEditDialogVisible.value = false
    const response = await dictApi.getDictItemList(currentDictId.value)
    dictItemList.value = response.data
    ElMessage.success(dictItemForm.id ? '更新成功' : '创建成功')
  } catch (error) {
    console.error('提交失败:', error)
    ElMessage.error('提交失败')
  }
}

// 删除字典项
const handleDeleteDictItem = (id) => {
  ElMessageBox.confirm('确定要删除该字典项吗？', '警告', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await dictApi.deleteDictItem(id)
      const response = await dictApi.getDictItemList(currentDictId.value)
      dictItemList.value = response.data
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
  getDictList()
})
</script>

<style scoped>
.dict-management {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.dict-item-header {
  margin-bottom: 20px;
  text-align: right;
}

.dialog-footer {
  text-align: right;
}
</style>