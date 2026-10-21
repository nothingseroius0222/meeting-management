package com.meeting.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.meeting.dto.ReservationDetailDTO;
import com.meeting.dto.RoomUseAgeDTO;
import com.meeting.entity.MeetingReservation;
import com.meeting.entity.MeetingRoom;
import com.meeting.entity.SysNotification;
import com.meeting.entity.User;
import com.meeting.mapper.MeetingReservationMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 会议预约服务类
 */
@Service
public class MeetingReservationService extends ServiceImpl<MeetingReservationMapper, MeetingReservation> {

    @Autowired
    private MeetingRoomService meetingRoomService;
    
    @Autowired
    private UserService userService;

    @Autowired
    private SysNotificationService sysNotificationService;

    /**
     * 创建会议预约
     */
    public void createReservation(MeetingReservation reservation) {
        //检查开始时间是否早于系统时间
        LocalDateTime now = LocalDateTime.now(ZoneId.of("Asia/Shanghai"));
        if (reservation.getStartTime().isBefore(now)) {
            throw new RuntimeException("开始时间不能早于当前时间，请重新选择");
        }
        //检查开始时间是否早于结束时间
        if (reservation.getStartTime().isAfter(reservation.getEndTime())
                || reservation.getStartTime().equals(reservation.getEndTime())) {
            throw new IllegalArgumentException("结束时间必须晚于开始时间");
        }
        // 验证预约时间段

        // 检查会议室是否存在且可用
        MeetingRoom room = meetingRoomService.getRoomById(reservation.getRoomId());
        if (room == null || room.getStatus() != 1) {
            throw new RuntimeException("会议室不存在或不可用");
        }
        
        // 检查时间冲突
        if (hasTimeConflict(reservation)) {
            throw new RuntimeException("该时间段已被预约");
        }
        
        // 设置默认状态为待审核
        reservation.setStatus(0);
        
        this.save(reservation);
    }

    /**
     * 检查时间冲突
     */
    private boolean hasTimeConflict(MeetingReservation reservation) {
        QueryWrapper<MeetingReservation> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("room_id", reservation.getRoomId());
        queryWrapper.in("status", 0,1);
        queryWrapper.eq("deleted", 0);
        
        List<MeetingReservation> existingReservations = this.list(queryWrapper);
        
        for (MeetingReservation existing : existingReservations) {
            if (isTimeOverlap(reservation.getStartTime(), reservation.getEndTime(),
                    existing.getStartTime(), existing.getEndTime())) {
                return true;
            }
        }
        
        return false;
    }

    /**
     * 检查时间是否重叠
     */
    private boolean isTimeOverlap(LocalDateTime start1, LocalDateTime end1,
                                LocalDateTime start2, LocalDateTime end2) {
        if(start1.isBefore(start2)){
            return start1.isBefore(end2) && end1.isAfter(start2);
        }else{
            return start2.isBefore(end1) && end2.isAfter(start1);
        }
    }

    /**
     * 获取用户的预约列表
     */
    public List<MeetingReservation> getUserReservations(Long userId) {
        QueryWrapper<MeetingReservation> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        queryWrapper.eq("deleted", 0);
        queryWrapper.orderByDesc("create_time");
        return this.list(queryWrapper);
    }

    /**
     * 获取用户的预约详情列表（包含会议室信息）
     */
    public List<ReservationDetailDTO> getUserReservationDetails(Long userId) {
        List<MeetingReservation> reservations = getUserReservations(userId);
        
        return reservations.stream().map(reservation -> {
            ReservationDetailDTO dto = new ReservationDetailDTO();
            
            // 复制基本信息
            dto.setId(reservation.getId());
            dto.setRoomId(reservation.getRoomId());
            dto.setUserId(reservation.getUserId());
            dto.setTitle(reservation.getTitle());
            dto.setDescription(reservation.getDescription());
            dto.setStartTime(reservation.getStartTime());
            dto.setEndTime(reservation.getEndTime());
            dto.setAttendeeCount(reservation.getAttendeeCount());
            dto.setStatus(reservation.getStatus());
            dto.setAuditRemark(reservation.getAuditRemark());
            dto.setCreateTime(reservation.getCreateTime());
            dto.setUpdateTime(reservation.getUpdateTime());
            
            // 获取会议室名称
            try {
                MeetingRoom room = meetingRoomService.getRoomById(reservation.getRoomId());
                dto.setRoomName(room != null ? room.getRoomName() : "未知会议室");
            } catch (Exception e) {
                dto.setRoomName("未知会议室");
            }
            
            // 用户名称设为当前用户
            dto.setUserName("我");
            
            return dto;
        }).collect(Collectors.toList());
    }

