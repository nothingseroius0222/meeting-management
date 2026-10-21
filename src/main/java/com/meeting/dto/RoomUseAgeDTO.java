package com.meeting.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RoomUseAgeDTO {
    private Long id;
    private String roomName;
    private Double usagePercentage;
}
