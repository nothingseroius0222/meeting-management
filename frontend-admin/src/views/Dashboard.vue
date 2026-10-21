<template>
  <div class="dashboard">
    <h2>仪表盘</h2>
    
    <el-row :gutter="20" class="stats-row">
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-content">
            <div class="stat-icon">
              <el-icon size="40" color="#409EFF"><OfficeBuilding /></el-icon>
            </div>
            <div class="stat-info">
              <h3>{{ stats.totalRooms }}</h3>
              <p>会议室总数</p>
            </div>
          </div>
        </el-card>
      </el-col>
      
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-content">
            <div class="stat-icon">
              <el-icon size="40" color="#67C23A"><Calendar /></el-icon>
            </div>
            <div class="stat-info">
              <h3>{{ stats.totalReservations }}</h3>
              <p>预约总数</p>
            </div>
          </div>
        </el-card>
      </el-col>
      
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-content">
            <div class="stat-icon">
              <el-icon size="40" color="#E6A23C"><Clock /></el-icon>
            </div>
            <div class="stat-info">
              <h3>{{ stats.pendingReservations }}</h3>
              <p>待审核预约</p>
            </div>
          </div>
        </el-card>
      </el-col>
      
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-content">
            <div class="stat-icon">
              <el-icon size="40" color="#F56C6C"><User /></el-icon>
            </div>
            <div class="stat-info">
              <h3>{{ stats.totalUsers }}</h3>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>
    
    <el-row :gutter="20" class="content-row">
      <el-col :span="12">
        <el-card>
          <template #header>
            <span>最近预约</span>
          </template>
          <el-table :data="recentReservations" style="width: 100%">
            <el-table-column prop="title" label="会议主题" />
            <el-table-column prop="roomName" label="会议室" />
            <el-table-column prop="startTime" label="开始时间" />
            <el-table-column prop="status" label="状态">
              <template #default="scope">
                <el-tag :type="getStatusType(scope.row.status)">
                  {{ getStatusText(scope.row.status) }}
                </el-tag>
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-col>
      
      <el-col :span="12">
        <el-card>
          <template #header>
            <span>会议室使用情况</span>
          </template>
          <div class="room-usage">
            <div v-for="room in roomUsage" :key="room.id" class="usage-item">
              <div class="room-name">{{ room.roomName }}</div>
              <el-progress :percentage="room.usagePercentage" />
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { ref, reactive, onMounted } from 'vue'
import api from '../utils/api'
import { ElMessage } from 'element-plus'

export default {
  name: 'Dashboard',
  setup() {
    const stats = reactive({
      totalRooms: 0,
      totalReservations: 0,
      pendingReservations: 0,
      totalUsers: 0
    })
    
    const recentReservations = ref([])
    const roomUsage = ref([])
    
    const getStatusType = (status) => {
      const typeMap = {
        0: 'warning',
        1: 'success',
        2: 'danger',
        3: 'info',
        4: 'success',
      }
      return typeMap[status] || 'info'
    }
    
    const getStatusText = (status) => {
      const textMap = {
        0: '待审核',
        1: '已通过',
        2: '已拒绝',
        3: '已取消',
        4: '已结束'
      }
      return textMap[status] || '未知'
    }
    
    const loadDashboardData = async () => {
      try {
        // 加载统计数据
        const [roomsRes, reservationsRes,usersRes,usage] = await Promise.all([
          api.get('/room/list'),
          api.get('/reservation/all'),
          api.get('/user/list'),
          api.post('/reservation/usage')
        ])
        
        stats.totalRooms = roomsRes.data?.length || 0
        stats.totalReservations = reservationsRes.data?.length || 0
        stats.pendingReservations = reservationsRes.data?.filter(r => r.status === 0).length || 0
        stats.totalUsers = usersRes.data?.length || 0 // 模拟数据
        
        // 加载最近预约
        recentReservations.value = reservationsRes.data?.slice(0, 5) || []
        
        // 模拟会议室使用情况
        roomUsage.value = usage.data?.map(room => ({
          id: room.id,
          roomName: room.roomName,
          usagePercentage: room.usagePercentage
        })) || []
        
      } catch (error) {
        ElMessage.error('加载数据失败')
      }
    }
    
    onMounted(() => {
      loadDashboardData()
    })
    
    return {
      stats,
      recentReservations,
      roomUsage,
      getStatusType,
      getStatusText
    }
  }
}
</script>

<style scoped>
.dashboard h2 {
  margin-bottom: 20px;
  color: #333;
}

.stats-row {
  margin-bottom: 20px;
}

.stat-card {
  cursor: pointer;
  transition: all 0.3s;
}

.stat-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}

.stat-content {
  display: flex;
  align-items: center;
  padding: 10px;
}

.stat-icon {
  margin-right: 15px;
}

.stat-info h3 {
  margin: 0 0 5px 0;
  font-size: 24px;
  color: #333;
}

.stat-info p {
  margin: 0;
  color: #666;
  font-size: 14px;
}

.content-row {
  margin-top: 20px;
}

.room-usage {
  padding: 10px 0;
}

.usage-item {
  margin-bottom: 20px;
}

.room-name {
  margin-bottom: 8px;
  font-weight: 500;
  color: #333;
}
</style>
