<template>
  <div class="admin-init">
    <h2>管理员账号初始化</h2>
    
    <el-card>
      <h3>问题诊断</h3>
      <p>如果您无法使用 admin/123456 登录，可能是因为：</p>
      <ul>
        <li>数据库中没有管理员账号</li>
        <li>密码加密不匹配</li>
        <li>数据库连接问题</li>
      </ul>
    </el-card>
    
    <el-card>
      <h3>解决方案</h3>
      
      <el-button type="primary" @click="initAdmin" :loading="loading">
        初始化管理员账号
      </el-button>
      
      <el-button type="warning" @click="resetPassword" :loading="resetLoading" style="margin-left: 10px;">
        重置密码为123456
      </el-button>
      
      <div v-if="initResult" class="result">
        <el-tag :type="initResult.success ? 'success' : 'danger'">
          {{ initResult.message }}
        </el-tag>
      </div>
    </el-card>
    
    <el-card>
      <h3>测试登录</h3>
      
      <el-form :model="loginForm" inline>
        <el-form-item label="用户名">
          <el-input v-model="loginForm.username" placeholder="admin" />
        </el-form-item>
        <el-form-item label="密码">
          <el-input v-model="loginForm.password" type="password" placeholder="123456" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="testLogin" :loading="loginLoading">
            测试登录
          </el-button>
        </el-form-item>
      </el-form>
      
      <div v-if="loginResult" class="result">
        <el-tag :type="loginResult.success ? 'success' : 'danger'">
          {{ loginResult.message }}
        </el-tag>
      </div>
    </el-card>
    
    <el-card>
      <h3>手动SQL解决方案</h3>
      <p>如果自动初始化失败，请手动执行以下SQL：</p>
      <el-input
        type="textarea"
        :rows="8"
        readonly
        :value="sqlScript"
      />
      <el-button type="primary" @click="copySql" style="margin-top: 10px;">
        复制SQL
      </el-button>
    </el-card>
  </div>
</template>

<script>
import { ref, reactive } from 'vue'
import api from '../utils/api'
import { ElMessage } from 'element-plus'

export default {
  name: 'AdminInit',
  setup() {
    const loading = ref(false)
    const loginLoading = ref(false)
    const resetLoading = ref(false)
    const initResult = ref(null)
    const loginResult = ref(null)
    
    const loginForm = reactive({
      username: 'admin',
      password: '123456'
    })
    
    const sqlScript = `-- 删除旧的管理员账号
DELETE FROM sys_user WHERE username = 'admin';

-- 插入新的管理员账号
INSERT INTO sys_user (username, password, real_name, email, phone, user_type, status, create_time, update_time, deleted) 
VALUES ('admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi', '系统管理员', 'admin@example.com', '13800138000', 1, 1, NOW(), NOW(), 0);

-- 验证账号
SELECT id, username, real_name, user_type, status FROM sys_user WHERE username = 'admin';`
    
    const initAdmin = async () => {
      loading.value = true
      try {
        const response = await api.post('/user/init-admin')
        if (response.code === 200) {
          initResult.value = {
            success: true,
            message: '管理员账号初始化成功！'
          }
          ElMessage.success('管理员账号初始化成功')
        } else {
          initResult.value = {
            success: false,
            message: response.message
          }
        }
      } catch (error) {
        initResult.value = {
          success: false,
          message: error.message || '初始化失败'
        }
        ElMessage.error('初始化失败')
      } finally {
        loading.value = false
      }
    }
    
    const resetPassword = async () => {
      resetLoading.value = true
      try {
        const response = await api.post('/user/reset-admin-password')
        if (response.code === 200) {
          initResult.value = {
            success: true,
            message: '管理员密码重置成功！密码已设置为123456'
          }
          ElMessage.success('密码重置成功')
        } else {
          initResult.value = {
            success: false,
            message: response.message
          }
        }
      } catch (error) {
        initResult.value = {
          success: false,
          message: error.message || '密码重置失败'
        }
        ElMessage.error('密码重置失败')
      } finally {
        resetLoading.value = false
      }
    }
    
    const testLogin = async () => {
      loginLoading.value = true
      try {
        const response = await api.post('/user/login', loginForm)
        if (response.code === 200) {
          loginResult.value = {
            success: true,
            message: '登录测试成功！'
          }
          ElMessage.success('登录测试成功')
        } else {
          loginResult.value = {
            success: false,
            message: response.message
          }
        }
      } catch (error) {
        loginResult.value = {
          success: false,
          message: error.message || '登录测试失败'
        }
        ElMessage.error('登录测试失败')
      } finally {
        loginLoading.value = false
      }
    }
    
    const copySql = () => {
      navigator.clipboard.writeText(sqlScript).then(() => {
        ElMessage.success('SQL已复制到剪贴板')
      }).catch(() => {
        ElMessage.error('复制失败')
      })
    }
    
    return {
      loading,
      loginLoading,
      initResult,
      loginResult,
      loginForm,
      sqlScript,
      initAdmin,
      resetPassword,
      testLogin,
      copySql
    }
  }
}
</script>

<style scoped>
.admin-init {
  padding: 20px;
}

.admin-init h2 {
  margin-bottom: 20px;
  color: #333;
}

.result {
  margin-top: 10px;
}

ul {
  margin: 10px 0;
  padding-left: 20px;
}

li {
  margin: 5px 0;
}
</style>
