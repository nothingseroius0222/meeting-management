<template>
  <div class="user-management">
    <h2>用户管理</h2>
    
    <el-table :data="users" style="width: 100%">
      <el-table-column prop="username" label="用户名" />
      <el-table-column prop="realName" label="真实姓名" />
      <el-table-column prop="email" label="邮箱" />
      <el-table-column prop="phone" label="手机号" />
      <el-table-column prop="userType" label="用户类型">
        <template #default="scope">
          <el-tag :type="scope.row.userType === 1 ? 'danger' : 'primary'">
            {{ scope.row.userType === 1 ? '管理员' : '普通用户' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="status" label="状态">
        <template #default="scope">
          <el-tag :type="scope.row.status === 1 ? 'success' : 'danger'">
            {{ scope.row.status === 1 ? '启用' : '禁用' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="createTime" label="注册时间">
        <template #default="scope">
          {{ formatDateTime(scope.row.createTime) }}
        </template>
      </el-table-column>
      <el-table-column label="操作">
        <template #default="scope">
          <el-button
            :type="scope.row.status === 1 ? 'warning' : 'success'"
            size="small"
            @click="toggleUserStatus(scope.row)"
          >
            {{ scope.row.status === 1 ? '禁用' : '启用' }}
          </el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script>
import { ref, onMounted } from 'vue'
import api from '../utils/api'
import { ElMessage, ElMessageBox } from 'element-plus'

export default {
  name: 'UserManagement',
  setup() {
    const users = ref([])
    
    const formatDateTime = (dateTime) => {
      return new Date(dateTime).toLocaleString('zh-CN')
    }
    
    const loadUsers = async () => {
      try {
        console.log('正在加载用户列表...') // 调试日志
        const response = await api.get('/user/list')
        console.log('用户列表响应:', response) // 调试日志
        
        if (response.code === 200) {
          users.value = response.data
          ElMessage.success(`加载了 ${response.data.length} 个用户`)
        } else {
          ElMessage.error(response.message || '加载用户列表失败')
        }
      } catch (error) {
        console.error('加载用户列表错误:', error) // 调试日志
        ElMessage.error(error.message || '加载用户列表失败')
        
        // 如果API调用失败，使用模拟数据作为备选
        const mockUsers = [
          {
            id: 1,
            username: 'admin',
            realName: '系统管理员',
            email: 'admin@example.com',
            phone: '13800138000',
            userType: 1,
            status: 1,
            createTime: new Date().toISOString()
          }
        ]
        users.value = mockUsers
        ElMessage.warning('使用模拟数据')
      }
    }
    
    const toggleUserStatus = async (user) => {
      try {
        const action = user.status === 1 ? '禁用' : '启用'
        await ElMessageBox.confirm(`确定要${action}用户 ${user.realName} 吗？`, '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
        
        console.log('更新用户状态:', user) // 调试日志
        
        // 调用更新用户状态的API
        const newStatus = user.status === 1 ? 0 : 1
        const response = await api.put('/user/update-status', {
          id: user.id,
          status: newStatus
        })
        
        if (response.code === 200) {
          user.status = newStatus
          ElMessage.success(`${action}成功`)
        } else {
          ElMessage.error(response.message || `${action}失败`)
        }
      } catch (error) {
        if (error !== 'cancel') {
          console.error('更新用户状态错误:', error) // 调试日志
          ElMessage.error(error.message || '操作失败')
        }
      }
    }
    
    onMounted(() => {
      loadUsers()
    })
    
    return {
      users,
      formatDateTime,
      toggleUserStatus
    }
  }
}
</script>

<style scoped>
.user-management h2 {
  margin-bottom: 20px;
  color: #333;
}
</style>
