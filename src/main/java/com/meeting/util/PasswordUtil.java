package com.meeting.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * 密码工具类
 */
public class PasswordUtil {
    
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        
        // 生成123456的BCrypt加密密码
        String password = "123456";
        String encodedPassword = encoder.encode(password);
        
        System.out.println("原始密码: " + password);
        System.out.println("加密后密码: " + encodedPassword);
        
        // 验证密码
        boolean matches = encoder.matches(password, encodedPassword);
        System.out.println("密码验证结果: " + matches);
        
        // 生成SQL插入语句
        System.out.println("\nSQL插入语句:");
        System.out.println("INSERT INTO sys_user (username, password, real_name, user_type, status) VALUES ('admin', '" + encodedPassword + "', '系统管理员', 1, 1);");
    }
}
