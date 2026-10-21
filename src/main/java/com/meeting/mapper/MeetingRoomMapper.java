package com.meeting.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.meeting.entity.MeetingRoom;
import org.apache.ibatis.annotations.Mapper;

/**
 * 会议室Mapper接口
 */
@Mapper
public interface MeetingRoomMapper extends BaseMapper<MeetingRoom> {
}
