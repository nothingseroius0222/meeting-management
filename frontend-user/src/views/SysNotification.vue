<template>
  <div class="notification-center">
    <!-- 顶部按钮：小铃铛 -->
    <el-popover
        placement="bottom-end"
        :width="400"
        trigger="click"
        @show="loadNotifications"
    >
      <!-- 触发按钮 -->
      <template #reference>
        <el-badge :value="unreadCount" :hidden="unreadCount === 0" class="item">
          <el-button type="text">
            <el-icon :size="24" color="#256">
              <Bell />
            </el-icon>
          </el-button>
        </el-badge>
      </template>

      <!-- 下拉面板 -->
      <div class="notification-popover">
        <div class="popover-header">
          <span>通知中心</span>
          <el-button type="text" size="mini" @click="markAllRead" v-if="unreadCount > 0">
            全部已读
          </el-button>
        </div>
        <div class="popover-list">
          <div v-if="notifications.length === 0" class="empty-tip">
            暂无通知
          </div>
          <div
              v-for="item in notifications.slice(0, 8)"
              :key="item.id"
              class="notification-item"
              :class="{ unread: item.isRead === 0 }"
              @click="handleClick(item)"
          >
            <div class="item-title">
              <span class="dot" v-if="item.isRead === 0"></span>
              {{ item.title }}
            </div>
            <div class="item-content">{{ item.content }}</div>
            <div class="item-time">{{ formatTime(item.createdAt) }}</div>
          </div>
        </div>
        <div class="popover-footer">
          <el-button type="text" size="mini" @click="openDrawer">
            查看全部通知
          </el-button>
        </div>
      </div>
    </el-popover>

    <!-- 抽屉：全部通知 -->
    <el-drawer
        title="通知中心"
        v-model="drawerVisible"
        direction="rtl"
        size="40%"
        @close="onDrawerClose"
    >
      <div class="drawer-content">
        <!-- Tab 切换栏 -->
        <div class="drawer-tabs">
          <el-button
              :type="activeTab === 'all' ? 'primary' : 'text'"
              @click="activeTab = 'all'"
          >
            全部 ({{ allNotifications.length }})
          </el-button>
          <el-button
              :type="activeTab === 'unread' ? 'primary' : 'text'"
              @click="activeTab = 'unread'"
          >
            未读 ({{ unreadCount }})
          </el-button>
          <el-button type="text" @click="markAllRead" v-if="unreadCount > 0" style="margin-left: auto;">
            全部已读
          </el-button>
        </div>

        <!-- 通知列表 -->
        <div v-if="filteredNotifications.length === 0" class="empty-tip">
          📭 暂无通知
        </div>
        <div
            v-for="item in filteredNotifications"
            :key="item.id"
            class="drawer-item"
            :class="{ unread: item.isRead === 0 }"
        >
          <div class="item-status">
            <span class="dot" v-if="item.isRead === 0"></span>
            <span class="read-status" v-else>已读</span>
          </div>
          <div class="item-content" @click="handleClick(item)">
            <h4>{{ item.title }}</h4>
            <p>{{ item.content }}</p>
            <span class="time">{{ formatTime(item.createdAt) }}</span>
          </div>
          <!-- 删除按钮 -->
          <el-button
              type="text"
              size="small"
              class="delete-btn"
              @click="deleteNotification(item.id)"
          >
            <el-icon><Delete /></el-icon>
          </el-button>
        </div>
      </div>
    </el-drawer>
    <!-- 弹出框：展示通知的详细信息 -->
    <el-dialog
        v-model="centerDialogVisible"
        :title="currentNotification?.title || '通知详情'"
        width="500"
        center
    >
      <div v-if="currentNotification">
        <p style="color: #333; line-height: 1.8; white-space: pre-wrap;">
          {{ currentNotification.content }}
        </p>
        <p style="color: #999; font-size: 12px; margin-top: 12px;">
          {{ formatTime(currentNotification.createdAt) }}
        </p>
      </div>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="centerDialogVisible = false">关闭</el-button>
          <el-button type="primary" @click="centerDialogVisible = false">
            我知道了
          </el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script>
import { ref, onMounted, computed  } from 'vue'
import api from '../utils/api'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Delete } from '@element-plus/icons-vue'  // 引入删除图标

