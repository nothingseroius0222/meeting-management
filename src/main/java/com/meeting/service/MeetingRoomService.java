package com.meeting.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.meeting.entity.MeetingReservation;
import com.meeting.entity.MeetingRoom;
import com.meeting.mapper.MeetingRoomMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 会议室服务类
 */
@Service
public class MeetingRoomService extends ServiceImpl<MeetingRoomMapper, MeetingRoom> {

    /**
     * 获取所有没被删除的会议室
     */
    public List<MeetingRoom> getUndeletedRooms() {
        QueryWrapper<MeetingRoom> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("deleted", 0);
        queryWrapper.orderByAsc("id");
        return this.list(queryWrapper);
    }

    /**
     * 获取所有可用的会议室
     */
    public List<MeetingRoom> getAvailableRooms() {
        QueryWrapper<MeetingRoom> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status", 1);
        queryWrapper.eq("deleted", 0);
        queryWrapper.orderByAsc("id");
        return this.list(queryWrapper);
    }

    /**
     * 根据ID获取会议室
     */
    public MeetingRoom getRoomById(Long id) {
        QueryWrapper<MeetingRoom> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", id);
        queryWrapper.eq("deleted", 0);
        return this.getOne(queryWrapper);
    }

}
