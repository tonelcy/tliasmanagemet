<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { pageClazz, saveClazz, updateClazz, deleteClazz, listClazz } from '@/api/clazz'
import { listCollege } from '@/api/college'

const tableData = ref([])
const loading = ref(false)
const dialogVisible = ref(false)
const isEdit = ref(false)
const form = ref({
  id: null,
  name: '',
  room: '',
  collegeId: null
})
const colleges = ref([])
const selectedIds = ref([])
const pagination = ref({
  page: 1,
  pageSize: 10,
  total: 0
})

const getList = async () => {
  loading.value = true
  try {
    const res = await pageClazz({
      page: pagination.value.page,
      pageSize: pagination.value.pageSize
    })
    if (res.code === 1) {
      tableData.value = res.data.rows
      pagination.value.total = res.data.total
    }
  } catch (error) {
    console.error('查询失败', error)
    ElMessage.error('查询失败')
  } finally {
    loading.value = false
  }
}

const getColleges = async () => {
  try {
    const res = await listCollege()
    if (res.code === 1) {
      colleges.value = res.data
    }
  } catch (error) {
    console.error('查询学院失败', error)
  }
}

const handleAdd = () => {
  isEdit.value = false
  dialogVisible.value = true
  form.value = {
    id: null,
    name: '',
    room: '',
    collegeId: null
  }
}

const handleEdit = (row) => {
  isEdit.value = true
  dialogVisible.value = true
  form.value = { ...row }
}

const handleDelete = async (row) => {
  ElMessageBox.confirm('确定删除该班级吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      const res = await deleteClazz([row.id])
      if (res.code === 1) {
        ElMessage.success('删除成功')
        getList()
      } else {
        ElMessage.error(res.msg || '删除失败')
      }
    } catch (error) {
      ElMessage.error('删除失败')
    }
  })
}

const handleBatchDelete = async () => {
  if (selectedIds.value.length === 0) {
    ElMessage.warning('请选择要删除的记录')
    return
  }
  ElMessageBox.confirm('确定删除选中的班级吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      const res = await deleteClazz(selectedIds.value)
      if (res.code === 1) {
        ElMessage.success('删除成功')
        selectedIds.value = []
        getList()
      } else {
        ElMessage.error(res.msg || '删除失败')
      }
    } catch (error) {
      ElMessage.error('删除失败')
    }
  })
}

const handleSubmit = async () => {
  try {
    if (isEdit.value) {
      const res = await updateClazz(form.value)
      if (res.code === 1) {
        ElMessage.success('修改成功')
        dialogVisible.value = false
        getList()
      } else {
        ElMessage.error(res.msg || '修改失败')
      }
    } else {
      const res = await saveClazz(form.value)
      if (res.code === 1) {
        ElMessage.success('添加成功')
        dialogVisible.value = false
        getList()
      } else {
        ElMessage.error(res.msg || '添加失败')
      }
    }
  } catch (error) {
    ElMessage.error(isEdit.value ? '修改失败' : '添加失败')
  }
}

const handleSelectionChange = (selection) => {
  selectedIds.value = selection.map(item => item.id)
}

const handleCurrentChange = (page) => {
  pagination.value.page = page
  getList()
}

const handleSizeChange = (size) => {
  pagination.value.pageSize = size
  pagination.value.page = 1
  getList()
}

onMounted(() => {
  getList()
  getColleges()
})
</script>

<template>
  <div class="clazz-container">
    <div class="toolbar">
      <el-button type="primary" @click="handleAdd">新增班级</el-button>
      <el-button type="danger" @click="handleBatchDelete" :disabled="selectedIds.length === 0">批量删除</el-button>
    </div>

    <el-table :data="tableData" v-loading="loading" @selection-change="handleSelectionChange" border>
      <el-table-column type="selection" width="50" />
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column prop="name" label="班级名称" />
      <el-table-column prop="room" label="教室" />
      <el-table-column prop="collegeName" label="所属学院" />
      <el-table-column prop="createTime" label="创建时间" />
      <el-table-column label="操作" width="200">
        <template #default="{ row }">
          <el-button type="primary" size="small" @click="handleEdit(row)">编辑</el-button>
          <el-button type="danger" size="small" @click="handleDelete(row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <div class="pagination">
      <el-pagination
        v-model:current-page="pagination.page"
        v-model:page-size="pagination.pageSize"
        :page-sizes="[10, 20, 50, 100]"
        :total="pagination.total"
        layout="total, sizes, prev, pager, next, jumper"
        @current-change="handleCurrentChange"
        @size-change="handleSizeChange"
      />
    </div>

    <el-dialog v-model="dialogVisible" :title="isEdit ? '编辑班级' : '新增班级'" width="500px">
      <el-form :model="form" label-width="80px">
        <el-form-item label="班级名称">
          <el-input v-model="form.name" placeholder="请输入班级名称" />
        </el-form-item>
        <el-form-item label="教室">
          <el-input v-model="form.room" placeholder="请输入教室" />
        </el-form-item>
        <el-form-item label="所属学院">
          <el-select v-model="form.collegeId" placeholder="请选择学院" style="width: 100%">
            <el-option
              v-for="college in colleges"
              :key="college.id"
              :label="college.name"
              :value="college.id"
            />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<style scoped>
.clazz-container {
  padding: 20px;
}

.toolbar {
  margin-bottom: 20px;
}

.pagination {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}
</style>
