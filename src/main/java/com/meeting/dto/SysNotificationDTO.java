package com.meeting.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class SysNotificationDTO {
    private Long id;
    private String title;
    private String content;
    private String link;
    private Integer isRead;
    private LocalDateTime createdAt;
}
