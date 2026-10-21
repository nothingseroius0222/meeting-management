package com.meeting.controller;

import com.meeting.common.Result;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * 健康检查控制器
 */
@RestController
@RequestMapping("/api")
public class HealthController {

    /**
     * 健康检查接口
     */
    @GetMapping("/health")
    public Result<Map<String, Object>> health() {
        Map<String, Object> data = new HashMap<>();
        data.put("status", "UP");
        data.put("timestamp", LocalDateTime.now());
        data.put("service", "meeting-management");
        return Result.success("服务正常", data);
    }

    /**
     * 系统信息接口
     */
    @GetMapping("/info")
    public Result<Map<String, Object>> info() {
        Map<String, Object> data = new HashMap<>();
        data.put("name", "会议预约管理系统");
        data.put("version", "1.0.0");
        data.put("description", "基于Spring Boot + Vue3的会议预约管理系统");
        data.put("timestamp", LocalDateTime.now());
        return Result.success(data);
    }
}
