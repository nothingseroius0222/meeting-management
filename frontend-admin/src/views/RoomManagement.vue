<template>
  <div class="room-management">
    <div class="header">
      <h2>会议室管理</h2>
      <el-button type="primary" @click="showCreateDialog">添加会议室</el-button>
    </div>
    
    <el-table :data="rooms" style="width: 100%">
      <el-table-column prop="roomName" label="会议室名称" />
      <el-table-column prop="location" label="位置" />
      <el-table-column prop="capacity" label="容量" />
      <el-table-column prop="equipment" label="设备" />
      <el-table-column prop="status" label="状态">
        <template #default="scope">
          <el-tag :type="scope.row.status === 1 ? 'success' : 'danger'">
            {{ scope.row.status === 1 ? '启用' : '禁用' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作">
        <template #default="scope">
          <el-button type="primary" size="small" @click="editRoom(scope.row)">
            编辑
          </el-button>
          <el-button type="danger" size="small" @click="deleteRoom(scope.row)">
            删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    
    <!-- 添加/编辑对话框 -->
    <el-dialog
      :title="dialogTitle"
      v-model="dialogVisible"
      width="500px"
    >
      <el-form :model="roomForm" :rules="rules" ref="roomFormRef" label-width="100px">
        <el-form-item label="会议室名称" prop="roomName">
          <el-input v-model="roomForm.roomName" />
        </el-form-item>
        <el-form-item label="位置" prop="location">
          <el-input v-model="roomForm.location" />
        </el-form-item>
        <el-form-item label="容量" prop="capacity">
          <el-input-number v-model="roomForm.capacity" :min="1" :max="1000" />
        </el-form-item>
        <el-form-item label="设备" prop="equipment">
          <el-input v-model="roomForm.equipment" type="textarea" :rows="3" />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="roomForm.status">
            <el-radio :label="1">启用</el-radio>
            <el-radio :label="0">禁用</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit" :loading="loading">
          确定
        </el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script>
import { ref, reactive, onMounted } from 'vue'
import api from '../utils/api'
import { ElMessage, ElMessageBox } from 'element-plus'

export default {
  name: 'RoomManagement',
  setup() {
    const rooms = ref([])
    const dialogVisible = ref(false)
    const loading = ref(false)
    const isEdit = ref(false)
    const roomFormRef = ref()
    
    const roomForm = reactive({
      id: null,
      roomName: '',
      location: '',
      capacity: 10,
      equipment: '',
      status: 1
    })
    
    const rules = {
      roomName: [
        { required: true, message: '请输入会议室名称', trigger: 'blur' }
      ],
      location: [
        { required: true, message: '请输入位置', trigger: 'blur' }
      ],
      capacity: [
        { required: true, message: '请输入容量', trigger: 'blur' }
      ]
    }
    
    const dialogTitle = ref('添加会议室')
    
    const loadRooms = async () => {
      try {
        const response = await api.get('/room/undeletedlist')
        if (response.code === 200) {
          rooms.value = response.data
        }
      } catch (error) {
        ElMessage.error('加载会议室列表失败')
      }
    }
    
    const showCreateDialog = () => {
      isEdit.value = false
      dialogTitle.value = '添加会议室'
      resetForm()
      dialogVisible.value = true
    }
    
    const editRoom = (room) => {
      isEdit.value = true
      dialogTitle.value = '编辑会议室'
      Object.assign(roomForm, room)
      dialogVisible.value = true
    }
    
    const deleteRoom = async (room) => {
      try {
        await ElMessageBox.confirm('确定要删除这个会议室吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
        
        const response = await api.delete(`/room/${room.id}`)
        if (response.code === 200) {
          ElMessage.success(response.data)
          loadRooms()
        } else {
          ElMessage.error(response.message)
        }
      } catch (error) {
        if (error !== 'cancel') {
          ElMessage.error('删除失败')
        }
      }
    }
    
    const handleSubmit = async () => {
      if (!roomFormRef.value) return
      
      await roomFormRef.value.validate(async (valid) => {
        if (valid) {
          loading.value = true
          try {
            let response
            if (isEdit.value) {
              response = await api.put('/room/update', roomForm)
            } else {
              response = await api.post('/room/create', roomForm)
            }
            
            if (response.code === 200) {
              ElMessage.success(isEdit.value ? response.data : '创建成功')
              dialogVisible.value = false
              loadRooms()
            } else {
              ElMessage.error(response.message)
            }
          } catch (error) {
            ElMessage.error(isEdit.value ? '更新失败' : '创建失败')
          } finally {
            loading.value = false
          }
        }
      })
    }
    
    const resetForm = () => {
      Object.assign(roomForm, {
        id: null,
        roomName: '',
        location: '',
        capacity: 10,
        equipment: '',
        status: 1
      })
    }
    
    onMounted(() => {
      loadRooms()
    })
    
    return {
      rooms,
      dialogVisible,
      loading,
      isEdit,
      roomFormRef,
      roomForm,
      rules,
      dialogTitle,
      showCreateDialog,
      editRoom,
      deleteRoom,
      handleSubmit
    }
  }
}
</script>

<style scoped>
.room-management h2 {
  margin-bottom: 20px;
  color: #333;
}

.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}
</style>