    /**
     * 获取所有预约列表（管理员）
     */
    public List<MeetingReservation> getAllReservations() {
        QueryWrapper<MeetingReservation> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("deleted", 0);
        queryWrapper.orderByDesc("create_time");
        return this.list(queryWrapper);
    }

    /**
     * 获取所有预约详情列表（包含会议室和用户信息）
     */
    public List<ReservationDetailDTO> getAllReservationDetails() {
        List<MeetingReservation> reservations = getAllReservations();
        
        return reservations.stream().map(reservation -> {
            ReservationDetailDTO dto = new ReservationDetailDTO();
            
            // 复制基本信息
            dto.setId(reservation.getId());
            dto.setRoomId(reservation.getRoomId());
            dto.setUserId(reservation.getUserId());
            dto.setTitle(reservation.getTitle());
            dto.setDescription(reservation.getDescription());
            dto.setStartTime(reservation.getStartTime());
            dto.setEndTime(reservation.getEndTime());
            dto.setAttendeeCount(reservation.getAttendeeCount());
            dto.setStatus(reservation.getStatus());
            dto.setAuditRemark(reservation.getAuditRemark());
            dto.setCreateTime(reservation.getCreateTime());
            dto.setUpdateTime(reservation.getUpdateTime());
            
            // 获取会议室名称
            try {
                MeetingRoom room = meetingRoomService.getRoomById(reservation.getRoomId());
                dto.setRoomName(room != null ? room.getRoomName() : "未知会议室");
            } catch (Exception e) {
                dto.setRoomName("未知会议室");
            }
            
            // 获取用户名称
            try {
                User user = userService.getById(reservation.getUserId());
                dto.setUserName(user != null ? user.getRealName() : "未知用户");
            } catch (Exception e) {
                dto.setUserName("未知用户");
            }
            
            return dto;
        }).collect(Collectors.toList());
    }

    /**
     * 审核预约
     */
    public void auditReservation(Long id, Integer status, String remark) {
        MeetingReservation reservation = this.getById(id);
        if (reservation == null) {
            throw new RuntimeException("预约不存在");
        }
        
        reservation.setStatus(status);
        reservation.setAuditRemark(remark);
        this.updateById(reservation);
    }

    /**
     * 取消预约
     */
    public void cancelReservation(Long id, Long userId) {
        MeetingReservation reservation = this.getById(id);
        if (reservation == null) {
            throw new RuntimeException("预约不存在");
        }
        
        if (!reservation.getUserId().equals(userId)) {
            throw new RuntimeException("无权限取消此预约");
        }
        
        reservation.setStatus(3); // 已取消
        this.updateById(reservation);
    }

    /**
     * 根据ID获取会议室使用百分比
     */
    public List<RoomUseAgeDTO> selectRoomUsage() {
        //1.查询所有的会议室
        List<MeetingRoom>  rooms = meetingRoomService.getAvailableRooms();

        QueryWrapper<MeetingReservation> queryWrapper1 = new QueryWrapper<>();
        queryWrapper1.in("status",1,4);
        queryWrapper1.eq("deleted",0);
        Integer total = this.list(queryWrapper1).size();
        // 2. 如果没有任何预约，所有会议室使用率都是 0
        if (total == 0) {
            return rooms.stream()
                    .map(room -> new RoomUseAgeDTO(room.getId(), room.getRoomName(),0.0))
                    .collect(Collectors.toList());
        }
        //3.查询个会议室使用次数
        QueryWrapper<MeetingReservation> queryWrapper2 = new QueryWrapper<>();
        queryWrapper2.in("status",1,4);
        queryWrapper2.eq("deleted",0);
        queryWrapper2.select("room_id, COUNT(*) as count");
        queryWrapper2.groupBy("room_id");

        //把查询结果转为List<map>
        List<Map<String, Object>> groupResult  = this.baseMapper.selectMaps(queryWrapper2);
        //再转为map
        Map<Long, Long> roomCountMap = groupResult .stream()
                .collect(Collectors.toMap(
                        m -> (Long)m.get("room_id"),
                        m -> (Long)m.get("count")));
        //组装结果：遍历所有会议室，计算使用率
        return rooms.stream()
                .map(room -> {
                    Long count = roomCountMap.getOrDefault(room.getId(), 0L);
                    // 计算百分比，保留两位小数
                        Double percent = count.doubleValue() / total.doubleValue() * 100;
                        double rounded = Math.round(percent * 100) / 100.0;
                        return new RoomUseAgeDTO(room.getId(), room.getRoomName(), rounded);
                })
                .collect(Collectors.toList());
    }