export default {
  name: 'NotificationCenter',
  components: { Delete },  // 注册图标
  setup() {
    const userId = 4 // 实际使用时从 userStore 获取

    const unreadCount = ref(0)
    const notifications = ref([])
    const allNotifications = ref([])
    const drawerVisible = ref(false)
    const centerDialogVisible = ref(false)
    const currentNotification = ref(null)  // 当前查看的通知
    const activeTab = ref('all')  // 当前选中的 tab

    // 加载未读数量
    const loadUnreadCount = async () => {
      try {
        const res = await api.get('/notification/unread-count', { params: { userId } })
        unreadCount.value = res.data || 0
      } catch (error) {
        console.error('加载未读数量失败', error)
      }
    }

    // 加载通知列表（下拉面板）
    const loadNotifications = async () => {
      try {
        const res = await api.get('/notification/sysNotification-list', { params: { userId } })
        notifications.value = res.data || []
      } catch (error) {
        console.error('加载通知列表失败', error)
      }
    }

    // 加载全部通知（抽屉）
    const loadAllNotifications = async () => {
      try {
        const res = await api.get('/notification/sysNotification-list', { params: { userId } })
        allNotifications.value = res.data || []
      } catch (error) {
        console.error('加载全部通知失败', error)
      }
    }

    // 打开抽屉
    const openDrawer = async () => {
      drawerVisible.value = true
      await loadAllNotifications()
    }

    // 关闭抽屉
    const onDrawerClose = () => {
      drawerVisible.value = false
      loadUnreadCount() // 刷新未读数量
    }

    // 点击通知
    const handleClick = async (item) => {
      console.log('点击了通知:', item)  // 👈 加这行
      // 如果未读，标记为已读
      if (item.isRead === 0) {
        try {
          await api.post('/notification/read', { id: item.id, userId })
          item.isRead = 1
          unreadCount.value = Math.max(0, unreadCount.value - 1)
        } catch (error) {
          console.error('标记已读失败', error)
        }
      }
      // 跳转
      currentNotification.value = item
      centerDialogVisible.value = true
      console.log('对话框状态:', centerDialogVisible.value)  // 👈 加这行x
    }

    // 全部标记为已读
    const markAllRead = async () => {
      try {
        await api.get('/notification/read-all', { params: { userId } })
        unreadCount.value = 0
        notifications.value.forEach(n => n.isRead = 1)
        allNotifications.value.forEach(n => n.isRead = 1)
        ElMessage.success('全部已读')
      } catch (error) {
        ElMessage.error('操作失败')
      }
    }

    // 格式化时间
    const formatTime = (time) => {
      if (!time) return ''
      const date = new Date(time)
      const now = new Date()
      const diff = (now - date) / 1000 // 秒

      if (diff < 60) return '刚刚'
      if (diff < 3600) return Math.floor(diff / 60) + '分钟前'
      if (diff < 86400) return Math.floor(diff / 3600) + '小时前'
      if (diff < 2592000) return Math.floor(diff / 86400) + '天前'

      return date.toLocaleString('zh-CN')
    }

    // 计算属性：根据 activeTab 过滤通知
    const filteredNotifications = computed(() => {
      if (activeTab.value === 'unread') {
        return allNotifications.value.filter(item => item.isRead === 0)
      }
      return allNotifications.value
    })

    // 删除单条通知
    const deleteNotification = async (id) => {
      try {
        await ElMessageBox.confirm('确定要删除这条通知吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
        await api.delete(`/notification/delete/${id}`)
        // 从列表中移除
        allNotifications.value = allNotifications.value.filter(n => n.id !== id)
        notifications.value = notifications.value.filter(n => n.id !== id)
        // 重新计算未读数量（如果删除的是未读，要减1）
        const remainingUnread = allNotifications.value.filter(n => n.isRead === 0).length
        unreadCount.value = remainingUnread
        ElMessage.success('已删除')
      } catch (error) {
        if (error !== 'cancel') {
          ElMessage.error('删除失败')
        }
      }
    }

    onMounted(() => {
      loadUnreadCount()
      // 定时刷新未读数量（每30秒）
      setInterval(loadUnreadCount, 30000)
    })

    return {
      unreadCount,
      notifications,
      allNotifications,
      drawerVisible,
      centerDialogVisible,
      currentNotification,
      loadNotifications,
      openDrawer,
      onDrawerClose,
      handleClick,
      markAllRead,
      formatTime,
      activeTab,
      filteredNotifications,
      deleteNotification
    }
  }
}
</script>

<style scoped>
.notification-popover {
  max-height: 400px;
}
.popover-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 10px 0;
  border-bottom: 1px solid #eee;
  font-weight: bold;
}
.popover-list {
  max-height: 300px;
  overflow-y: auto;
}
.notification-item {
  padding: 12px 0;
  border-bottom: 1px solid #f5f5f5;
  cursor: pointer;
}
.notification-item:hover {
  background-color: #f5f7fa;
}
.notification-item.unread {
  background-color: #f0f9ff;
}
.item-title {
  font-weight: 500;
  margin-bottom: 4px;
}
.item-title .dot {
  display: inline-block;
  width: 6px;
  height: 6px;
  background: #f56c6c;
  border-radius: 50%;
  margin-right: 8px;
}
.item-content {
  color: #666;
  font-size: 13px;
  margin-bottom: 4px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}
.item-time {
  color: #999;
  font-size: 12px;
}
.empty-tip {
  text-align: center;
  color: #999;
  padding: 40px 0;
}
.drawer-content {
  padding: 0 20px;
}
.drawer-header {
  text-align: right;
  padding: 10px 0;
  border-bottom: 1px solid #eee;
}
.drawer-item {
  display: flex;
  padding: 16px 12px;
  border-bottom: 1px solid #f5f5f5;
  cursor: pointer;
}
.drawer-item:hover {
  background-color: #f5f7fa;
}
.drawer-item.unread {
  background-color: #f0f9ff;
  border-left: 3px solid #409eff;
}
.drawer-item .item-status {
  margin-right: 12px;
  flex-shrink: 0;
}
.drawer-item .item-status .dot {
  display: inline-block;
  width: 8px;
  height: 8px;
  background: #f56c6c;
  border-radius: 50%;
  margin-top: 8px;
}
.drawer-item .item-content {
  flex: 1;
  white-space: normal;
}
.drawer-item .item-content h4 {
  margin: 0 0 8px 0;
}
.drawer-item .item-content p {
  margin: 0 0 8px 0;
  color: #666;
}
.drawer-item .item-content .time {
  color: #999;
  font-size: 12px;
}
.drawer-tabs {
  display: flex;
  align-items: center;
  padding: 10px 20px;
  border-bottom: 1px solid #eee;
  gap: 8px;
}
.drawer-tabs .el-button {
  font-size: 14px;
}
.drawer-item {
  display: flex;
  align-items: center;
  padding: 16px 12px;
  border-bottom: 1px solid #f5f5f5;
}
.drawer-item .item-content {
  flex: 1;
  cursor: pointer;
  padding-right: 12px;
}
.drawer-item .delete-btn {
  flex-shrink: 0;
  color: #999;
  transition: color 0.2s;
}
.drawer-item .delete-btn:hover {
  color: #f56c6c;
}
</style>