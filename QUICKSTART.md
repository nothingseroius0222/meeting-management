# 会议预约管理系统 - 启动指南

## 🚀 快速启动

### 1. 环境准备
确保您的系统已安装：
- **Java 11+** (推荐Java 17)
- **Node.js 16+** (推荐Node.js 18)
- **MySQL 5.6+** (推荐MySQL 8.0)
- **Maven 3.6+**

### 2. 数据库准备
```sql
-- 1. 创建数据库
CREATE DATABASE `50906` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 2. 执行初始化脚本
source database/init.sql;
```

### 3. 启动服务

#### 方式一：使用启动脚本（推荐）
```bash
# Windows
start-check.bat

# Linux/Mac
chmod +x start-check.sh
./start-check.sh
```

#### 方式二：手动启动
```bash
# 1. 启动后端
mvn spring-boot:run

# 2. 启动用户前端（新终端）
cd frontend-user
npm install
npm run dev

# 3. 启动管理后台（新终端）
cd frontend-admin
npm install
npm run dev
```

### 4. 访问系统
- **用户前端**: http://localhost:3000
- **管理后台**: http://localhost:3001
- **后端API**: http://localhost:8080
- **测试页面**: http://localhost:3000/test

### 5. 默认账号
- **管理员**: `admin` / `123456`

## 🔧 配置说明

### 数据库配置
修改 `src/main/resources/application.yml` 中的数据库连接信息：
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/50906
    username: root
    password: 123456
```

### 端口配置
- 后端端口：8080 (在application.yml中修改)
- 用户前端端口：3000 (在frontend-user/vite.config.js中修改)
- 管理后台端口：3001 (在frontend-admin/vite.config.js中修改)

## 🐛 常见问题

### 问题1：前端页面无法加载
**解决方案：**
1. 检查后端服务是否启动：访问 http://localhost:8080/api/health
2. 检查数据库连接：访问 http://localhost:3000/test 进行测试
3. 查看浏览器控制台错误信息

### 问题2：登录失败
**解决方案：**
1. 确保数据库中有管理员账号
2. 检查密码是否正确（默认：123456）
3. 查看后端日志

### 问题3：端口被占用
**解决方案：**
```bash
# Windows
netstat -ano | findstr :8080
taskkill /PID <进程ID> /F

# Linux/Mac
lsof -ti:8080
kill -9 <进程ID>
```

## 📊 系统测试

### 使用测试页面
访问 http://localhost:3000/test 可以测试：
- 后端服务连接
- 数据库连接
- 登录接口

### API测试
```bash
# 健康检查
curl http://localhost:8080/api/health

# 系统信息
curl http://localhost:8080/api/info

# 登录测试
curl -X POST http://localhost:8080/api/user/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"123456"}'
```

## 📝 开发说明

### 项目结构
```
meeting-management/
├── src/                    # 后端源码
├── frontend-user/          # 用户前端
├── frontend-admin/         # 管理后台
├── database/               # 数据库脚本
└── docs/                   # 文档
```

### 技术栈
- **后端**: Spring Boot + MyBatis Plus + MySQL + JWT
- **前端**: Vue3 + Element Plus + Pinia + Axios
- **构建**: Maven + Vite

### 开发模式
```bash
# 后端热重载
mvn spring-boot:run

# 前端热重载
npm run dev
```

## 📞 技术支持

如果遇到问题，请：
1. 查看 [问题排查指南](TROUBLESHOOTING.md)
2. 检查系统日志
3. 使用测试页面进行诊断
4. 提供详细的错误信息

## 🎯 功能特性

### 用户功能
- ✅ 用户注册/登录
- ✅ 会议室浏览
- ✅ 会议预约
- ✅ 预约管理
- ✅ 预约取消

### 管理员功能
- ✅ 会议室管理
- ✅ 预约审核
- ✅ 用户管理
- ✅ 数据统计
- ✅ 系统监控

## 🔒 安全特性
- JWT Token认证
- BCrypt密码加密
- CORS跨域支持
- SQL注入防护
- XSS攻击防护
