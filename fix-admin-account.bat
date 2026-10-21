@echo off
echo 数据库账号修复工具
echo ==================

echo.
echo 1. 检查数据库连接...
mysql -u root -p -e "USE 50906; SELECT 'Database connected successfully';"

if %errorlevel% neq 0 (
    echo 错误: 无法连接到数据库
    echo 请确保:
    echo 1. MySQL服务正在运行
    echo 2. 数据库 '50906' 已创建
    echo 3. 用户名密码正确
    pause
    exit /b 1
)

echo.
echo 2. 检查用户表是否存在...
mysql -u root -p -e "USE 50906; DESCRIBE sys_user;"

if %errorlevel% neq 0 (
    echo 错误: 用户表不存在
    echo 请先执行数据库初始化脚本: database/init.sql
    pause
    exit /b 1
)

echo.
echo 3. 检查现有管理员账号...
mysql -u root -p -e "USE 50906; SELECT id, username, real_name, user_type, status FROM sys_user WHERE username = 'admin';"

echo.
echo 4. 删除旧的管理员账号（如果存在）...
mysql -u root -p -e "USE 50906; DELETE FROM sys_user WHERE username = 'admin';"

echo.
echo 5. 创建新的管理员账号...
mysql -u root -p -e "USE 50906; INSERT INTO sys_user (username, password, real_name, email, phone, user_type, status, create_time, update_time, deleted) VALUES ('admin', '\$2a\$10\$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi', '系统管理员', 'admin@example.com', '13800138000', 1, 1, NOW(), NOW(), 0);"

echo.
echo 6. 验证新账号...
mysql -u root -p -e "USE 50906; SELECT id, username, real_name, user_type, status FROM sys_user WHERE username = 'admin';"

echo.
echo 修复完成！
echo 现在可以使用以下账号登录:
echo 用户名: admin
echo 密码: 123456
echo.
pause
