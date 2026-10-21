package com.meeting.controller;

import com.meeting.common.Result;
import com.meeting.entity.MeetingRoom;
import com.meeting.service.MeetingReservationService;
import com.meeting.service.MeetingRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 会议室控制器
 */
@RestController
@RequestMapping("/api/room")
@CrossOrigin(origins = "*")
public class MeetingRoomController {

    @Autowired
    private MeetingRoomService meetingRoomService;
    @Autowired
    private MeetingReservationService meetingReservationService;

    /**
     * 获取所有可用会议室
     */
    @GetMapping("/undeletedlist")
    public Result<List<MeetingRoom>> getUndeletedRooms() {
        try {
            List<MeetingRoom> rooms = meetingRoomService.getUndeletedRooms();
            return Result.success(rooms);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 获取所有可用会议室
     */
    @GetMapping("/list")
    public Result<List<MeetingRoom>> getAvailableRooms() {
        try {
            List<MeetingRoom> rooms = meetingRoomService.getAvailableRooms();
            return Result.success(rooms);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 根据ID获取会议室详情
     */
    @GetMapping("/{id}")
    public Result<MeetingRoom> getRoomById(@PathVariable Long id) {
        try {
            MeetingRoom room = meetingRoomService.getRoomById(id);
            return Result.success(room);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 创建会议室（管理员）
     */
    @PostMapping("/create")
    public Result<String> createRoom(@RequestBody MeetingRoom room) {
        try {
            meetingRoomService.save(room);
            return Result.success("创建成功");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 更新会议室（管理员）
     */
    @PutMapping("/update")
    public Result<String> updateRoom(@RequestBody MeetingRoom room) {
        try {
            String message = meetingReservationService.updateStatusById(room);
            return Result.success(message);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 删除会议室（管理员）
     */
    @DeleteMapping("/{id}")
    public Result<String> deleteRoom(@PathVariable Long id) {
        try {
            String message = meetingReservationService.deleteRoomById(id);
            return Result.success(message);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}
