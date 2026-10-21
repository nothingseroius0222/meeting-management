@echo off
echo 启动会议预约管理系统...

echo.
echo 1. 启动后端服务...
start "后端服务" cmd /k "mvn spring-boot:run"

echo.
echo 2. 等待后端服务启动...
timeout /t 10 /nobreak > nul

echo.
echo 3. 启动用户前端...
start "用户前端" cmd /k "cd frontend-user && npm install && npm run dev"

echo.
echo 4. 启动管理后台...
start "管理后台" cmd /k "cd frontend-admin && npm install && npm run dev"

echo.
echo 所有服务启动完成！
echo 用户前端: http://localhost:3000
echo 管理后台: http://localhost:3001
echo 后端API: http://localhost:8080
echo.
echo 默认管理员账号: admin / 123456
echo.
pause
