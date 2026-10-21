# 会议预约管理系统 - 项目结构

## 整体架构
```
会议预约管理系统
├── 后端服务 (Spring Boot)
├── 用户前端 (Vue3)
├── 管理后台 (Vue3)
└── 数据库 (MySQL)
```

## 技术栈对比

| 组件 | 技术栈 | 端口 | 说明 |
|------|--------|------|------|
| 后端API | Spring Boot + MyBatis Plus + MySQL | 8080 | RESTful API服务 |
| 用户前端 | Vue3 + Element Plus + Vite | 3000 | 普通用户界面 |
| 管理后台 | Vue3 + Element Plus + Vite | 3001 | 管理员界面 |

## 功能模块

### 用户功能
- 用户注册/登录
- 查看会议室列表
- 预约会议室
- 查看我的预约
- 取消预约

### 管理员功能
- 管理员登录
- 会议室管理
- 预约审核
- 用户管理
- 数据统计

## 数据库设计

### 核心表结构
1. **sys_user** - 用户表
   - 存储用户基本信息
   - 区分普通用户和管理员

2. **meeting_room** - 会议室表
   - 存储会议室信息
   - 包含容量、设备等属性

3. **meeting_reservation** - 预约表
   - 存储预约信息
   - 关联用户和会议室
   - 包含审核状态

## API设计

### 认证相关
- POST /api/user/login - 用户登录
- POST /api/user/register - 用户注册

### 会议室相关
- GET /api/room/list - 获取会议室列表
- POST /api/room/create - 创建会议室（管理员）

### 预约相关
- POST /api/reservation/create - 创建预约
- GET /api/reservation/my - 获取我的预约
- POST /api/reservation/audit - 审核预约（管理员）

## 部署架构

### 开发环境
- 后端: Maven + Spring Boot DevTools
- 前端: Vite Dev Server
- 数据库: MySQL 5.6+

### 生产环境
- 后端: JAR包部署
- 前端: 静态文件部署
- 数据库: MySQL 8.0+

## 安全设计

### 认证机制
- JWT Token认证
- 密码BCrypt加密
- 角色权限控制

### 数据安全
- SQL注入防护
- XSS攻击防护
- CSRF防护

## 扩展性设计

### 水平扩展
- 无状态API设计
- 数据库读写分离
- 缓存层支持

### 功能扩展
- 插件化架构
- 微服务拆分
- 消息队列支持
