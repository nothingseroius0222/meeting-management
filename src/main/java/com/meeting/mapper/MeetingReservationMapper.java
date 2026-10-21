package com.meeting.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.meeting.entity.MeetingReservation;
import org.apache.ibatis.annotations.Mapper;

/**
 * 会议预约Mapper接口
 */
@Mapper
public interface MeetingReservationMapper extends BaseMapper<MeetingReservation> {
}
