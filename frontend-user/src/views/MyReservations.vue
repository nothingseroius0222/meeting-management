<template>
  <div class="my-reservations">
    <div class="page-header">
      <h2>我的预约</h2>
      <el-button type="primary" @click="$router.push('/create-reservation')">
        新建预约
      </el-button>
    </div>

    <!-- Tab 切换栏（类似通知中心） -->
    <div class="reservation-tabs">
      <el-button
          v-for="tab in tabs"
          :key="tab.key"
          :type="activeTab === tab.key ? 'primary' : 'text'"
          @click="activeTab = tab.key"
      >
        {{ tab.label }} <span class="tab-count">{{ getTabCount(tab.key) }}</span>
      </el-button>
    </div>

    <!-- 表格 -->
    <el-table :data="filteredReservations" style="width: 100%">
      <el-table-column prop="title" label="会议主题" />
      <el-table-column prop="roomName" label="会议室" />
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
          <el-tag :type="getStatusType(scope.row)">
            {{ getStatusText(scope.row) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="180">
        <template #default="scope">
          <!-- 取消按钮：待审核(0) 或 已通过(1) 且未结束 -->
          <el-button
              v-if="canCancel(scope.row)"
              type="warning"
              size="small"
              @click="cancelReservation(scope.row)"
          >
            取消
          </el-button>
          <!-- 删除按钮：已拒绝(2)、已取消(3)、已结束 -->
          <el-button
              v-if="canDelete(scope.row)"
              type="danger"
              size="small"
              @click="deleteReservation(scope.row)"
          >
            删除
          </el-button>
          <el-button
              v-if="canDelay(scope.row)"
              type="danger"
              size="small"
              @click="delayReservation(scope.row)"
          >
            延时
          </el-button>
          <span v-if="!canCancel(scope.row) && !canDelete(scope.row)">-</span>
        </template>
      </el-table-column>
    </el-table>
    <!-- 延时对话框 -->
    <el-dialog
        v-model="delayDialogVisible"
        title="延时会议"
        width="400px"
        append-to-body
    >
      <div style="padding: 10px 0;">
        <p><strong>会议主题：</strong>{{ currentDelayReservation?.title }}</p>
        <p><strong>原结束时间：</strong>{{ formatDateTime(currentDelayReservation?.endTime) }}</p>
        <div style="margin-top: 15px;">
          <label>延长分钟数：</label>
          <el-input-number
              v-model="delayMinutes"
              :min="5"
              :max="120"
              :step="5"
              size="default"
              controls-position="right"
          />
          <span style="margin-left: 10px; color: #999;">（5~120分钟）</span>
        </div>
      </div>
      <template #footer>
        <el-button @click="delayDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="confirmDelay" :loading="delayLoading">
          确认延时
        </el-button>
      </template>
    </el-dialog>
    <!-- 空状态 -->
    <div v-if="filteredReservations.length === 0" class="empty-tip">
      📭 暂无 {{ getCurrentTabLabel() }}
    </div>
  </div>
</template>

<script>
import { ref, onMounted, computed } from 'vue'
import { useUserStore } from '../stores/user'
import api from '../utils/api'
import { ElMessage, ElMessageBox } from 'element-plus'

export default {
  name: 'MyReservations',
  setup() {
    const userStore = useUserStore()
    const reservations = ref([])
    const activeTab = ref('all')
    // 延时对话框相关
    const delayDialogVisible = ref(false)
    const delayMinutes = ref(30)  // 默认延后30分钟
    const currentDelayReservation = ref(null)  // 当前要延时的预约
    const delayLoading = ref(false)  // 提交加载状态

    // Tab 配置
    const tabs = [
      { key: 'all', label: '全部' },
      { key: 'pending', label: '待审核' },
      { key: 'approved', label: '已预约' },
      { key: 'ongoing', label: '进行中' },
      { key: 'ended', label: '已结束' },
      { key: 'rejected', label: '已拒绝' }
    ]

    // 判断会议状态（基于时间和状态码）
    const getMeetingStatus = (reservation) => {
      const now = new Date()
      const start = new Date(reservation.startTime)
      const end = new Date(reservation.endTime)

      // 状态码判断
      if (reservation.status === 0) return 'pending'      // 待审核
      if (reservation.status === 2) return 'rejected'     // 已拒绝
      if (reservation.status === 3) return 'cancelled'    // 已取消
      if (reservation.status === 4) return 'ended'

      // 已通过(1) 根据时间判断
      if (reservation.status === 1) {
        if (now < start) return 'approved'    // 未开始 → 已预约
        if (now >= start && now <= end) return 'ongoing'  // 进行中
      }

      return 'unknown'
    }

    // 过滤后的预约列表
    const filteredReservations = computed(() => {
      if (activeTab.value === 'all') {
        return reservations.value
      }

      // 特殊处理：全部、已预约、进行中、已结束 是基于时间和状态组合的
      return reservations.value.filter(item => {
        const status = getMeetingStatus(item)
        return status === activeTab.value
      })
    })

    // 获取 Tab 数量
    const getTabCount = (tabKey) => {
      if (tabKey === 'all') return reservations.value.length
      return reservations.value.filter(item => {
        const status = getMeetingStatus(item)
        return status === tabKey
      }).length
    }

    // 获取当前 Tab 的显示名称
    const getCurrentTabLabel = () => {
      const tab = tabs.find(t => t.key === activeTab.value)
      return tab ? tab.label : ''
    }

    // 状态标签类型
    const getStatusType = (row) => {
      const status = getMeetingStatus(row)
      const typeMap = {
        pending: 'warning',   // 待审核 → 黄色
        approved: 'success',  // 已预约 → 绿色
        ongoing: 'primary',   // 进行中 → 蓝色
        ended: 'info',        // 已结束 → 灰色
        rejected: 'danger',   // 已拒绝 → 红色
        cancelled: 'info'     // 已取消 → 灰色
      }
      return typeMap[status] || 'info'
    }

    // 状态显示文本
    const getStatusText = (row) => {
      const status = getMeetingStatus(row)
      const textMap = {
        pending: '待审核',
        approved: '已预约',
        ongoing: '进行中',
        ended: '已结束',
        rejected: '已拒绝',
        cancelled: '已取消'
      }
      return textMap[status] || '未知'
    }

    // 是否能取消（待审核 或 已通过且未结束）
    const canCancel = (row) => {
      if (row.status === 0) return true
      if (row.status === 1) {
        const now = new Date()
        const end = new Date(row.endTime)
        return now < end  // 还没结束才能取消
      }
      return false
    }

    // 是否能删除（已拒绝、已取消、已结束）
    const canDelete = (row) => {
      const status = getMeetingStatus(row)
      return ['ended', 'rejected', 'cancelled','approved'].includes(status)
    }

    // 是否能延时（已预约、进行中）
    const canDelay = (row) => {
      const status = getMeetingStatus(row)
      return ['approved', 'ongoing','pending'].includes(status)
    }

    const formatDateTime = (dateTime) => {
      if (!dateTime) return ''
      return new Date(dateTime).toLocaleString('zh-CN')
    }

    const loadReservations = async () => {
      try {
        const response = await api.get('/reservation/my', {
          params: { userId: userStore.user.id }
        })
        if (response.code === 200) {
          reservations.value = response.data || []
        }
      } catch (error) {
        ElMessage.error('加载预约记录失败')
        console.error('Error loading reservations:', error)
      }
    }

    const cancelReservation = async (reservation) => {
      try {
        await ElMessageBox.confirm('确定要取消这个预约吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })

        const response = await api.post('/reservation/cancel', {
          id: reservation.id,
          userId: userStore.user.id
        })

        if (response.code === 200) {
          ElMessage.success('预约已取消')
          loadReservations()
        } else {
          ElMessage.error(response.message)
        }
      } catch (error) {
        if (error !== 'cancel') {
          ElMessage.error('取消预约失败')
        }
      }
    }

    const deleteReservation = async (reservation) => {
      try {
        await ElMessageBox.confirm('确定要删除这个预约吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })

        const response = await api.post('/reservation/delete', {
          id: reservation.id,
          userId: userStore.user.id,
          status: reservation.status
        })

        if (response.code === 200) {
          ElMessage.success(response.message)
          loadReservations()
        } else {
          ElMessage.error(response.message)
        }
      } catch (error) {
        if (error !== 'cancel') {
          ElMessage.error('删除失败')
        }
      }
    }

    /*延时对话框*/
    const delayReservation = async (reservation) => {
      currentDelayReservation.value = reservation
      delayMinutes.value = 30  // 重置默认值
      delayDialogVisible.value = true

    }
    /*确认延时*/
    const confirmDelay = async () => {
      if (!currentDelayReservation.value) {
        ElMessage.warning('请选择要延时的会议')
        return
      }

      if (delayMinutes.value < 5) {
        ElMessage.warning('延长分钟数至少为5分钟')
        return
      }
      delayLoading.value = true
      try {
        const response = await api.post('/reservation/delay', {
          id: currentDelayReservation.value.id,
          roomId: currentDelayReservation.value.roomId,
          userId: userStore.user.id,
          delayMinutes:delayMinutes.value,
          status: currentDelayReservation.value.status
        })
        if (response.code === 200) {
          ElMessage.success(response.message)
          delayDialogVisible.value = false
          loadReservations()
        } else {
          ElMessage.error(response.message)
        }
      } catch (error) {
        ElMessage.error('延时请求失败')
      }finally {
        delayLoading.value = false
      }
    }

    onMounted(() => {
      loadReservations()
    })

    return {
      reservations,
      activeTab,
      tabs,
      filteredReservations,
      getTabCount,
      getCurrentTabLabel,
      getStatusType,
      getStatusText,
      canCancel,
      canDelete,
      canDelay,
      confirmDelay,
      delayDialogVisible,
      delayMinutes,
      currentDelayReservation,
      delayLoading,
      formatDateTime,
      cancelReservation,
      deleteReservation,
      delayReservation
    }
  }
}
</script>

<style scoped>
.my-reservations {
  padding: 20px;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.page-header h2 {
  color: #333;
  margin: 0;
}

.reservation-tabs {
  display: flex;
  gap: 8px;
  margin-bottom: 20px;
  border-bottom: 1px solid #eee;
  padding-bottom: 12px;
  flex-wrap: wrap;
}

.reservation-tabs .el-button {
  font-size: 14px;
}

.reservation-tabs .el-button.is-text {
  color: #666;
}

.reservation-tabs .el-button.is-text:hover {
  color: #409eff;
}

.tab-count {
  display: inline-block;
  background-color: #f0f0f0;
  color: #666;
  border-radius: 10px;
  padding: 0 8px;
  font-size: 12px;
  margin-left: 4px;
  min-width: 18px;
  text-align: center;
}

.el-button.is-primary .tab-count {
  background-color: rgba(255, 255, 255, 0.3);
  color: #fff;
}

.empty-tip {
  text-align: center;
  color: #999;
  padding: 60px 0;
  font-size: 16px;
}
</style>