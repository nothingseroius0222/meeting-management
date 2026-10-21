package com.meeting.task;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.meeting.entity.MeetingReservation;
import com.meeting.service.MeetingReservationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@Slf4j
@Component
public class MeetingScheduledTask {
    @Autowired
    private MeetingReservationService reservationService;

    @Scheduled(cron = "0 */5 * * * ?")  // 每5分钟执行一次
    public void autoCompleteMeeting() {
        // 查询所有“已通过”且“结束时间 < 当前时间”的预约
        QueryWrapper<MeetingReservation> wrapper = new QueryWrapper<>();
        wrapper.eq("status", 1);  // 已通过
        wrapper.lt("end_time", LocalDateTime.now(ZoneId.of("Asia/Shanghai")));
        wrapper.eq("deleted", 0);

        List<MeetingReservation> list = reservationService.list(wrapper);

        for (MeetingReservation r : list) {
            r.setStatus(4);  // 4 = 已完成
            reservationService.updateById(r);
        }
        log.info("自动完成 {} 个会议", list.size());
    }
}
