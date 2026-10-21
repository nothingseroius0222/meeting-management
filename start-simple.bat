@echo off
echo 会议预约管理系统 - 快速启动
echo ================================

echo.
echo 正在启动后端服务...
start "后端服务" cmd /k "mvn spring-boot:run"

echo.
echo 等待后端服务启动（15秒）...
timeout /t 15 /nobreak > nul

echo.
echo 正在启动用户前端...
start "用户前端" cmd /k "cd frontend-user && npm install && npm run dev"

echo.
echo 正在启动管理后台...
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
echo 如果遇到问题，请查看 TROUBLESHOOTING.md
echo.
pause
