<script setup>
import { ref, reactive } from 'vue'
import { registerApi } from '@/api/register'
import { ElMessage } from 'element-plus'
import { useRouter } from 'vue-router'

const router = useRouter()

// 注册表单数据
const registerForm = reactive({
    username: '',
    password: '',
    confirmPassword: ''
})

// 表单引用
const registerFormRef = ref()

// 表单校验规则
const registerRules = {
    username: [
        { required: true, message: '请输入用户名', trigger: 'blur' },
        { min: 2, max: 20, message: '用户名长度需在2-20个字符之间', trigger: 'blur' }
    ],
    password: [
        { required: true, message: '请输入密码', trigger: 'blur' },
        { min: 6, max: 20, message: '密码长度需在6-20个字符之间', trigger: 'blur' }
    ],
    confirmPassword: [
        { required: true, message: '请再次输入密码', trigger: 'blur' },
        {
            validator: (rule, value, callback) => {
                if (value !== registerForm.password) {
                    callback(new Error('两次输入的密码不一致'))
                } else {
                    callback()
                }
            },
            trigger: 'blur'
        }
    ]
}

// 注册按钮加载状态
const loading = ref(false)

// 注册
const registerUser = async () => {
    // 表单校验
    if (!registerFormRef.value) return
    await registerFormRef.value.validate(async (valid) => {
        if (valid) {
            loading.value = true
            try {
                const res = await registerApi({
                    username: registerForm.username,
                    password: registerForm.password
                })
                if (res.code === 1) {
                    ElMessage.success('注册成功，请登录')
                    // 清除表单
                    registerFormRef.value.resetFields()
                    // 跳转到登录页
                    router.push('/login')
                } else {
                    ElMessage.error(res.msg || '注册失败')
                }
            } catch (error) {
                console.error('注册失败：', error)
                ElMessage.error(error.response?.data?.msg || '注册失败，请稍后重试')
            } finally {
                loading.value = false
            }
        }
    })
}

// 取消
const cancel = () => {
    router.push('/login')
}
</script>

<template>
    <div id="container">
        <div class="register-form">
            <el-form 
                ref="registerFormRef" 
                :model="registerForm" 
                :rules="registerRules" 
                label-width="100px">
                <p class="title">Tlias智能学习辅助系统</p>
                <p class="subtitle">用户注册</p>
                <el-form-item label="用户名" prop="username">
                    <el-input 
                        v-model="registerForm.username" 
                        placeholder="请输入用户名（2-20个字符）"
                        autocomplete="off" />
                </el-form-item>
                <el-form-item label="密码" prop="password">
                    <el-input 
                        type="password" 
                        v-model="registerForm.password" 
                        placeholder="请输入密码（6-20个字符）"
                        show-password
                        autocomplete="new-password" />
                </el-form-item>
                <el-form-item label="确认密码" prop="confirmPassword">
                    <el-input 
                        type="password" 
                        v-model="registerForm.confirmPassword" 
                        placeholder="请再次输入密码"
                        show-password
                        autocomplete="new-password" />
                </el-form-item>
                <el-form-item>
                    <el-button 
                        class="button" 
                        type="primary" 
                        @click="registerUser"
                        :loading="loading">注册</el-button>
                    <el-button class="button" type="info" @click="cancel">返回登录</el-button>
                </el-form-item>
            </el-form>
        </div>
    </div>
</template>

<style scoped>
#container {
    padding: 10%;
    min-height: 100vh;
    background-image: url('../../assets/bg1.jpg');
    background-repeat: no-repeat;
    background-size: cover;
    background-position: center;
    display: flex;
    align-items: center;
    justify-content: center;
}

.register-form {
    width: 100%;
    max-width: 450px;
    padding: 40px 50px;
    margin: 0 auto;
    border: 1px solid #e0e0e0;
    border-radius: 12px;
    box-shadow: 0 4px 20px rgba(0, 0, 0, 0.3);
    background-color: rgba(255, 255, 255, 0.95);
}

.title {
    font-size: 28px;
    font-family: '楷体', 'Microsoft YaHei', sans-serif;
    text-align: center;
    margin-bottom: 8px;
    font-weight: bold;
    color: #303133;
}

.subtitle {
    font-size: 18px;
    text-align: center;
    margin-bottom: 30px;
    color: #909399;
}

.button {
    margin-top: 20px;
    width: 120px;
}
</style>
