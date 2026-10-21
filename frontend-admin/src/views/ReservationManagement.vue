<template>
  <div class="reservation-management">
    <h2>预约管理</h2>
    
    <el-table :data="reservations" style="width: 100%">
      <el-table-column prop="title" label="会议主题" />
      <el-table-column prop="roomName" label="会议室" />
      <el-table-column prop="userName" label="预约人" />
      <el-table-column prop="startTime" label="开始时间">
        <template #default="scope">
          {{ formatDateTime(scope.row.startTime) }}
        </template>
      </el-table-column>
      <el-table-column prop="endTime" label="结束时间">
        <template #default="scope">
          {{ formatDateTime(scope.row.endTime) }}
        </template>
      </el-table-column>
      <el-table-column prop="attendeeCount" label="参会人数" />
      <el-table-column prop="status" label="状态">
        <template #default="scope">
          <el-tag :type="getStatusType(scope.row.status)">
            {{ getStatusText(scope.row.status) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作">
        <template #default="scope">
          <el-button
            v-if="scope.row.status === 0"
            type="success"
            size="small"
            @click="auditReservation(scope.row, 1)"
          >
            通过
          </el-button>
          <el-button
            v-if="scope.row.status === 0"
            type="danger"
            size="small"
            @click="auditReservation(scope.row, 2)"
          >
            拒绝
          </el-button>
          <span v-if="scope.row.status !== 0">-</span>
        </template>
      </el-table-column>
    </el-table>
    
    <!-- 审核对话框 -->
    <el-dialog
      title="审核预约"
      v-model="auditDialogVisible"
      width="500px"
    >
      <el-form :model="auditForm" ref="auditFormRef" label-width="100px">
        <el-form-item label="会议主题">
          <span>{{ currentReservation?.title }}</span>
        </el-form-item>
        <el-form-item label="预约人">
          <span>{{ currentReservation?.userName }}</span>
        </el-form-item>
        <el-form-item label="会议室">
          <span>{{ currentReservation?.roomName }}</span>
        </el-form-item>
        <el-form-item label="时间">
          <span>{{ formatDateTime(currentReservation?.startTime) }} - {{ formatDateTime(currentReservation?.endTime) }}</span>
        </el-form-item>
        <el-form-item label="审核结果" prop="status">
          <el-radio-group v-model="auditForm.status">
            <el-radio :label="1">通过</el-radio>
            <el-radio :label="2">拒绝</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input
            v-model="auditForm.remark"
            type="textarea"
            :rows="3"
            placeholder="请输入审核备注"
          />
        </el-form-item>
      </el-form>
      
      <template #footer>
        <el-button @click="auditDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleAudit" :loading="loading">
          确定
        </el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script>
import { ref, reactive, onMounted } from 'vue'
import api from '../utils/api'
import { ElMessage } from 'element-plus'

export default {
  name: 'ReservationManagement',
  setup() {
    const reservations = ref([])
    const auditDialogVisible = ref(false)
    const loading = ref(false)
    const auditFormRef = ref()
    const currentReservation = ref(null)
    
    const auditForm = reactive({
      status: 1,
      remark: ''
    })
    
    const getStatusType = (status) => {
      const typeMap = {
        0: 'warning',
        1: 'success',
        2: 'danger',
        3: 'info',
        4: 'success'
      }
      return typeMap[status] || 'info'
    }
    
    const getStatusText = (status) => {
      const textMap = {
        0: '待审核',
        1: '已通过',
        2: '已拒绝',
        3: '已取消',
        4: '已结束',
      }
      return textMap[status] || '未知'
    }
    
    const formatDateTime = (dateTime) => {
      return new Date(dateTime).toLocaleString('zh-CN')
    }
    
    const loadReservations = async () => {
      try {
        console.log('正在加载预约列表...') // 调试日志
        const response = await api.get('/reservation/all')
        console.log('预约列表响应:', response) // 调试日志
        
        if (response.code === 200) {
          reservations.value = response.data
          console.log('预约数据:', reservations.value) // 调试日志
          ElMessage.success(`加载了 ${response.data.length} 条预约记录`)
        } else {
          ElMessage.error(response.message || '加载预约列表失败')
        }
      } catch (error) {
        console.error('加载预约列表错误:', error) // 调试日志
        ElMessage.error(error.message || '加载预约列表失败')
      }
    }
    
    const auditReservation = (reservation, status) => {
      currentReservation.value = reservation
      auditForm.status = status
      auditForm.remark = ''
      auditDialogVisible.value = true
    }
    
    const handleAudit = async () => {
      loading.value = true
      try {
        const response = await api.post('/reservation/audit', {
          id: currentReservation.value.id,
          status: auditForm.status,
          remark: auditForm.remark
        })
        
        if (response.code === 200) {
          ElMessage.success('审核完成')
          auditDialogVisible.value = false
          loadReservations()
        } else {
          ElMessage.error(response.message)
        }
      } catch (error) {
        ElMessage.error('审核失败')
      } finally {
        loading.value = false
      }
    }
    
    onMounted(() => {
      loadReservations()
    })
    
    return {
      reservations,
      auditDialogVisible,
      loading,
      auditFormRef,
      currentReservation,
      auditForm,
      getStatusType,
      getStatusText,
      formatDateTime,
      auditReservation,
      handleAudit
    }
  }
}
</script>

<style scoped>
.reservation-management h2 {
  margin-bottom: 20px;
  color: #333;
}
</style>
