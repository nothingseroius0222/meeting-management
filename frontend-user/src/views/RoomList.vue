<template>
  <div class="room-list">
    <h2>会议室列表</h2>
    
    <el-row :gutter="20">
      <el-col :span="8" v-for="room in rooms" :key="room.id">
        <el-card class="room-card" @click="goToReservation(room)">
          <div class="room-content">
            <h3>{{ room.roomName }}</h3>
            <p class="location">{{ room.location }}</p>
            <p class="capacity">容量: {{ room.capacity }}人</p>
            <p class="equipment">{{ room.equipment }}</p>
            <el-button type="primary" size="small">预约</el-button>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import api from '../utils/api'
import { ElMessage } from 'element-plus'

export default {
  name: 'RoomList',
  setup() {
    const router = useRouter()
    const rooms = ref([])
    
    const loadRooms = async () => {
      try {
        const response = await api.get('/room/list')
        if (response.code === 200) {
          rooms.value = response.data
        }
      } catch (error) {
        ElMessage.error('加载会议室列表失败')
      }
    }
    
    const goToReservation = (room) => {
      router.push({
        path: '/create-reservation',
        query: { roomId: room.id }
      })
    }
    
    onMounted(() => {
      loadRooms()
    })
    
    return {
      rooms,
      goToReservation
    }
  }
}
</script>

<style scoped>
.room-list h2 {
  margin-bottom: 20px;
  color: #333;
}

.room-card {
  cursor: pointer;
  transition: all 0.3s;
  margin-bottom: 20px;
}

.room-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}

.room-content h3 {
  margin: 0 0 10px 0;
  color: #409EFF;
}

.room-content p {
  margin: 5px 0;
  color: #666;
}

.location {
  font-weight: 500;
}

.capacity {
  color: #67C23A;
}

.equipment {
  font-size: 14px;
  line-height: 1.4;
}
</style>
