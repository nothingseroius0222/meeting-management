package com.meeting.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 会议室实体类
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("meeting_room")
public class MeetingRoom {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 会议室名称
     */
    @TableField("room_name")
    private String roomName;

    /**
     * 会议室位置
     */
    @TableField("location")
    private String location;

    /**
     * 容量
     */
    @TableField("capacity")
    private Integer capacity;

    /**
     * 设备描述
     */
    @TableField("equipment")
    private String equipment;

    /**
     * 状态：0-禁用，1-启用
     */
    @TableField("status")
    private Integer status;

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
