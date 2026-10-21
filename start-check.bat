@echo off
echo 会议预约管理系统 - 启动检查
echo ================================

echo.
echo 检查Java环境...
java -version
if %errorlevel% neq 0 (
    echo 错误: 未找到Java环境，请安装Java 11或以上版本
    pause
    exit /b 1
)

echo.
echo 检查Node.js环境...
node --version
if %errorlevel% neq 0 (
    echo 错误: 未找到Node.js环境，请安装Node.js 16或以上版本
    pause
    exit /b 1
)

echo.
echo 检查MySQL连接...
mysql --version
if %errorlevel% neq 0 (
    echo 警告: 未找到MySQL客户端，请确保MySQL服务正在运行
)

echo.
echo 检查项目文件...
if not exist "pom.xml" (
    echo 错误: 未找到pom.xml文件
    pause
    exit /b 1
)

if not exist "frontend-user\package.json" (
    echo 错误: 未找到用户前端项目文件
    pause
    exit /b 1
)

if not exist "frontend-admin\package.json" (
    echo 错误: 未找到管理后台项目文件
    pause
    exit /b 1
)

echo.
echo 所有检查通过！
echo.
echo 请确保：
echo 1. MySQL服务正在运行
echo 2. 数据库 '50906' 已创建
echo 3. 数据库用户名密码正确 (root/123456)
echo.
echo 按任意键开始启动服务...
pause

echo.
echo 启动后端服务...
start "后端服务" cmd /k "mvn spring-boot:run"

echo.
echo 等待后端服务启动...
timeout /t 15 /nobreak > nul

echo.
echo 启动用户前端...
start "用户前端" cmd /k "cd frontend-user && npm install && npm run dev"

echo.
echo 启动管理后台...
start "管理后台" cmd /k "cd frontend-admin && npm install && npm run dev"

echo.
echo 所有服务启动完成！
echo.
echo 访问地址：
echo 用户前端: http://localhost:3000
echo 管理后台: http://localhost:3001
echo 后端API: http://localhost:8080
echo 测试页面: http://localhost:3000/test
echo.
echo 默认管理员账号: admin / 123456
echo.
pause
