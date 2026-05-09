# Tlias Web Management

智能学习辅助系统 - Java后端项目

## 项目概述

Tlias Web Management 是一个完整的企业级Web管理系统，为教育机构提供全方位的智能管理解决方案。系统包含部门管理、员工管理、班级管理、学生管理、数据统计分析等核心功能。

## 功能特性

### 核心功能
- ✅ 部门管理 - 部门信息的增删改查
- ✅ 员工管理 - 员工信息维护与权限管理
- ✅ 班级管理 - 班级信息的创建与管理
- ✅ 学生管理 - 学生档案管理
- ✅ 数据统计 - 多维度数据可视化分析
- ✅ 操作日志 - 完整的操作记录追踪
- ✅ 安全认证 - JWT Token认证机制

### 技术特性
- 🔧 Redis缓存 - 热点数据缓存，提升性能
- 📨 RabbitMQ消息队列 - 异步处理，削峰填谷
- 🛡️ 布隆过滤器 - 防止缓存穿透
- 📊 MyBatis持久化 - 灵活的SQL映射
- 🔐 BCrypt密码加密 - 安全的密码存储
- 📝 统一异常处理 - 规范的错误响应

## 技术栈

### 后端技术栈
- Spring Boot 2.6.13
- MyBatis
- MySQL 8+
- Redis 6+
- RabbitMQ 3+
- JWT (jjwt 0.11.5)
- Spring Security Crypto
- Lombok
- PageHelper
- Swagger/Knife4j

### 前端技术栈
- Vue 3
- Element Plus
- Vue Router
- Axios
- Vite
- ECharts (数据可视化)

## 项目结构

```
tlias-web-management/
├── .github/
│   └── ISSUE_TEMPLATE/          # Issues模板
│       ├── bug_report.md
│       ├── feature_request.md
│       └── task_template.md
├── .trae/
│   └── documents/               # 项目文档
│       ├── GITHUB_SETUP.md
│       └── Redis_MQ使用规划方案.md
├── front/                       # 前端项目
│   └── tilas-management/
│       ├── src/
│       │   ├── api/             # API接口
│       │   ├── views/           # 页面组件
│       │   ├── router/          # 路由配置
│       │   └── utils/           # 工具函数
│       └── vite.config.js
├── src/                         # 后端项目
│   └── main/
│       ├── java/com/lcy/
│       │   ├── controller/      # 控制器层
│       │   ├── service/         # 服务层接口
│       │   │   └── impl/        # 服务层实现
│       │   ├── mapper/          # 数据访问层
│       │   ├── pojo/            # 实体类和DTO
│       │   ├── config/          # 配置类
│       │   ├── utils/           # 工具类
│       │   ├── interceptor/     # 拦截器
│       │   ├── listener/        # 消息监听器
│       │   ├── exception/       # 异常处理
│       │   └── aop/             # 切面编程
│       └── resources/
│           ├── application.yml  # 主配置文件
│           ├── application-dev.yml
│           ├── application-vm.yml
│           ├── application-prod.yml
│           └── com/lcy/mapper/  # MyBatis XML映射
├── tlias-database-init.sql      # 数据库初始化脚本
└── pom.xml                      # Maven配置文件
```

## 快速开始

### 环境要求

- JDK 8 或更高版本
- Maven 3.6+
- MySQL 8.0+
- Redis 6.0+
- RabbitMQ 3.8+

### 本地开发步骤

1. **克隆项目**
   ```bash
   git clone https://github.com/tonelcy/tliasmanagemet.git
   cd tlias-web-management
   ```

2. **初始化数据库**
   ```bash
   # 创建数据库
   mysql -u root -p
   CREATE DATABASE tlias;
   
   # 执行初始化脚本
   mysql -u root -p tlias < tlias-database-init.sql
   ```

3. **配置环境**
   
   修改 `application.yml` 或使用相应的配置文件：
   - `application-dev.yml` - 本地开发环境
   - `application-vm.yml` - 虚拟机开发环境
   - `application-prod.yml` - 生产环境

4. **启动后端服务**
   ```bash
   mvn clean install
   mvn spring-boot:run
   ```

5. **启动前端服务**
   ```bash
   cd front/tilas-management
   npm install
   npm run dev
   ```

6. **访问应用**
   - 前端地址：http://localhost:5173
   - API文档：http://localhost:8080/doc.html
   - 后端服务：http://localhost:8080

## 分支管理

### 分支策略
- `main` - 主分支，生产环境代码，受保护
- `develop` - 开发分支，日常开发在此分支进行
- `feature/*` - 功能分支，从develop创建
- `bugfix/*` - 修复分支，用于修复问题
- `hotfix/*` - 紧急修复分支，从main创建

### 开发流程
1. 从 `develop` 分支创建新的功能分支
2. 开发完成后提交Pull Request
3. 代码审查通过后合并到 `develop`
4. 发布时将 `develop` 合并到 `main`

## 配置说明

### 多环境配置
项目支持多种环境配置：
- `dev` - 本地开发环境
- `vm` - 虚拟机开发环境（Ubuntu）
- `prod` - 生产环境

激活方式：
```bash
# 修改 application.yml 中的 spring.profiles.active
spring:
  profiles:
    active: dev
```

### 核心配置项
- **数据库连接** - datasource配置
- **Redis连接** - redis配置
- **RabbitMQ连接** - rabbitmq配置
- **文件上传** - OSS配置
- **JWT密钥** - jwt配置

## API文档

启动项目后访问：
- Swagger/Knife4j文档：http://localhost:8080/doc.html

## 数据库表设计

系统包含以下核心表：
- `dept` - 部门表
- `emp` - 员工表
- `college` - 学院表
- `clazz` - 班级表
- `student` - 学生表
- `operate_log` - 操作日志表
- `emp_log` - 员工操作记录表

详细表结构请参考 `tlias-database-init.sql`。

## 开发指南

### 添加新功能
1. 在 `pojo` 包创建实体类
2. 在 `mapper` 包创建数据访问接口
3. 在 `service` 包创建业务逻辑
4. 在 `controller` 包创建REST API
5. 添加相应的单元测试

### 代码规范
- 遵循阿里巴巴Java开发规范
- 使用Lombok简化代码
- 统一返回格式为 `Result` 对象
- Service层添加事务注解
- Controller层添加API文档注解

## 测试

### 运行测试
```bash
# 单元测试
mvn test

# 集成测试
mvn verify
```

## 部署

### 生产环境部署

1. **编译打包**
   ```bash
   mvn clean package -DskipTests
   ```

2. **上传jar包**
   ```bash
   scp target/tlias-web-management-0.0.1-SNAPSHOT.jar user@server:/opt/tlias-web-management/
   ```

3. **启动服务**
   ```bash
   # 使用提供的部署脚本
   cd /opt/tlias-web-management
   ./deploy/start.sh
   ```

详细部署指南请参考 `.trae/documents/GITHUB_SETUP.md`。

## 贡献指南

1. Fork本仓库
2. 创建您的功能分支 (`git checkout -b feature/AmazingFeature`)
3. 提交您的更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到远程分支 (`git push origin feature/AmazingFeature`)
5. 创建Pull Request

## 许可证

本项目采用 MIT 许可证 - 详见 [LICENSE](LICENSE) 文件

## 联系方式

- 项目地址：https://github.com/tonelcy/tliasmanagemet
- 问题反馈：请在GitHub Issues中提交

## 致谢

感谢所有为这个项目做出贡献的开发者！

---

**Tlias Web Management** - 让教育管理更智能
