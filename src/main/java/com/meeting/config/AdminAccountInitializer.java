package com.meeting.config;

import com.meeting.entity.User;
import com.meeting.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * 应用启动时自动初始化管理员账号
 */
@Component
public class AdminAccountInitializer implements CommandLineRunner {

    @Autowired
    private UserService userService;

    @Override
    public void run(String... args) throws Exception {
        try {
            // 检查管理员账号是否存在
            User admin = userService.getUserByUsername("admin");
            
            if (admin == null) {
                // 如果管理员账号不存在，创建一个
                System.out.println("=== 初始化管理员账号 ===");
                userService.initAdminAccount();
                System.out.println("管理员账号创建成功！");
                System.out.println("用户名: admin");
                System.out.println("密码: 123456");
                System.out.println("========================");
            } else {
                // 如果管理员账号存在，重置密码为123456
                System.out.println("=== 重置管理员密码 ===");
                userService.resetAdminPassword();
                System.out.println("管理员密码重置成功！");
                System.out.println("用户名: admin");
                System.out.println("密码: 123456");
                System.out.println("======================");
            }
        } catch (Exception e) {
            System.err.println("初始化管理员账号失败: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
