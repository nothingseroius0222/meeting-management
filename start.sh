#!/bin/bash

echo "启动会议预约管理系统..."

echo ""
echo "1. 启动后端服务..."
gnome-terminal --title="后端服务" -- bash -c "mvn spring-boot:run; exec bash" &

echo ""
echo "2. 等待后端服务启动..."
sleep 10

echo ""
echo "3. 启动用户前端..."
gnome-terminal --title="用户前端" -- bash -c "cd frontend-user && npm install && npm run dev; exec bash" &

echo ""
echo "4. 启动管理后台..."
gnome-terminal --title="管理后台" -- bash -c "cd frontend-admin && npm install && npm run dev; exec bash" &

echo ""
echo "所有服务启动完成！"
echo "用户前端: http://localhost:3000"
echo "管理后台: http://localhost:3001"
echo "后端API: http://localhost:8080"
echo ""
echo "默认管理员账号: admin / 123456"
echo ""
read -p "按任意键继续..."
