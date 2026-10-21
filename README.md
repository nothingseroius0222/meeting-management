# 会议预约管理系统

一个完整的会议预约管理系统，包含前端用户界面、管理后台和后端API服务。

## 技术栈

### 后端
- **框架**: Spring Boot 2.7.18
- **数据库**: MySQL 5.6+
- **ORM**: MyBatis Plus 3.5.3.1
- **安全**: Spring Security + JWT
- **Java版本**: Java 11

### 前端
- **框架**: Vue 3.3.4
- **UI组件**: Element Plus 2.3.8
- **构建工具**: Vite 4.4.5
- **状态管理**: Pinia 2.1.6
- **HTTP客户端**: Axios 1.4.0

## 项目结构

```
meeting-management/
├── src/                          # 后端源码
│   ├── main/java/com/meeting/
│   │   ├── entity/              # 实体类
│   │   ├── mapper/              # Mapper接口
│   │   ├── service/             # 服务层
│   │   ├── controller/          # 控制器
│   │   ├── common/              # 通用类
│   │   ├── config/              # 配置类
│   │   └── util/                # 工具类
│   └── main/resources/
│       └── application.yml      # 配置文件
├── frontend-user/               # 用户前端
│   ├── src/
│   │   ├── views/               # 页面组件
│   │   ├── router/              # 路由配置
│   │   ├── stores/              # 状态管理
│   │   └── utils/               # 工具函数
│   └── package.json
├── frontend-admin/              # 管理后台
│   ├── src/
│   │   ├── views/               # 页面组件
│   │   ├── router/              # 路由配置
│   │   ├── stores/              # 状态管理
│   │   └── utils/               # 工具函数
│   └── package.json
├── database/
│   └── init.sql                 # 数据库初始化脚本
└── pom.xml                      # Maven配置
```

## 功能特性

### 用户功能
- 用户注册/登录
- 查看会议室列表
- 预约会议室
- 查看我的预约记录
- 取消预约

### 管理员功能
- 管理员登录
- 会议室管理（增删改查）
- 预约审核
- 用户管理
- 数据统计

## 快速开始

### 1. 数据库准备

```sql
-- 创建数据库
CREATE DATABASE meeting_management;

-- 执行初始化脚本
source database/init.sql
```

### 2. 后端启动

```bash
# 进入项目根目录
cd meeting-management

# 修改数据库配置（src/main/resources/application.yml）
# 确保数据库连接信息正确

# 启动后端服务
mvn spring-boot:run
```

后端服务将在 `http://localhost:8080` 启动

### 3. 前端启动

#### 用户前端
```bash
cd frontend-user
npm install
npm run dev
```

用户前端将在 `http://localhost:3000` 启动

#### 管理后台
```bash
cd frontend-admin
npm install
npm run dev
```

管理后台将在 `http://localhost:3001` 启动

## 默认账号

### 管理员账号
- 用户名: `admin`
- 密码: `123456`

### 测试用户
可以通过注册页面创建新用户

## API接口

### 用户相关
- `POST /api/user/login` - 用户登录
- `POST /api/user/register` - 用户注册
- `GET /api/user/info` - 获取用户信息

### 会议室相关
- `GET /api/room/list` - 获取会议室列表
- `GET /api/room/{id}` - 获取会议室详情
- `POST /api/room/create` - 创建会议室（管理员）
- `PUT /api/room/update` - 更新会议室（管理员）
- `DELETE /api/room/{id}` - 删除会议室（管理员）

### 预约相关
- `POST /api/reservation/create` - 创建预约
- `GET /api/reservation/my` - 获取我的预约
- `GET /api/reservation/all` - 获取所有预约（管理员）
- `POST /api/reservation/audit` - 审核预约（管理员）
- `POST /api/reservation/cancel` - 取消预约

## 数据库表结构

### sys_user (用户表)
- id: 用户ID
- username: 用户名
- password: 密码（加密）
- real_name: 真实姓名
- email: 邮箱
- phone: 手机号
- user_type: 用户类型（0-普通用户，1-管理员）
- status: 状态（0-禁用，1-启用）

### meeting_room (会议室表)
- id: 会议室ID
- room_name: 会议室名称
- location: 位置
- capacity: 容量
- equipment: 设备描述
- status: 状态（0-禁用，1-启用）

### meeting_reservation (预约表)
- id: 预约ID
- room_id: 会议室ID
- user_id: 用户ID
- title: 会议主题
- description: 会议描述
- start_time: 开始时间
- end_time: 结束时间
- attendee_count: 参会人数
- status: 状态（0-待审核，1-已通过，2-已拒绝，3-已取消）
- audit_remark: 审核备注

## 开发说明

### 后端开发
1. 使用MyBatis Plus进行数据库操作
2. JWT进行身份认证
3. Spring Security进行权限控制
4. 统一返回结果格式

### 前端开发
1. Vue 3 Composition API
2. Element Plus UI组件
3. Pinia状态管理
4. Axios HTTP请求

## 部署说明

### 后端部署
```bash
# 打包
mvn clean package

# 运行jar包
java -jar target/meeting-management-1.0.0.jar
```

### 前端部署
```bash
# 用户前端
cd frontend-user
npm run build

# 管理后台
cd frontend-admin
npm run build
```

## 注意事项

1. 确保MySQL版本为5.6或以上
2. Java版本需要11或以上
3. Node.js版本建议使用16或以上
4. 首次启动前需要执行数据库初始化脚本
5. 默认管理员密码为123456，建议生产环境修改

## 页面展示

![image-20250913194758392](https://i-blog.csdnimg.cn/img_convert/c795d1dbb9e4da4ed9c7cc0c82ba0c95.png)

![image-20250913194810518](https://i-blog.csdnimg.cn/img_convert/19a3439a447ed5970b84d7dbe65af78a.png)

![image-20250913194838923](https://i-blog.csdnimg.cn/img_convert/a99ae1a7734c0eb048234620b1242250.png)

![image-20250913194943065](https://i-blog.csdnimg.cn/img_convert/382d2a1dd832da39fe901505b8947246.png)



![image-20250913194950951](https://i-blog.csdnimg.cn/img_convert/b84ab5689f14a9de0908c33f852ae2b5.png)

![image-20250913195004602](https://i-blog.csdnimg.cn/img_convert/ec4a49ff0895dac42cc1952788cb28a0.png)

![image-20250913195018952](https://i-blog.csdnimg.cn/img_convert/900633e0a54bbc971344f2ccc345d2d5.png)
