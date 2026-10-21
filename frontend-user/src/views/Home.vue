<template>
  <div class="home-container">
    <el-container>
      <!-- 头部 -->
      <el-header class="header">
        <div class="header-left">
          <h1>会议预约系统</h1>
        </div>
        <div class="header-right">
          <span>欢迎，{{ userStore.user?.realName || userStore.user?.username }}</span>
          <NotificationCenter />
          <el-button type="primary" @click="handleLogout">退出登录</el-button>
        </div>
      </el-header>
      
      <!-- 主体内容 -->
      <el-main class="main">
        <div class="welcome-section">
          <h2>欢迎使用会议预约系统</h2>
          <p>您可以在这里预约会议室，查看预约记录</p>
        </div>
        
        <div class="action-cards">
          <el-row :gutter="20">
            <el-col :span="8">
              <el-card class="action-card" @click="$router.push('/rooms')">
                <div class="card-content">
                  <el-icon size="40" color="#409EFF"><OfficeBuilding /></el-icon>
                  <h3>会议室列表</h3>
                  <p>查看所有可用会议室</p>
                </div>
              </el-card>
            </el-col>
            <el-col :span="8">
              <el-card class="action-card" @click="$router.push('/create-reservation')">
                <div class="card-content">
                  <el-icon size="40" color="#67C23A"><Calendar /></el-icon>
                  <h3>预约会议室</h3>
                  <p>创建新的会议预约</p>
                </div>
              </el-card>
            </el-col>
            <el-col :span="8">
              <el-card class="action-card" @click="$router.push('/my-reservations')">
                <div class="card-content">
                  <el-icon size="40" color="#E6A23C"><List /></el-icon>
                  <h3>我的预约</h3>
                  <p>查看我的预约记录</p>
                </div>
              </el-card>
            </el-col>
          </el-row>
        </div>
      </el-main>
    </el-container>
  </div>
</template>

<script>
import { useRouter } from 'vue-router'
import { useUserStore } from '../stores/user'
import { ElMessage, ElMessageBox } from 'element-plus'
import NotificationCenter from './SysNotification.vue'

export default {
  name: 'Home',
  components: {
    // 2. 注册组件
    NotificationCenter
  },
  setup() {
    const router = useRouter()
    const userStore = useUserStore()
    
    const handleLogout = async () => {
      try {
        await ElMessageBox.confirm('确定要退出登录吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
        
        userStore.logout()
        ElMessage.success('已退出登录')
        router.push('/login')
      } catch {
        // 用户取消
      }
    }
    
    return {
      userStore,
      handleLogout
    }
  }
}
</script>

<style scoped>
.header-right {
  display: flex;
  align-items: center;
  gap: 15px; /* 已有，不用改 */
}
.home-container {
  height: 100vh;
}

.header {
  background: #fff;
  border-bottom: 1px solid #e4e7ed;
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 20px;
}

.header-left h1 {
  color: #409EFF;
  margin: 0;
}

.header-right {
  display: flex;
  align-items: center;
  gap: 15px;
}

.main {
  background: #f5f5f5;
  padding: 40px;
}

.welcome-section {
  text-align: center;
  margin-bottom: 40px;
}

.welcome-section h2 {
  color: #333;
  margin-bottom: 10px;
}

.welcome-section p {
  color: #666;
  font-size: 16px;
}

.action-cards {
  max-width: 1000px;
  margin: 0 auto;
}

.action-card {
  cursor: pointer;
  transition: all 0.3s;
  height: 200px;
}

.action-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 8px 25px rgba(0, 0, 0, 0.15);
}

.card-content {
  text-align: center;
  padding: 20px;
}

.card-content h3 {
  margin: 15px 0 10px;
  color: #333;
}

.card-content p {
  color: #666;
  margin: 0;
}
</style>
