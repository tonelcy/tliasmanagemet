<script setup>
import { ref, onMounted } from 'vue'
import * as echarts from 'echarts'
import { getStudentGenderStats, getStudentDegreeStats, getStudentCollegeStats, getStudentClazzStats } from '@/api/student'

const genderChartRef = ref(null)
const degreeChartRef = ref(null)
const collegeChartRef = ref(null)
const clazzChartRef = ref(null)

let genderChart = null
let degreeChart = null
let collegeChart = null
let clazzChart = null

const loadGenderStats = async () => {
  try {
    const res = await getStudentGenderStats()
    if (res.code === 1 && res.data) {
      const data = res.data.map(item => ({
        name: item.name,
        value: item.value
      }))
      renderGenderChart(data)
    }
  } catch (error) {
    console.error('加载性别统计失败', error)
  }
}

const loadDegreeStats = async () => {
  try {
    const res = await getStudentDegreeStats()
    if (res.code === 1 && res.data) {
      const data = res.data.map(item => ({
        name: item.name,
        value: item.value
      }))
      renderDegreeChart(data)
    }
  } catch (error) {
    console.error('加载学历统计失败', error)
  }
}

const loadCollegeStats = async () => {
  try {
    const res = await getStudentCollegeStats()
    if (res.code === 1 && res.data) {
      const data = res.data.map(item => ({
        name: item.name,
        value: item.value
      }))
      renderCollegeChart(data)
    }
  } catch (error) {
    console.error('加载学院统计失败', error)
  }
}

const loadClazzStats = async () => {
  try {
    const res = await getStudentClazzStats()
    if (res.code === 1 && res.data) {
      const data = res.data.map(item => ({
        name: item.name,
        value: item.value
      }))
      renderClazzChart(data)
    }
  } catch (error) {
    console.error('加载班级统计失败', error)
  }
}

const renderGenderChart = (data) => {
  if (!genderChartRef.value) return
  
  genderChart = echarts.init(genderChartRef.value)
  const option = {
    title: {
      text: '学生性别分布',
      left: 'center'
    },
    tooltip: {
      trigger: 'item'
    },
    legend: {
      orient: 'vertical',
      left: 'left'
    },
    series: [
      {
        name: '性别分布',
        type: 'pie',
        radius: '50%',
        data: data,
        emphasis: {
          itemStyle: {
            shadowBlur: 10,
            shadowOffsetX: 0,
            shadowColor: 'rgba(0, 0, 0, 0.5)'
          }
        }
      }
    ]
  }
  genderChart.setOption(option)
}

const renderDegreeChart = (data) => {
  if (!degreeChartRef.value) return
  
  degreeChart = echarts.init(degreeChartRef.value)
  const option = {
    title: {
      text: '学生学历分布',
      left: 'center'
    },
    tooltip: {
      trigger: 'axis',
      axisPointer: {
        type: 'shadow'
      }
    },
    xAxis: {
      type: 'category',
      data: data.map(item => item.name)
    },
    yAxis: {
      type: 'value'
    },
    series: [
      {
        name: '人数',
        type: 'bar',
        data: data.map(item => item.value),
        itemStyle: {
          color: '#409EFF'
        }
      }
    ]
  }
  degreeChart.setOption(option)
}

const renderCollegeChart = (data) => {
  if (!collegeChartRef.value) return
  
  collegeChart = echarts.init(collegeChartRef.value)
  const option = {
    title: {
      text: '各学院学生人数',
      left: 'center'
    },
    tooltip: {
      trigger: 'item'
    },
    series: [
      {
        name: '学生人数',
        type: 'pie',
        radius: ['40%', '70%'],
        data: data,
        emphasis: {
          itemStyle: {
            shadowBlur: 10,
            shadowOffsetX: 0,
            shadowColor: 'rgba(0, 0, 0, 0.5)'
          }
        }
      }
    ]
  }
  collegeChart.setOption(option)
}

const renderClazzChart = (data) => {
  if (!clazzChartRef.value) return
  
  clazzChart = echarts.init(clazzChartRef.value)
  const option = {
    title: {
      text: '各班级学生人数',
      left: 'center'
    },
    tooltip: {
      trigger: 'axis',
      axisPointer: {
        type: 'shadow'
      }
    },
    xAxis: {
      type: 'category',
      data: data.map(item => item.name)
    },
    yAxis: {
      type: 'value'
    },
    series: [
      {
        name: '人数',
        type: 'line',
        data: data.map(item => item.value),
        smooth: true,
        itemStyle: {
          color: '#67C23A'
        },
        areaStyle: {
          color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
            { offset: 0, color: 'rgba(103, 194, 58, 0.3)' },
            { offset: 1, color: 'rgba(103, 194, 58, 0.05)' }
          ])
        }
      }
    ]
  }
  clazzChart.setOption(option)
}

onMounted(() => {
  loadGenderStats()
  loadDegreeStats()
  loadCollegeStats()
  loadClazzStats()
})
</script>

<template>
  <div class="statistics-container">
    <el-row :gutter="20">
      <el-col :span="12">
        <el-card class="chart-card">
          <div ref="genderChartRef" class="chart-container"></div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card class="chart-card">
          <div ref="degreeChartRef" class="chart-container"></div>
        </el-card>
      </el-col>
    </el-row>
    <el-row :gutter="20" style="margin-top: 20px;">
      <el-col :span="12">
        <el-card class="chart-card">
          <div ref="collegeChartRef" class="chart-container"></div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card class="chart-card">
          <div ref="clazzChartRef" class="chart-container"></div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<style scoped>
.statistics-container {
  padding: 20px;
}

.chart-card {
  margin-bottom: 20px;
}

.chart-container {
  width: 100%;
  height: 400px;
}
</style>