    /**
     * 删除会议室(管理员)
     */
    public String deleteRoomById(Long id) {
        // 1. 获取会议室信息（用于通知内容）
        MeetingRoom room = meetingRoomService.getById(id);
        if (room == null) {
            return "会议室不存在";
        }
        //2.查询是否有仍在生效的预约记录
        QueryWrapper<MeetingReservation> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("status",0,1);
        queryWrapper.eq("deleted",0);
        queryWrapper.eq("room_id",id);
        List<MeetingReservation> affectedList = this.list(queryWrapper);
        if(affectedList.isEmpty()){
            meetingRoomService.removeById(id);
            return ("删除成功");
        }
        //3.强制取消已经预约的记录
        UpdateWrapper<MeetingReservation> updateWrapper = new UpdateWrapper<>();
        updateWrapper.in("status",0,1)
                     .eq("deleted",0)
                     .eq("room_id",id);
        MeetingReservation updateEntity = new MeetingReservation();
        updateEntity.setStatus(2);
        updateEntity.setAuditRemark("会议室已停用，预约被自动取消");
        this.update(updateEntity, updateWrapper);

        //4.发送通知给所有受影响的用户
        //4.1:统计用户id
        List<Long> userIds = affectedList.stream()
                .map(MeetingReservation::getUserId)
                .distinct()
                .collect(Collectors.toList());
        //4.2:统计每个用户有几条预约记录
        Map<Long, Long> userReservationCount = affectedList.stream()
                .collect(Collectors.groupingBy(
                        MeetingReservation::getUserId,
                        Collectors.counting()
                ));
        //4.3:构造通知内容并发送
        for (Long userId : userIds) {
            Long count = userReservationCount.getOrDefault(userId, 0L);
            //获取用户姓名（可先，用于更友好的通知）
            User user = userService.getById(userId);
            String userName = user.getUsername() != null ? user.getUsername() : "用户";
            String title = "【重要通知】会议室已停用";
            String content = String.format(
                    "%s，您好！\n会议室《%s》已停用，您有 %d 个预约在该会议室，已被自动取消。\n请重新预约其他会议室。",
                    userName,
                    room.getRoomName(),
                    count
            );
            SysNotification sysNotification = new SysNotification();
            sysNotification.setUserId(userId);
            sysNotification.setType(0);
            sysNotification.setTitle(title);
            sysNotification.setContent(content);
            sysNotification.setIsRead(0);
            //设置关联的会议室id
            sysNotification.setTargetId(room.getId());
            //发送通知(插入通知表)
            sysNotificationService.sendToUser(sysNotification);
        }
        //5.最后逻辑删除会议室
        meetingRoomService.removeById(id);
        return "删除成功，已取消 " + affectedList.size() + " 个预约，并通知 " + userIds.size() + " 位用户";
    }

