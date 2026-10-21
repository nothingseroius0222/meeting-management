@echo off
echo 测试API接口
echo ===========

echo.
echo 1. 测试健康检查接口...
curl -s http://localhost:8080/api/health

echo.
echo.
echo 2. 测试用户列表接口...
curl -s http://localhost:8080/api/user/list

echo.
echo.
echo 3. 测试会议室列表接口...
curl -s http://localhost:8080/api/room/list

echo.
echo.
echo 4. 测试登录接口...
curl -s -X POST http://localhost:8080/api/user/login -H "Content-Type: application/json" -d "{\"username\":\"admin\",\"password\":\"123456\"}"

echo.
echo.
echo 测试完成！
echo.
pause
