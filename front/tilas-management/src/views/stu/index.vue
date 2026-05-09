<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { pageStudent, saveStudent, updateStudent, deleteStudent } from '@/api/student'
import { listCollege } from '@/api/college'
import { listClazz } from '@/api/clazz'

const tableData = ref([])
const loading = ref(false)
const dialogVisible = ref(false)
const isEdit = ref(false)
const form = ref({
  id: null,
  name: '',
  no: '',
  gender: null,
  phone: '',
  clazzId: null,
  collegeId: null,
  degree: null,
  admissionDate: '',
  image: ''
})
const colleges = ref([])
const clazzs = ref([])
const selectedIds = ref([])
const pagination = ref({
  page: 1,
  pageSize: 10,
  total: 0
})

const getList = async () => {
  loading.value = true
  try {
    const res = await pageStudent({
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

const getClazzs = async () => {
  try {
    const res = await listClazz()
    if (res.code === 1) {
      clazzs.value = res.data
    }
  } catch (error) {
    console.error('查询班级失败', error)
  }
}

const handleAdd = () => {
  isEdit.value = false
  dialogVisible.value = true
  form.value = {
    id: null,
    name: '',
    no: '',
    gender: null,
    phone: '',
    clazzId: null,
    collegeId: null,
    degree: null,
    admissionDate: '',
    image: ''
  }
}

const handleEdit = (row) => {
  isEdit.value = true
  dialogVisible.value = true
  form.value = { ...row }
}

const handleDelete = async (row) => {
  ElMessageBox.confirm('确定删除该学生吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      const res = await deleteStudent([row.id])
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
  ElMessageBox.confirm('确定删除选中的学生吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      const res = await deleteStudent(selectedIds.value)
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
      const res = await updateStudent(form.value)
      if (res.code === 1) {
        ElMessage.success('修改成功')
        dialogVisible.value = false
        getList()
      } else {
        ElMessage.error(res.msg || '修改失败')
      }
    } else {
      const res = await saveStudent(form.value)
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
  getClazzs()
})
</script>

<template>
  <div class="student-container">
    <div class="toolbar">
      <el-button type="primary" @click="handleAdd">新增学生</el-button>
      <el-button type="danger" @click="handleBatchDelete" :disabled="selectedIds.length === 0">批量删除</el-button>
    </div>

    <el-table :data="tableData" v-loading="loading" @selection-change="handleSelectionChange" border>
      <el-table-column type="selection" width="50" />
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column prop="name" label="姓名" />
      <el-table-column prop="no" label="学号" />
      <el-table-column prop="gender" label="性别">
        <template #default="{ row }">
          {{ row.gender === 1 ? '男' : '女' }}
        </template>
      </el-table-column>
      <el-table-column prop="phone" label="手机号" />
      <el-table-column prop="clazzName" label="班级" />
      <el-table-column prop="collegeName" label="学院" />
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

    <el-dialog v-model="dialogVisible" :title="isEdit ? '编辑学生' : '新增学生'" width="600px">
      <el-form :model="form" label-width="100px">
        <el-form-item label="姓名">
          <el-input v-model="form.name" placeholder="请输入姓名" />
        </el-form-item>
        <el-form-item label="学号">
          <el-input v-model="form.no" placeholder="请输入学号" />
        </el-form-item>
        <el-form-item label="性别">
          <el-radio-group v-model="form.gender">
            <el-radio :value="1">男</el-radio>
            <el-radio :value="2">女</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="手机号">
          <el-input v-model="form.phone" placeholder="请输入手机号" />
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
        <el-form-item label="所属班级">
          <el-select v-model="form.clazzId" placeholder="请选择班级" style="width: 100%">
            <el-option
              v-for="clazz in clazzs"
              :key="clazz.id"
              :label="clazz.name"
              :value="clazz.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="学历">
          <el-select v-model="form.degree" placeholder="请选择学历" style="width: 100%">
            <el-option :value="1" label="专科" />
            <el-option :value="2" label="本科" />
            <el-option :value="3" label="硕士" />
            <el-option :value="4" label="博士" />
          </el-select>
        </el-form-item>
        <el-form-item label="入学日期">
          <el-date-picker v-model="form.admissionDate" type="date" placeholder="请选择入学日期" style="width: 100%" />
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
.student-container {
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
