package com.meeting.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.meeting.entity.User;
import com.meeting.mapper.UserMapper;
import com.meeting.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * 用户服务类
 */
@Service
public class UserService extends ServiceImpl<UserMapper, User> {

    @Autowired
    private JwtUtil jwtUtil;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    /**
     * 用户登录
     */
    public Map<String, Object> login(String username, String password) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        queryWrapper.eq("status", 1);
        queryWrapper.eq("deleted", 0);
        
        User user = this.getOne(queryWrapper);
        if (user == null) {
            throw new RuntimeException("用户名或密码错误");
        }
        
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("用户名或密码错误");
        }
        
        String token = jwtUtil.generateToken(user.getUsername(), user.getId(), user.getUserType());
        
        Map<String, Object> result = new HashMap<>();
        result.put("token", token);
        result.put("user", user);
        return result;
    }

    /**
     * 用户注册
     */
    public void register(User user) {
        // 检查用户名是否已存在
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", user.getUsername());
        queryWrapper.eq("deleted", 0);
        
        if (this.count(queryWrapper) > 0) {
            throw new RuntimeException("用户名已存在");
        }
        
        // 加密密码
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setUserType(0); // 普通用户
        user.setStatus(1); // 启用状态
        
        this.save(user);
    }

    /**
     * 根据用户名获取用户
     */
    public User getUserByUsername(String username) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        queryWrapper.eq("deleted", 0);
        return this.getOne(queryWrapper);
    }

    /**
     * 初始化管理员账号
     */
    public void initAdminAccount() {
        // 检查是否已存在管理员账号
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", "admin");
        queryWrapper.eq("deleted", 0);
        
        User existingAdmin = this.getOne(queryWrapper);
        if (existingAdmin != null) {
            System.out.println("管理员账号已存在，跳过创建");
            return;
        }
        
        // 创建管理员账号
        User admin = new User();
        admin.setUsername("admin");
        admin.setPassword(passwordEncoder.encode("123456"));
        admin.setRealName("系统管理员");
        admin.setEmail("admin@example.com");
        admin.setPhone("13800138000");
        admin.setUserType(1); // 管理员
        admin.setStatus(1); // 启用
        
        this.save(admin);
        System.out.println("管理员账号创建成功: admin/123456");
    }

    /**
     * 重置管理员密码为123456
     */
    public void resetAdminPassword() {
        // 查找管理员账号
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", "admin");
        queryWrapper.eq("deleted", 0);
        
        User admin = this.getOne(queryWrapper);
        if (admin == null) {
            // 如果管理员账号不存在，创建一个
            System.out.println("管理员账号不存在，创建新账号");
            initAdminAccount();
            return;
        }
        
        // 重置密码为123456
        admin.setPassword(passwordEncoder.encode("123456"));
        this.updateById(admin);
        System.out.println("管理员密码重置成功: admin/123456");
    }
}
