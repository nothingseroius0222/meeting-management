<template>
  <div class="test-page">
    <h2>系统测试页面</h2>
    
    <el-card>
      <h3>后端连接测试</h3>
      <el-button type="primary" @click="testBackend">测试后端连接</el-button>
      <div v-if="backendStatus" class="status">
        <el-tag :type="backendStatus.success ? 'success' : 'danger'">
          {{ backendStatus.message }}
        </el-tag>
      </div>
    </el-card>
    
    <el-card>
      <h3>数据库连接测试</h3>
      <el-button type="primary" @click="testDatabase">测试数据库连接</el-button>
      <div v-if="dbStatus" class="status">
        <el-tag :type="dbStatus.success ? 'success' : 'danger'">
          {{ dbStatus.message }}
        </el-tag>
      </div>
    </el-card>
    
    <el-card>
      <h3>API接口测试</h3>
      <el-button type="primary" @click="testLogin">测试登录接口</el-button>
      <div v-if="loginStatus" class="status">
        <el-tag :type="loginStatus.success ? 'success' : 'danger'">
          {{ loginStatus.message }}
        </el-tag>
      </div>
    </el-card>
  </div>
</template>

<script>
import { ref } from 'vue'
import api from '../utils/api'
import { ElMessage } from 'element-plus'

export default {
  name: 'TestPage',
  setup() {
    const backendStatus = ref(null)
    const dbStatus = ref(null)
    const loginStatus = ref(null)
    
    const testBackend = async () => {
      try {
        const response = await fetch('http://localhost:8080/api/user/info', {
          method: 'GET',
          headers: {
            'Content-Type': 'application/json'
          }
        })
        
        if (response.ok) {
          backendStatus.value = {
            success: true,
            message: '后端服务连接正常'
          }
        } else {
          backendStatus.value = {
            success: false,
            message: `后端服务响应异常: ${response.status}`
          }
        }
      } catch (error) {
        backendStatus.value = {
          success: false,
          message: `后端服务连接失败: ${error.message}`
        }
      }
    }
    
    const testDatabase = async () => {
      try {
        const response = await api.get('/room/list')
        if (response.code === 200) {
          dbStatus.value = {
            success: true,
            message: '数据库连接正常'
          }
        } else {
          dbStatus.value = {
            success: false,
            message: `数据库查询失败: ${response.message}`
          }
        }
      } catch (error) {
        dbStatus.value = {
          success: false,
          message: `数据库连接失败: ${error.message}`
        }
      }
    }
    
    const testLogin = async () => {
      try {
        const response = await api.post('/user/login', {
          username: 'admin',
          password: '123456'
        })
        
        if (response.code === 200) {
          loginStatus.value = {
            success: true,
            message: '登录接口正常'
          }
        } else {
          loginStatus.value = {
            success: false,
            message: `登录失败: ${response.message}`
          }
        }
      } catch (error) {
        loginStatus.value = {
          success: false,
          message: `登录接口异常: ${error.message}`
        }
      }
    }
    
    return {
      backendStatus,
      dbStatus,
      loginStatus,
      testBackend,
      testDatabase,
      testLogin
    }
  }
}
</script>

<style scoped>
.test-page {
  padding: 20px;
}

.test-page h2 {
  margin-bottom: 20px;
  color: #333;
}

.status {
  margin-top: 10px;
}
</style>
