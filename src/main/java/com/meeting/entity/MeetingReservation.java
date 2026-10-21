package com.meeting.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 会议预约实体类
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("meeting_reservation")
public class MeetingReservation {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 会议室ID
     */
    @TableField("room_id")
    private Long roomId;

    /**
     * 预约用户ID
     */
    @TableField("user_id")
    private Long userId;

    /**
     * 会议主题
     */
    @TableField("title")
    private String title;

    /**
     * 会议描述
     */
    @TableField("description")
    private String description;

    /**
     * 开始时间
     */
    @TableField("start_time")
    private LocalDateTime startTime;

    /**
     * 结束时间
     */
    @TableField("end_time")
    private LocalDateTime endTime;

    /**
     * 参会人数
     */
    @TableField("attendee_count")
    private Integer attendeeCount;

    /**
     * 状态：0-待审核，1-已通过，2-已拒绝，3-已取消，4-已完成
     */
    @TableField("status")
    private Integer status;

    /**
     * 审核备注
     */
    @TableField("audit_remark")
    private String auditRemark;

    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /**
     * 逻辑删除：0-未删除，1-已删除
     */
    @TableLogic
    @TableField("deleted")
    private Integer deleted;
}
