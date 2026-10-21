<template>
  <div class="create-reservation">
    <h2>预约会议室</h2>
    
    <el-card>
      <el-form :model="reservationForm" :rules="rules" ref="reservationFormRef" label-width="100px">
        <el-form-item label="会议室" prop="roomId">
          <el-select v-model="reservationForm.roomId" placeholder="请选择会议室" style="width: 100%">
            <el-option
              v-for="room in rooms"
              :key="room.id"
              :label="room.roomName"
              :value="room.id"
            />
          </el-select>
        </el-form-item>
        
        <el-form-item label="会议主题" prop="title">
          <el-input v-model="reservationForm.title" placeholder="请输入会议主题" />
        </el-form-item>
        
        <el-form-item label="会议描述" prop="description">
          <el-input
            v-model="reservationForm.description"
            type="textarea"
            :rows="3"
            placeholder="请输入会议描述"
          />
        </el-form-item>
        
        <el-form-item label="开始时间" prop="startTime">
          <el-date-picker
            v-model="reservationForm.startTime"
            type="datetime"
            placeholder="选择开始时间"
            style="width: 100%"
          />
        </el-form-item>
        
        <el-form-item label="结束时间" prop="endTime">
          <el-date-picker
            v-model="reservationForm.endTime"
            type="datetime"
            placeholder="选择结束时间"
            style="width: 100%"
          />
        </el-form-item>
        
        <el-form-item label="参会人数" prop="attendeeCount">
          <el-input-number
            v-model="reservationForm.attendeeCount"
            :min="1"
            :max="100"
            style="width: 100%"
          />
        </el-form-item>
        
        <el-form-item>
          <el-button type="primary" @click="handleSubmit" :loading="loading">
            提交预约
          </el-button>
          <el-button @click="goBack">返回</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script>
import { ref, reactive, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { useUserStore } from '../stores/user'
import api from '../utils/api'
import { ElMessage } from 'element-plus'

export default {
  name: 'CreateReservation',
  setup() {
    const router = useRouter()
    const route = useRoute()
    const userStore = useUserStore()
    const reservationFormRef = ref()
    const loading = ref(false)
    const rooms = ref([])
    
    const reservationForm = reactive({
      roomId: null,
      title: '',
      description: '',
      startTime: null,
      endTime: null,
      attendeeCount: 1
    })
    
    const rules = {
      roomId: [
        { required: true, message: '请选择会议室', trigger: 'change' }
      ],
      title: [
        { required: true, message: '请输入会议主题', trigger: 'blur' }
      ],
      startTime: [
        { required: true, message: '请选择开始时间', trigger: 'change' }
      ],
      endTime: [
        { required: true, message: '请选择结束时间', trigger: 'change' }
      ],
      attendeeCount: [
        { required: true, message: '请输入参会人数', trigger: 'blur' }
      ]
    }
    
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
    
    const handleSubmit = async () => {
      if (!reservationFormRef.value) return
      
      // 检查用户是否登录
      if (!userStore.user || !userStore.user.id) {
        ElMessage.error('请先登录')
        router.push('/login')
        return
      }
      
      await reservationFormRef.value.validate(async (valid) => {
        if (valid) {
          loading.value = true
          try {
            const formData = {
              ...reservationForm,
              userId: userStore.user.id,
              startTime: reservationForm.startTime.toISOString(),
              endTime: reservationForm.endTime.toISOString()
            }
            
            console.log('提交预约数据:', formData) // 调试日志
            
            const response = await api.post('/reservation/create', formData)
            console.log('预约响应:', response) // 调试日志
            
            if (response.code === 200) {
              ElMessage.success('预约提交成功，等待审核')
              router.push('/my-reservations')
            } else {
              ElMessage.error(response.message || '预约提交失败')
            }
          } catch (error) {
            console.error('预约提交错误:', error) // 调试日志
            ElMessage.error(error.message || '预约提交失败')
          } finally {
            loading.value = false
          }
        }
      })
    }
    
    const goBack = () => {
      router.back()
    }
    
    onMounted(() => {
      loadRooms()
      
      // 如果URL中有roomId参数，自动选择
      if (route.query.roomId) {
        reservationForm.roomId = parseInt(route.query.roomId)
      }
    })
    
    return {
      reservationFormRef,
      loading,
      rooms,
      reservationForm,
      rules,
      handleSubmit,
      goBack
    }
  }
}
</script>

<style scoped>
.create-reservation h2 {
  margin-bottom: 20px;
  color: #333;
}
</style>
