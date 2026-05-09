<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { changePassword } from '@/api/emp'

const router = useRouter()

// 表单数据
const passwordForm = reactive({
  oldPassword: '',
  newPassword: '',
  confirmPassword: ''
})

// 表单引用
const passwordFormRef = ref()

// 加载状态
const loading = ref(false)

// 密码可见性
const showOldPassword = ref(false)
const showNewPassword = ref(false)
const showConfirmPassword = ref(false)

// 密码复杂度验证正则
const passwordRegex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,20}$/

// 表单校验规则
const passwordRules = {
  oldPassword: [
    { required: true, message: '请输入当前密码', trigger: 'blur' }
  ],
  newPassword: [
    { required: true, message: '请输入新密码', trigger: 'blur' },
    { min: 8, max: 20, message: '密码长度必须在8-20位之间', trigger: 'blur' },
    {
      validator: (rule, value, callback) => {
        if (value && !passwordRegex.test(value)) {
          callback(new Error('密码必须包含大小写字母、数字和特殊符号'))
        } else {
          callback()
        }
      },
      trigger: 'blur'
    }
  ],
  confirmPassword: [
    { required: true, message: '请再次输入新密码', trigger: 'blur' },
    {
      validator: (rule, value, callback) => {
        if (value && value !== passwordForm.newPassword) {
          callback(new Error('两次输入的密码不一致'))
        } else {
          callback()
        }
      },
      trigger: 'blur'
    }
  ]
}

// 提交修改密码
const handleSubmit = async () => {
  if (!passwordFormRef.value) return
  await passwordFormRef.value.validate(async (valid) => {
    if (valid) {
      loading.value = true
      try {
        await changePassword(passwordForm)
        ElMessage.success('密码修改成功，请重新登录')
        // 清除登录信息
        localStorage.removeItem('loginUser')
        // 跳转到登录页
        router.push('/login')
      } catch (error) {
        console.error('修改密码失败', error)
        ElMessage.error(error.response?.data?.msg || '修改密码失败')
      } finally {
        loading.value = false
      }
    }
  })
}

// 取消
const handleCancel = () => {
  router.push('/index')
}
</script>

<template>
  <div class="password-container">
    <el-card class="password-card">
      <template #header>
        <div class="card-header">
          <h2>修改密码</h2>
        </div>
      </template>
      
      <el-form 
        ref="passwordFormRef" 
        :model="passwordForm" 
        :rules="passwordRules" 
        label-width="100px"
        class="password-form"
      >
        <el-form-item label="当前密码" prop="oldPassword">
          <el-input 
            :type="showOldPassword ? 'text' : 'password'" 
            v-model="passwordForm.oldPassword" 
            placeholder="请输入当前密码"
            autocomplete="current-password"
          >
            <template #suffix>
              <el-icon class="cursor-pointer" @click="showOldPassword = !showOldPassword">
                <component :is="showOldPassword ? 'View' : 'Hide'" />
              </el-icon>
            </template>
          </el-input>
        </el-form-item>
        
        <el-form-item label="新密码" prop="newPassword">
          <el-input 
            :type="showNewPassword ? 'text' : 'password'" 
            v-model="passwordForm.newPassword" 
            placeholder="请输入新密码（8-20位，需包含大小写字母、数字和特殊符号）"
            autocomplete="new-password"
          >
            <template #suffix>
              <el-icon class="cursor-pointer" @click="showNewPassword = !showNewPassword">
                <component :is="showNewPassword ? 'View' : 'Hide'" />
              </el-icon>
            </template>
          </el-input>
          <div class="password-hint">
            <span v-if="passwordForm.newPassword">
              <span :class="passwordForm.newPassword.length >= 8 ? 'valid' : 'invalid'">
                ✔ 长度8-20位
              </span>
              <span :class="/(?=.*[a-z])/.test(passwordForm.newPassword) ? 'valid' : 'invalid'">
                ✔ 包含小写字母
              </span>
              <span :class="/(?=.*[A-Z])/.test(passwordForm.newPassword) ? 'valid' : 'invalid'">
                ✔ 包含大写字母
              </span>
              <span :class="/(?=.*\d)/.test(passwordForm.newPassword) ? 'valid' : 'invalid'">
                ✔ 包含数字
              </span>
              <span :class="/(?=.*[@$!%*?&])/.test(passwordForm.newPassword) ? 'valid' : 'invalid'">
                ✔ 包含特殊符号
              </span>
            </span>
          </div>
        </el-form-item>
        
        <el-form-item label="确认密码" prop="confirmPassword">
          <el-input 
            :type="showConfirmPassword ? 'text' : 'password'" 
            v-model="passwordForm.confirmPassword" 
            placeholder="请再次输入新密码"
            autocomplete="new-password"
          >
            <template #suffix>
              <el-icon class="cursor-pointer" @click="showConfirmPassword = !showConfirmPassword">
                <component :is="showConfirmPassword ? 'View' : 'Hide'" />
              </el-icon>
            </template>
          </el-input>
        </el-form-item>
        
        <el-form-item>
          <el-button 
            type="primary" 
            @click="handleSubmit" 
            :loading="loading"
            class="submit-button"
          >
            提交修改
          </el-button>
          <el-button 
            @click="handleCancel"
            class="cancel-button"
          >
            取消
          </el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<style scoped>
.password-container {
  display: flex;
  justify-content: center;
  align-items: flex-start;
  padding-top: 60px;
  min-height: 100vh;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.password-card {
  width: 100%;
  max-width: 550px;
  box-shadow: 0 10px 40px rgba(0, 0, 0, 0.2);
}

.card-header {
  text-align: center;
}

.card-header h2 {
  margin: 0;
  color: #333;
  font-size: 28px;
  font-weight: bold;
}

.password-form {
  padding: 20px 0;
}

.password-hint {
  margin-top: 8px;
  font-size: 12px;
  display: flex;
  flex-wrap: wrap;
  gap: 12px;
}

.password-hint span {
  color: #999;
}

.password-hint .valid {
  color: #67c23a;
}

.password-hint .invalid {
  color: #f56c6c;
}

.submit-button {
  width: 140px;
  margin-right: 15px;
}

.cancel-button {
  width: 100px;
}

.cursor-pointer {
  cursor: pointer;
  font-size: 16px;
}
</style>
