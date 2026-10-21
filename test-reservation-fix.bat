@echo off
echo 测试预约管理修复
echo ==================

echo.
echo 1. 测试预约列表接口...
curl -s http://localhost:8080/api/reservation/all

echo.
echo.
echo 2. 测试用户预约接口...
curl -s "http://localhost:8080/api/reservation/my?userId=1"

echo.
echo.
echo 3. 测试会议室列表接口...
curl -s http://localhost:8080/api/room/list

echo.
echo.
echo 4. 测试用户列表接口...
curl -s http://localhost:8080/api/user/list

echo.
echo.
echo 测试完成！
echo.
echo 请检查：
echo 1. 预约列表是否包含roomName和userName字段
echo 2. 管理后台预约管理页面是否显示会议室和预约人
echo 3. 用户前端我的预约页面是否显示会议室名称
echo.
pause
