package com.meeting.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * 密码验证工具类
 */
public class PasswordValidator {
    
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        
        // 测试密码123456
        String plainPassword = "123456";
        String encodedPassword = encoder.encode(plainPassword);
        
        System.out.println("=== 密码验证测试 ===");
        System.out.println("原始密码: " + plainPassword);
        System.out.println("加密后密码: " + encodedPassword);
        
        // 验证密码
        boolean matches = encoder.matches(plainPassword, encodedPassword);
        System.out.println("密码验证结果: " + matches);
        
        // 测试数据库中存储的密码
        String dbPassword = "$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi";
        boolean dbMatches = encoder.matches(plainPassword, dbPassword);
        System.out.println("数据库密码验证结果: " + dbMatches);
        
        System.out.println("==================");
    }
}
