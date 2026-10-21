# 问题排查指南

## 常见问题及解决方案

### 1. 前端访问报错

#### 问题：页面无法加载或显示空白
**可能原因：**
- 后端服务未启动
- 数据库连接失败
- 端口冲突

**解决方案：**
1. 检查后端服务是否启动：
   ```bash
   curl http://localhost:8080/api/user/info
   ```

2. 检查数据库连接：
   - 确保MySQL服务正在运行
   - 确保数据库 `50906` 已创建
   - 检查用户名密码是否正确

3. 使用测试页面检查：
   - 访问 http://localhost:3000/test
   - 点击各个测试按钮检查连接状态

#### 问题：登录失败
**可能原因：**
- 数据库中没有默认管理员账号
- 密码加密问题

**解决方案：**
1. 检查数据库中的管理员账号：
   ```sql
   SELECT * FROM sys_user WHERE username = 'admin';
   ```

2. 如果账号不存在，手动插入：
   ```sql
   INSERT INTO sys_user (username, password, real_name, user_type, status) 
   VALUES ('admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi', '系统管理员', 1, 1);
   ```

### 2. 后端启动失败

#### 问题：数据库连接失败
**错误信息：** `Could not create connection to database server`

**解决方案：**
1. 检查MySQL服务状态
2. 检查数据库配置（application.yml）
3. 确保数据库 `50906` 已创建
4. 检查用户名密码

#### 问题：端口被占用
**错误信息：** `Port 8080 was already in use`

**解决方案：**
1. 查找占用端口的进程：
   ```bash
   netstat -ano | findstr :8080
   ```
2. 结束进程或修改端口

### 3. 数据库问题

#### 问题：表不存在
**解决方案：**
1. 执行数据库初始化脚本：
   ```bash
   mysql -u root -p < database/init.sql
   ```

2. 检查表是否创建成功：
   ```sql
   USE 50906;
   SHOW TABLES;
   ```

#### 问题：外键约束错误
**解决方案：**
1. 检查表创建顺序
2. 先创建用户表和会议室表，再创建预约表

### 4. 前端构建问题

#### 问题：npm install 失败
**解决方案：**
1. 清除npm缓存：
   ```bash
   npm cache clean --force
   ```

2. 删除node_modules重新安装：
   ```bash
   rm -rf node_modules
   npm install
   ```

#### 问题：端口冲突
**解决方案：**
1. 修改vite.config.js中的端口配置
2. 或者结束占用端口的进程

### 5. 调试步骤

#### 步骤1：检查服务状态
```bash
# 检查Java进程
jps

# 检查端口占用
netstat -ano | findstr :8080
netstat -ano | findstr :3000
netstat -ano | findstr :3001
```

#### 步骤2：检查日志
- 后端日志：查看控制台输出
- 前端日志：打开浏览器开发者工具

#### 步骤3：使用测试页面
访问 http://localhost:3000/test 进行系统测试

#### 步骤4：检查网络连接
```bash
# 测试后端连接
curl http://localhost:8080/api/user/info

# 测试前端连接
curl http://localhost:3000
```

### 6. 环境要求

#### 必需环境：
- Java 11+
- Node.js 16+
- MySQL 5.6+
- Maven 3.6+

#### 推荐环境：
- Java 17
- Node.js 18
- MySQL 8.0
- Maven 3.8+

### 7. 联系支持

如果以上解决方案都无法解决问题，请提供：
1. 错误日志
2. 系统环境信息
3. 操作步骤
4. 浏览器控制台截图
