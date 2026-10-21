package com.meeting.controller;

import com.meeting.common.Result;
import com.meeting.dto.SysNotificationDTO;
import com.meeting.entity.SysNotification;
import com.meeting.service.SysNotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/notification")
public class SysNotificationController {

    @Autowired
    private SysNotificationService sysNotificationService;

    /**
     * 获取当前用户的通知列表
     */
    @GetMapping("/sysNotification-list")
    public Result<List<SysNotificationDTO>> getNotifications(@RequestParam Long userId) {
        List<SysNotificationDTO> list = sysNotificationService.getUserNotifications(userId);
        return Result.success(list);
    }

    /**
     * 获取未读通知数量
     */
    @GetMapping("/unread-count")
    public Result<Long> getUnreadCount(@RequestParam Long userId) {
        Long count = sysNotificationService.getUnreadCount(userId);
        return Result.success(count);
    }

    /**
     * 标记为已读
     */
    @PostMapping("/read")
    public Result<String> markAsRead(@RequestBody Map<String, Long> params) {
        Long id = params.get("id");
        Long userId = params.get("userId");
        sysNotificationService.markAsRead(id, userId);
        return Result.success("已标记为已读");
    }

    /**
     * 全部标记为已读
     */
    @GetMapping("/read-all")
    public Result<String> markAllAsRead(@RequestParam Long userId) {
        sysNotificationService.markAllAsRead(userId);
        return Result.success("全部已读");
    }


    @DeleteMapping("/delete/{id}")
    public Result<String> deleteNotification(@PathVariable Long id) {
        // 逻辑删除或物理删除
        sysNotificationService.removeById(id);  // 或者 logic delete
        return Result.success("删除成功");
    }
}
