package com.meeting.dto;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 预约详情DTO
 */
@Data
public class ReservationDetailDTO {
    
    private Long id;
    private Long roomId;
    private String roomName;
    private Long userId;
    private String userName;
    private String title;
    private String description;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Integer attendeeCount;
    private Integer status;
    private String auditRemark;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
