<template>
  <div class="operateLog-container">
    <el-card class="search-card">
      <el-form :inline="true" :model="queryParams" class="search-form">
        <el-form-item label="操作人">
          <el-input v-model="queryParams.operateEmpId" placeholder="请输入操作人ID" clearable />
        </el-form-item>
        <el-form-item label="操作时间">
          <el-date-picker
            v-model="dateRange"
            type="datetimerange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            value-format="YYYY-MM-DD HH:mm:ss"
          />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleQuery">搜索</el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-card class="table-card">
      <el-table :data="tableData" border style="width: 100%">
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="operateEmpId" label="操作人ID" width="100" />
        <el-table-column prop="operateTime" label="操作时间" width="180" />
        <el-table-column prop="className" label="操作类" show-overflow-tooltip />
        <el-table-column prop="methodName" label="操作方法" width="150" />
        <el-table-column prop="costTime" label="耗时(ms)" width="100" />
        <el-table-column label="操作" width="120">
          <template #default="scope">
            <el-button type="primary" size="small" @click="handleDetail(scope.row)">
              详情
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <el-pagination
        v-model:current-page="queryParams.page"
        v-model:page-size="queryParams.pageSize"
        :page-sizes="[5, 10, 20, 50]"
        :total="total"
        layout="total, sizes, prev, pager, next, jumper"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      />
    </el-card>

    <el-dialog v-model="detailVisible" title="操作日志详情" width="70%">
      <el-descriptions v-if="currentRow" :column="1" border>
        <el-descriptions-item label="ID">{{ currentRow.id }}</el-descriptions-item>
        <el-descriptions-item label="操作人ID">{{ currentRow.operateEmpId }}</el-descriptions-item>
        <el-descriptions-item label="操作时间">{{ currentRow.operateTime }}</el-descriptions-item>
        <el-descriptions-item label="操作类">{{ currentRow.className }}</el-descriptions-item>
        <el-descriptions-item label="操作方法">{{ currentRow.methodName }}</el-descriptions-item>
        <el-descriptions-item label="请求参数">
          <el-input
            :model-value="currentRow.methodParams"
            type="textarea"
            :rows="3"
            readonly
          />
        </el-descriptions-item>
        <el-descriptions-item label="返回值">
          <el-input
            :model-value="currentRow.returnValue"
            type="textarea"
            :rows="5"
            readonly
          />
        </el-descriptions-item>
        <el-descriptions-item label="执行耗时">{{ currentRow.costTime }} ms</el-descriptions-item>
      </el-descriptions>
      <template #footer>
        <el-button @click="detailVisible = false">关闭</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { pageOperateLog } from '@/api/operateLog'
import { ElMessage } from 'element-plus'

const tableData = ref([])
const total = ref(0)
const detailVisible = ref(false)
const currentRow = ref(null)
const dateRange = ref([])

const queryParams = reactive({
  page: 1,
  pageSize: 10,
  operateEmpId: null,
  begin: null,
  end: null
})

const getList = async () => {
  try {
    const params = {
      page: queryParams.page,
      pageSize: queryParams.pageSize
    }
    if (queryParams.operateEmpId) {
      params.operateEmpId = queryParams.operateEmpId
    }
    if (queryParams.begin) {
      params.begin = queryParams.begin
    }
    if (queryParams.end) {
      params.end = queryParams.end
    }
    const res = await pageOperateLog(params)
    if (res.code === 1) {
      tableData.value = res.data.rows
      total.value = res.data.total
    } else {
      ElMessage.error(res.msg || '查询失败')
    }
  } catch (error) {
    ElMessage.error('查询失败')
  }
}

const handleQuery = () => {
  if (dateRange.value && dateRange.value.length === 2) {
    queryParams.begin = dateRange.value[0]
    queryParams.end = dateRange.value[1]
  } else {
    queryParams.begin = null
    queryParams.end = null
  }
  queryParams.page = 1
  getList()
}

const handleReset = () => {
  queryParams.page = 1
  queryParams.operateEmpId = null
  dateRange.value = []
  getList()
}

const handleSizeChange = (val) => {
  queryParams.pageSize = val
  queryParams.page = 1
  getList()
}

const handleCurrentChange = (val) => {
  queryParams.page = val
  getList()
}

const handleDetail = (row) => {
  currentRow.value = row
  detailVisible.value = true
}

onMounted(() => {
  getList()
})
</script>

<style scoped>
.operateLog-container {
  padding: 20px;
}

.search-card {
  margin-bottom: 20px;
}

.search-form {
  margin-bottom: 0;
}

.table-card {
  margin-bottom: 20px;
}

.el-pagination {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}
</style>
