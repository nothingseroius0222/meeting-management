@echo off
echo 管理员密码修复工具
echo ==================

echo.
echo 正在修复管理员密码为123456...

echo.
echo 方法1: 使用API接口修复
curl -X POST http://localhost:8080/api/user/reset-admin-password

echo.
echo 方法2: 手动SQL修复
echo 请执行以下SQL命令:
echo.
echo USE `50906`;
echo DELETE FROM sys_user WHERE username = 'admin';
echo INSERT INTO sys_user (username, password, real_name, email, phone, user_type, status, create_time, update_time, deleted) VALUES ('admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi', '系统管理员', 'admin@example.com', '13800138000', 1, 1, NOW(), NOW(), 0);
echo.

echo.
echo 修复完成后，请使用以下账号登录:
echo 用户名: admin
echo 密码: 123456
echo.
echo 访问地址:
echo 用户前端: http://localhost:3000
echo 管理后台: http://localhost:3001
echo 管理员初始化页面: http://localhost:3000/admin-init
echo.
pause
