import { createRouter, createWebHistory } from 'vue-router'

import IndexView from '@/views/index/index.vue'
import ClazzView from '@/views/clazz/index.vue'
import CollegeView from '@/views/college/index.vue'
import DeptView from '@/views/dept/index.vue'
import EmpView from '@/views/emp/index.vue'
import LogView from '@/views/log/index.vue'
import StuView from '@/views/stu/index.vue'
import EmpReportView from '@/views/report/emp/index.vue'
import StuReportView from '@/views/report/stu/index.vue'
import StudentStatisticsView from '@/views/statistics/student.vue'
import TeachingStatisticsView from '@/views/statistics/teaching.vue'
import ManagementStatisticsView from '@/views/statistics/management.vue'
import OperateLogView from '@/views/operateLog/index.vue'
import LayoutView from '@/views/layout/index.vue'
import LoginView from '@/views/login/index.vue'
import RegisterView from '@/views/register/index.vue'
import PasswordView from '@/views/password/index.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
     path: '/', 
     name: '',
     component: LayoutView,
     redirect: '/index', //重定向
     children: [
      {path: 'index', name: 'index', component: IndexView},
      {path: 'clazz', name: 'clazz', component: ClazzView},
      {path: 'college', name: 'college', component: CollegeView},
      {path: 'stu', name: 'stu', component: StuView},
      {path: 'dept', name: 'dept', component: DeptView},
      {path: 'emp', name: 'emp', component: EmpView},
      {path: 'log', name: 'log', component: LogView},
      {path: 'empReport', name: 'empReport', component: EmpReportView},
      {path: 'stuReport', name: 'stuReport', component: StuReportView},
      {path: 'statistics/student', name: 'studentStatistics', component: StudentStatisticsView},
      {path: 'statistics/teaching', name: 'teachingStatistics', component: TeachingStatisticsView},
      {path: 'statistics/management', name: 'managementStatistics', component: ManagementStatisticsView},
      {path: 'operateLog', name: 'operateLog', component: OperateLogView},
      {path: 'password', name: 'password', component: PasswordView},
     ]
    },
    {path: '/login', name: 'login', component: LoginView},
    {path: '/register', name: 'register', component: RegisterView}
  ]
})

export default router
