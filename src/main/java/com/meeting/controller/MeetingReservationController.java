package com.meeting.controller;

import com.meeting.common.Result;
import com.meeting.dto.ReservationDetailDTO;
import com.meeting.dto.RoomUseAgeDTO;
import com.meeting.entity.MeetingReservation;
import com.meeting.service.MeetingReservationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * 会议预约控制器
 */
@Slf4j
@RestController
@RequestMapping("/api/reservation")
@CrossOrigin(origins = "*")
public class MeetingReservationController {

    @Autowired
    private MeetingReservationService meetingReservationService;

    /**
     * 创建会议预约
     */
    @PostMapping("/create")
    public Result<String> createReservation(@Valid @RequestBody MeetingReservation reservation) {
        try {
            reservation.setStartTime(reservation.getStartTime().plusHours(8));
            reservation.setEndTime(reservation.getEndTime().plusHours(8));
            meetingReservationService.createReservation(reservation);
            return Result.success("预约创建成功");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 获取用户的预约列表
     */
    @GetMapping("/my")
    public Result<List<ReservationDetailDTO>> getMyReservations(@RequestParam Long userId) {
        try {
            List<ReservationDetailDTO> reservations = meetingReservationService.getUserReservationDetails(userId);
            return Result.success(reservations);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 获取所有预约列表（管理员）
     */
    @GetMapping("/all")
    public Result<List<ReservationDetailDTO>> getAllReservations() {
        try {
            List<ReservationDetailDTO> reservations = meetingReservationService.getAllReservationDetails();
            return Result.success(reservations);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 审核预约（管理员）
     */
    @PostMapping("/audit")
    public Result<String> auditReservation(@RequestBody Map<String, Object> auditData) {
        try {
            Long id = Long.valueOf(auditData.get("id").toString());
            Integer status = Integer.valueOf(auditData.get("status").toString());
            String remark = auditData.get("remark").toString();
            
            meetingReservationService.auditReservation(id, status, remark);
            return Result.success("审核完成");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 取消预约
     */
    @PostMapping("/cancel")
    public Result<String> cancelReservation(@RequestBody Map<String, Object> cancelData) {
        try {
            Long id = Long.valueOf(cancelData.get("id").toString());
            Long userId = Long.valueOf(cancelData.get("userId").toString());
            
            meetingReservationService.cancelReservation(id, userId);
            return Result.success("取消成功");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    /**
     * 删除预约
     */
    @PostMapping("/delete")
    public Result<String> deleteReservation(@RequestBody Map<String, Object> cancelData) {
        try {
            Long id = Long.valueOf(cancelData.get("id").toString());
            Long userId = Long.valueOf(cancelData.get("userId").toString());
            Integer status = Integer.valueOf(cancelData.get("status").toString());
            String message = meetingReservationService.deleteReservation(id, userId,status);
            return Result.success(message,null);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    /**
     * 延时预约
     */
    @PostMapping("/delay")
    public Result<String> delayReservation(@RequestBody Map<String, Object> delayData) {
        try {
            Long id = Long.valueOf(delayData.get("id").toString());
            Long roomId = Long.valueOf(delayData.get("roomId").toString());
            Long userId = Long.valueOf(delayData.get("userId").toString());
            Integer delayMinutes = Integer.valueOf(delayData.get("delayMinutes").toString());
            Integer status = Integer.valueOf(delayData.get("status").toString());
            String message = meetingReservationService.delayReservation(id,roomId,userId,delayMinutes,status);
            return Result.success(message,null);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 查询会议室使用情况(管理员)
     */
    @PostMapping("/usage")
    public Result<List<RoomUseAgeDTO>> selectRoomUsePercent(){
        try {
            List<RoomUseAgeDTO> usageList = meetingReservationService.selectRoomUsage();
            return Result.success(usageList);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}
