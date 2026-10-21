package com.meeting.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.meeting.dto.SysNotificationDTO;
import com.meeting.entity.SysNotification;
import com.meeting.mapper.SysNotificationMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SysNotificationService extends ServiceImpl<SysNotificationMapper, SysNotification> {
    /**
     * 发送通知给单个用户
     *
     * @param sysNotification
     */
    public void sendToUser(SysNotification sysNotification) {
        sysNotification.setCreatedAt(LocalDateTime.now(ZoneId.of("Asia/Shanghai")));
        this.save(sysNotification);
    }

    /**
     * 获取用户未读通知数
     */
    public Long getUnreadCount(Long userId) {
        QueryWrapper<SysNotification> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId);
        wrapper.eq("is_read", 0);
        wrapper.eq("deleted", 0);
        return this.count(wrapper);
    }

    /**
     * 获取用户所有通知
     */
    public List<SysNotificationDTO> getUserNotifications(Long userId) {
        QueryWrapper<SysNotification> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId);
        wrapper.eq("deleted", 0);
        wrapper.orderByDesc("created_at");
        return this.list(wrapper).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * 标记单个为已读
     */
    public void markAsRead(Long notificationId, Long userId) {
        SysNotification n = this.getById(notificationId);
        if (n != null && n.getUserId().equals(userId)) {
            n.setIsRead(1);
            n.setReadAt(LocalDateTime.now(ZoneId.of("Asia/Shanghai")));
            this.updateById(n);
        }
    }

    /**
     * 全部标记为已读
     */
    public void markAllAsRead(Long userId) {
        QueryWrapper<SysNotification> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId);
        wrapper.eq("is_read", 0);
        wrapper.eq("deleted", 0);
        List<SysNotification> list = this.list(wrapper);
        list.forEach(n -> {
            n.setIsRead(1);
            n.setReadAt(LocalDateTime.now(ZoneId.of("Asia/Shanghai")));
        });
        this.updateBatchById(list);
    }

    /**
     * 转换为DTO
     */
    private SysNotificationDTO convertToDTO(SysNotification n) {
        SysNotificationDTO dto = new SysNotificationDTO();
        dto.setId(n.getId());
        dto.setTitle(n.getTitle());
        dto.setContent(n.getContent());
        dto.setLink(n.getLink());
        dto.setIsRead(n.getIsRead());
        dto.setCreatedAt(n.getCreatedAt());
        return dto;
    }


}