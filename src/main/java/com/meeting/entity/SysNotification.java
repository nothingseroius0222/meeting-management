package com.meeting.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("sys_notification")
public class SysNotification {
        @TableId(type = IdType.AUTO)
        private Long id;

        /**
         * 接收人id
         */
        @TableField("user_id")
        private Long userId;

        /**
         * 通知类型
         */
        @TableField("type")
        private Integer type;

        /**
         * 通知标题
         */
        @TableField("title")
        private String title;

        /**
         * 通知内容
         */
        @TableField("content")
        private String content;

        /**
         * 前端跳转路径
         */
        @TableField("link")
        private String link;

        /**
         * 是否已读,0=未读 1=已读
         */
        @TableField("is_Read")
        private Integer isRead;

        /**
         * 关联业务ID（如预约ID、会议室ID）
         */
        @TableField("target_Id")
        private Long targetId;

        /**
         * 创建时间
         */
        @TableField(value = "created_at")
        private LocalDateTime createdAt;

        /**
         * 阅读时间
         */
        @TableField(value = "read_at")
        private LocalDateTime readAt;

        /**
         * 逻辑删除
         */
        @TableField("deleted")
        @TableLogic
        private Integer deleted;
}
