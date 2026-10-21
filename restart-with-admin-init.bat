@echo off
echo 重新启动后端服务以初始化管理员账号
echo =====================================

echo.
echo 1. 停止现有的后端服务...
taskkill /f /im java.exe 2>nul

echo.
echo 2. 等待服务完全停止...
timeout /t 3 /nobreak > nul

echo.
echo 3. 重新启动后端服务...
start "后端服务" cmd /k "mvn spring-boot:run"

echo.
echo 4. 等待后端服务启动并初始化管理员账号...
echo 请查看后端控制台输出，应该会显示：
echo "=== 初始化管理员账号 ==="
echo "管理员账号创建成功！"
echo "用户名: admin"
echo "密码: 123456"
echo "========================"

echo.
echo 5. 等待15秒后启动前端...
timeout /t 15 /nobreak > nul

echo.
echo 6. 启动用户前端...
start "用户前端" cmd /k "cd frontend-user && npm run dev"

echo.
echo 7. 启动管理后台...
start "管理后台" cmd /k "cd frontend-admin && npm run dev"

echo.
echo 所有服务启动完成！
echo.
echo 访问地址：
echo 用户前端: http://localhost:3000
echo 管理后台: http://localhost:3001
echo 后端API: http://localhost:8080
echo.
echo 管理员账号：
echo 用户名: admin
echo 密码: 123456
echo.
echo 如果仍有问题，请查看后端控制台日志
echo.
pause
