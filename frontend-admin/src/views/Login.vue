<template>
  <div class="login-container">
    <div class="login-box">
      <h2 class="login-title">管理后台登录</h2>
      <el-form :model="loginForm" :rules="rules" ref="loginFormRef" class="login-form">
        <el-form-item prop="username">
          <el-input
            v-model="loginForm.username"
            placeholder="请输入管理员用户名"
            prefix-icon="User"
            size="large"
          />
        </el-form-item>
        <el-form-item prop="password">
          <el-input
            v-model="loginForm.password"
            type="password"
            placeholder="请输入密码"
            prefix-icon="Lock"
            size="large"
            @keyup.enter="handleLogin"
          />
        </el-form-item>
        <el-form-item>
          <el-button
            type="primary"
            size="large"
            class="login-button"
            :loading="loading"
            @click="handleLogin"
          >
            登录
          </el-button>
        </el-form-item>
        <el-form-item>
          <div class="login-footer">
            <el-link type="primary" @click="goToUserLogin">用户登录</el-link>
          </div>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script>
import { ref, reactive } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useUserStore } from '../stores/user'
import api from '../utils/api'
import { ElMessage } from 'element-plus'

export default {
  name: 'Login',
  setup() {
    const router = useRouter()
    const route = useRoute()
    const userStore = useUserStore()
    const loginFormRef = ref()
    const loading = ref(false)
    
    const loginForm = reactive({
      username: '',
      password: ''
    })
    
    const rules = {
      username: [
        { required: true, message: '请输入用户名', trigger: 'blur' }
      ],
      password: [
        { required: true, message: '请输入密码', trigger: 'blur' }
      ]
    }
    
    const handleLogin = async () => {
      if (!loginFormRef.value) return
      
      await loginFormRef.value.validate(async (valid) => {
        if (valid) {
          loading.value = true
          try {
            const response = await api.post('/user/login', loginForm)
            if (response.code === 200) {
              // 检查是否为管理员
              if (response.data.user.userType !== 1) {
                ElMessage.error('只有管理员可以登录管理后台')
                return
              }
              
              userStore.setToken(response.data.token)
              userStore.setUser(response.data.user)
              ElMessage.success('登录成功')
              // 获取登录页地址栏里的 redirect 参数
              let redirectPath = route.query.redirect || '/dashboard'
              // 安全过滤：防止 redirect 指向登录页或注册页，导致死循环
              const safePaths = ['/login', '/register']
              if (typeof redirectPath === 'string' && safePaths.includes(redirectPath)) {
                redirectPath = '/home'
              }
              // 跳转回去！replace 替换历史记录，避免后退回到登录页
              router.replace(redirectPath)
            } else {
              ElMessage.error(response.message)
            }
          } catch (error) {
            ElMessage.error('登录失败，请检查用户名和密码')
          } finally {
            loading.value = false
          }
        }
      })
    }
    
    const goToUserLogin = () => {
      window.open('http://localhost:3000', '_blank')
    }
    
    return {
      loginFormRef,
      loading,
      loginForm,
      rules,
      handleLogin,
      goToUserLogin
    }
  }
}
</script>

<style scoped>
.login-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.login-box {
  width: 400px;
  padding: 40px;
  background: white;
  border-radius: 10px;
  box-shadow: 0 15px 35px rgba(0, 0, 0, 0.1);
}

.login-title {
  text-align: center;
  margin-bottom: 30px;
  color: #333;
  font-size: 24px;
  font-weight: 600;
}

.login-form {
  width: 100%;
}

.login-button {
  width: 100%;
  height: 45px;
  font-size: 16px;
}

.login-footer {
  text-align: center;
  width: 100%;
}
</style>
