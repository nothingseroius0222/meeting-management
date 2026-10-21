package com.meeting.controller;

import com.meeting.common.Result;
import com.meeting.entity.User;
import com.meeting.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Map;
import java.util.List;

/**
 * 用户控制器
 */
@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 用户登录
     */
    @PostMapping("/login")
    public Result<Map<String, Object>> login(@RequestBody Map<String, String> loginData) {
        try {
            String username = loginData.get("username");
            String password = loginData.get("password");
            
            Map<String, Object> result = userService.login(username, password);
            return Result.success("登录成功", result);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 用户注册
     */
    @PostMapping("/register")
    public Result<String> register(@Valid @RequestBody User user) {
        try {
            userService.register(user);
            return Result.success("注册成功");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 获取用户信息
     */
    @GetMapping("/info")
    public Result<User> getUserInfo(@RequestHeader("Authorization") String token) {
        try {
            // 这里应该从token中解析用户信息，简化处理
            String username = token.replace("Bearer ", "");
            User user = userService.getUserByUsername(username);
            if (user != null) {
                user.setPassword(null); // 不返回密码
            }
            return Result.success(user);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 初始化管理员账号（仅用于开发环境）
     */
    @PostMapping("/init-admin")
    public Result<String> initAdmin() {
        try {
            userService.initAdminAccount();
            return Result.success("管理员账号初始化成功");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 重置管理员密码为123456（仅用于开发环境）
     */
    @PostMapping("/reset-admin-password")
    public Result<String> resetAdminPassword() {
        try {
            userService.resetAdminPassword();
            return Result.success("管理员密码重置成功，新密码：123456");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 获取用户列表（管理员）
     */
    @GetMapping("/list")
    public Result<List<User>> getUserList() {
        try {
            List<User> users = userService.list();
            // 不返回密码
            users.forEach(user -> user.setPassword(null));
            return Result.success(users);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 更新用户状态（管理员）
     */
    @PutMapping("/update-status")
    public Result<String> updateUserStatus(@RequestBody Map<String, Object> statusData) {
        try {
            Long userId = Long.valueOf(statusData.get("id").toString());
            Integer status = Integer.valueOf(statusData.get("status").toString());
            
            User user = userService.getById(userId);
            if (user == null) {
                return Result.error("用户不存在");
            }
            
            user.setStatus(status);
            userService.updateById(user);
            
            String action = status == 1 ? "启用" : "禁用";
            return Result.success("用户" + action + "成功");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}