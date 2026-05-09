<script setup>
import { ref, onMounted } from 'vue'
import * as echarts from 'echarts'

const courseChartRef = ref(null)
const teacherChartRef = ref(null)

let courseChart = null
let teacherChart = null

const mockCourseData = [
  { name: '高等数学', count: 120 },
  { name: '大学英语', count: 100 },
  { name: '计算机基础', count: 85 },
  { name: '数据结构', count: 90 },
  { name: '数据库原理', count: 75 },
  { name: 'Java程序设计', count: 95 }
]

const mockTeacherData = [
  { name: '计算机学院', count: 25 },
  { name: '数学学院', count: 18 },
  { name: '外语学院', count: 15 },
  { name: '经济学院', count: 20 },
  { name: '管理学院', count: 22 }
]

const renderCourseChart = () => {
  if (!courseChartRef.value) return
  
  courseChart = echarts.init(courseChartRef.value)
  const option = {
    title: {
      text: '课程开设情况',
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
      data: mockCourseData.map(item => item.name),
      axisLabel: {
        interval: 0,
        rotate: 45
      }
    },
    yAxis: {
      type: 'value'
    },
    series: [
      {
        name: '课程数',
        type: 'bar',
        data: mockCourseData.map(item => item.count),
        itemStyle: {
          color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
            { offset: 0, color: '#409EFF' },
            { offset: 1, color: '#36D1DC' }
          ])
        }
      }
    ]
  }
  courseChart.setOption(option)
}

const renderTeacherChart = () => {
  if (!teacherChartRef.value) return
  
  teacherChart = echarts.init(teacherChartRef.value)
  const option = {
    title: {
      text: '教师资源分布',
      left: 'center'
    },
    tooltip: {
      trigger: 'item'
    },
    series: [
      {
        name: '教师人数',
        type: 'pie',
        radius: '50%',
        data: mockTeacherData,
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
  teacherChart.setOption(option)
}

onMounted(() => {
  renderCourseChart()
  renderTeacherChart()
})
</script>

<template>
  <div class="statistics-container">
    <el-row :gutter="20">
      <el-col :span="12">
        <el-card class="chart-card">
          <div ref="courseChartRef" class="chart-container"></div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card class="chart-card">
          <div ref="teacherChartRef" class="chart-container"></div>
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
