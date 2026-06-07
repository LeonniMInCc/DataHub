# DataHub - 开发者数字资产共享平台

> 大学《数据库系统原理》课程项目

## 项目介绍

DataHub 是一个面向开发者的数字资产交易平台。平台允许开发者发布数据集和 API，浏览其他开发者的数据资产，订阅数据资产，查看调用记录和统计分析。

平台包含两类角色：
- **数据提供者（Provider）**：发布和管理数据资产，查看订阅情况和调用统计
- **数据订阅者（Subscriber）**：浏览、搜索、订阅数据资产，查看调用记录

## 技术栈

| 层级 | 技术 |
|------|------|
| 后端 | Java 17, Spring Boot 3.2, Spring Security, JWT, JPA |
| 前端 | Vue 3, Vite, Pinia, Vue Router, Axios, Tailwind CSS, ECharts |
| 数据库 | MySQL 8.0 |
| 构建 | Maven, npm |

## 项目结构

```
├── backend/                    # Spring Boot 后端
│   ├── pom.xml
│   └── src/main/java/com/datahub/
│       ├── DataHubApplication.java
│       ├── config/             # Security, CORS 配置
│       ├── entity/             # JPA 实体
│       ├── repository/         # 数据访问层
│       ├── service/            # 业务逻辑层
│       ├── controller/         # REST API 控制器
│       ├── dto/                # 数据传输对象
│       ├── security/           # JWT 工具和过滤器
│       └── exception/          # 全局异常处理
├── frontend/                   # Vue 3 前端
│   ├── src/
│   │   ├── views/              # 页面组件
│   │   ├── stores/             # Pinia 状态管理
│   │   ├── router/             # Vue Router 路由
│   │   └── api/                # Axios 封装
│   └── package.json
├── database/                   # 数据库脚本
│   ├── schema.sql              # DDL + 索引 + 外键
│   └── test_data.sql           # 测试数据
└── docs/                       # 项目文档
```

## 快速开始

### 1. 数据库准备

```bash
mysql -u root -p < database/schema.sql
mysql -u root -p < database/test_data.sql
```

### 2. 启动后端

```bash
cd backend
# 修改 src/main/resources/application.properties 中的数据库连接信息
mvn spring-boot:run
```

后端运行在 http://localhost:8080

### 3. 启动前端

```bash
cd frontend
npm install
npm run dev
```

前端运行在 http://localhost:5173

### 4. 测试账号

| 角色 | 用户名 | 密码 |
|------|--------|------|
| Provider | zhangsan | password123 |
| Subscriber | sub_a | password123 |

## API 接口

### 认证

| 方法 | 路径 | 说明 |
|------|------|------|
| POST | /api/auth/register | 注册 |
| POST | /api/auth/login | 登录 |

### 资产

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | /api/assets | 资产列表（分页、搜索、筛选） |
| GET | /api/assets/{id} | 资产详情 |
| POST | /api/assets | 发布资产（需认证） |
| PUT | /api/assets/{id} | 更新资产（需认证） |
| DELETE | /api/assets/{id} | 删除资产（需认证） |
| GET | /api/assets/my | 我的资产（需认证） |

### 订阅

| 方法 | 路径 | 说明 |
|------|------|------|
| POST | /api/subscriptions | 订阅资产（需认证） |
| DELETE | /api/subscriptions/{id} | 取消订阅（需认证） |
| GET | /api/subscriptions/my | 我的订阅（需认证） |

### 仪表盘

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | /api/dashboard | 仪表盘数据 |

### 用户

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | /api/user/profile | 个人资料（需认证） |

## 功能特性

- ✅ JWT 认证与授权
- ✅ 用户注册/登录
- ✅ 资产发布/编辑/删除
- ✅ 资产搜索与分类筛选
- ✅ 订阅/取消订阅
- ✅ 重复订阅防护
- ✅ 数据仪表盘（ECharts 可视化）
- ✅ 暗色主题 Glassmorphism 毛玻璃 UI
- ✅ 响应式布局
- ✅ 统一 JSON 响应格式
- ✅ 全局异常处理