    /**
     * 更新会议室(管理员)
     */
    public String updateStatusById(MeetingRoom room) {
        if(room.getStatus() != null && room.getStatus() == 1){
            meetingRoomService.updateById(room);
            return "更新成功";
        }else {
            QueryWrapper<MeetingReservation> queryWrapper = new QueryWrapper<>();
            queryWrapper.in("status",0,1);
            queryWrapper.eq("deleted",0);
            queryWrapper.eq("room_id",room.getId());
            List<MeetingReservation> affectedList = this.list(queryWrapper);
            if(affectedList.isEmpty()){
                meetingRoomService.updateById(room);
                return "更新成功";
            }
            //3.强制取消已经预约的记录
            UpdateWrapper<MeetingReservation> updateWrapper = new UpdateWrapper<>();
            updateWrapper.in("status",0,1)
                    .eq("deleted",0)
                    .eq("room_id",room.getId());
            MeetingReservation updateEntity = new MeetingReservation();
            updateEntity.setStatus(2);
            updateEntity.setAuditRemark("会议室已停用，预约被自动取消");
            this.update(updateEntity, updateWrapper);
            //4.发送通知给所有受影响的用户
            //4.1:统计用户id
            List<Long> userIds = affectedList.stream()
                    .map(MeetingReservation::getUserId)
                    .distinct()
                    .collect(Collectors.toList());
            //4.2:统计每个用户有几条预约记录
            Map<Long, Long> userReservationCount = affectedList.stream()
                    .collect(Collectors.groupingBy(
                            MeetingReservation::getUserId,
                            Collectors.counting()
                    ));
            //4.3:构造通知内容并发送
            for (Long userId : userIds) {
                Long count = userReservationCount.getOrDefault(userId, 0L);
                //获取用户姓名（可先，用于更友好的通知）
                User user = userService.getById(userId);
                String userName = user.getUsername() != null ? user.getUsername() : "用户";
                String title = "【重要通知】会议室已停用";
                String content = String.format(
                        "%s，您好！\n会议室《%s》已停用，您有 %d 个预约在该会议室，已被自动取消。\n请重新预约其他会议室。",
                        userName,
                        room.getRoomName(),
                        count
                );
                SysNotification sysNotification = new SysNotification();
                sysNotification.setUserId(userId);
                sysNotification.setType(0);
                sysNotification.setTitle(title);
                sysNotification.setContent(content);
                sysNotification.setIsRead(0);
                //设置关联的会议室id
                sysNotification.setTargetId(room.getId());
                //发送通知(插入通知表)
                sysNotificationService.sendToUser(sysNotification);
            }
            //5.最后逻辑删除会议室
            meetingRoomService.updateById(room);
            return "更新成功，已取消 " + affectedList.size() + " 个预约，并通知 " + userIds.size() + " 位用户";
        }
    }

    /**
     * 删除预约
     */
    public String deleteReservation(Long id, Long userId, Integer status) {
        MeetingReservation reservation = this.getById(id);
        if (reservation == null) {
            return "预约不存在,删除失败";
        }
        if (!reservation.getUserId().equals(userId)) {
            return "无权限删除此预约";
        }
        if(status != null && status != 2){
            reservation.setStatus(3);// 已取消
        }else {
            reservation.setStatus(2);// 已拒绝
        }
        this.updateById(reservation);//先更新状态
        this.removeById(reservation);//再删除
        return "删除成功";
    }

    /*
    * 延时预约
    */
    public String delayReservation(Long id, Long roomId,Long userId, Integer delayMinutes,Integer status) {
        MeetingReservation reservation = this.getById(id);
        if (reservation == null) {
            return "预约不存在,延时失败";
        }
        if (!reservation.getUserId().equals(userId)) {
            return "无权限取延时预约";
        }
        if (null != status && (status == 0 || status == 1)){
            //计算新的结束时间
            LocalDateTime newEndTime = reservation.getEndTime().plusMinutes(delayMinutes);

            QueryWrapper<MeetingReservation> queryWrapper = new QueryWrapper<>();
            queryWrapper.in("status",0,1)
                        .ne("id",id)
                        .eq("room_id",roomId)
                        .eq("deleted",0)
                        .lt("start_time",newEndTime)
                        .gt("end_time", reservation.getEndTime());;
            if(this.count(queryWrapper) > 0){
                return "该时间段已有预约，无法延时";
            }
            //更新结束时间
            reservation.setEndTime(newEndTime);
            this.updateById(reservation);
            return "会议已成功延长" + delayMinutes + "分钟";
        }
        return "延时失败";
    }
}
