<template>
  <el-container class="layout-container">
    <!-- 侧边栏 -->
    <el-aside width="200px" class="sidebar">
      <div class="logo">
        <h3>管理后台</h3>
      </div>
      <el-menu
        :default-active="$route.path"
        class="sidebar-menu"
        router
        background-color="#304156"
        text-color="#bfcbd9"
        active-text-color="#409EFF"
      >
        <el-menu-item index="/dashboard">
          <el-icon><Odometer /></el-icon>
          <span>仪表盘</span>
        </el-menu-item>
        <el-menu-item index="/rooms">
          <el-icon><OfficeBuilding /></el-icon>
          <span>会议室管理</span>
        </el-menu-item>
        <el-menu-item index="/reservations">
          <el-icon><Calendar /></el-icon>
          <span>预约管理</span>
        </el-menu-item>
        <el-menu-item index="/users">
          <el-icon><User /></el-icon>
          <span>用户管理</span>
        </el-menu-item>
      </el-menu>
    </el-aside>
    
    <!-- 主内容区 -->
    <el-container>
      <!-- 头部 -->
      <el-header class="header">
        <div class="header-left">
          <el-breadcrumb separator="/">
            <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
            <el-breadcrumb-item>{{ getCurrentPageTitle() }}</el-breadcrumb-item>
          </el-breadcrumb>
        </div>
        <div class="header-right">
          <span>欢迎，{{ userStore.user?.realName || userStore.user?.username }}</span>
          <el-button type="primary" @click="handleLogout">退出登录</el-button>
        </div>
      </el-header>
      
      <!-- 内容区 -->
      <el-main class="main">
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<script>
import { useRouter, useRoute } from 'vue-router'
import { useUserStore } from '../stores/user'
import { ElMessage, ElMessageBox } from 'element-plus'

export default {
  name: 'Layout',
  setup() {
    const router = useRouter()
    const route = useRoute()
    const userStore = useUserStore()
    
    const getCurrentPageTitle = () => {
      const titleMap = {
        '/dashboard': '仪表盘',
        '/rooms': '会议室管理',
        '/reservations': '预约管理',
        '/users': '用户管理'
      }
      return titleMap[route.path] || '未知页面'
    }
    
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
      getCurrentPageTitle,
      handleLogout
    }
  }
}
</script>

<style scoped>
.layout-container {
  height: 100vh;
}

.sidebar {
  background-color: #304156;
}

.logo {
  padding: 20px;
  text-align: center;
  border-bottom: 1px solid #434a50;
}

.logo h3 {
  color: #fff;
  margin: 0;
}

.sidebar-menu {
  border: none;
}

.header {
  background: #fff;
  border-bottom: 1px solid #e4e7ed;
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 20px;
}

.header-right {
  display: flex;
  align-items: center;
  gap: 15px;
}

.main {
  background: #f5f5f5;
  padding: 20px;
}
</style>
