<script setup>
import { ref, onMounted } from 'vue'
import * as echarts from 'echarts'

const trendChartRef = ref(null)
const operationChartRef = ref(null)

let trendChart = null
let operationChart = null

const mockTrendData = {
  months: ['1月', '2月', '3月', '4月', '5月', '6月'],
  students: [450, 480, 520, 550, 580, 620],
  teachers: [80, 85, 90, 92, 95, 100],
  courses: [20, 22, 25, 28, 30, 32]
}

const mockOperationData = [
  { name: '学生管理', count: 1200 },
  { name: '教师管理', count: 450 },
  { name: '课程管理', count: 380 },
  { name: '成绩管理', count: 650 },
  { name: '班级管理', count: 320 },
  { name: '学院管理', count: 180 }
]

const renderTrendChart = () => {
  if (!trendChartRef.value) return
  
  trendChart = echarts.init(trendChartRef.value)
  const option = {
    title: {
      text: '学校运营趋势分析',
      left: 'center'
    },
    tooltip: {
      trigger: 'axis'
    },
    legend: {
      data: ['学生', '教师', '课程'],
      bottom: 0
    },
    xAxis: {
      type: 'category',
      data: mockTrendData.months
    },
    yAxis: {
      type: 'value'
    },
    series: [
      {
        name: '学生',
        type: 'line',
        data: mockTrendData.students,
        smooth: true,
        itemStyle: { color: '#409EFF' }
      },
      {
        name: '教师',
        type: 'line',
        data: mockTrendData.teachers,
        smooth: true,
        itemStyle: { color: '#67C23A' }
      },
      {
        name: '课程',
        type: 'line',
        data: mockTrendData.courses,
        smooth: true,
        itemStyle: { color: '#E6A23C' }
      }
    ]
  }
  trendChart.setOption(option)
}

const renderOperationChart = () => {
  if (!operationChartRef.value) return
  
  operationChart = echarts.init(operationChartRef.value)
  const option = {
    title: {
      text: '管理模块使用情况',
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
      data: mockOperationData.map(item => item.name),
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
        name: '使用次数',
        type: 'bar',
        data: mockOperationData.map(item => item.count),
        itemStyle: {
          borderRadius: [10, 10, 0, 0],
          color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
            { offset: 0, color: '#83bff6' },
            { offset: 1, color: '#188df0' }
          ])
        }
      }
    ]
  }
  operationChart.setOption(option)
}

onMounted(() => {
  renderTrendChart()
  renderOperationChart()
})
</script>

<template>
  <div class="statistics-container">
    <el-row :gutter="20">
      <el-col :span="24">
        <el-card class="chart-card">
          <div ref="trendChartRef" class="chart-container"></div>
        </el-card>
      </el-col>
    </el-row>
    <el-row :gutter="20" style="margin-top: 20px;">
      <el-col :span="24">
        <el-card class="chart-card">
          <div ref="operationChartRef" class="chart-container"></div>
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
