# DataHub 开发者数字资产共享平台

DataHub 是一个面向开发者的数据资产共享与订阅平台，支持 Provider 发布数据集/API，Subscriber 浏览资产、模拟支付订阅并查看额度使用情况。项目采用前后端分离架构，包含完整的数据库建表脚本、测试数据、REST API、JWT 登录认证和 ECharts 数据仪表盘。

本项目作为本人2026年《数据库系统原理》课程设计。

## 核心功能

| 模块 | 功能 |
|------|------|
| 用户认证 | 注册、登录、JWT 无状态认证、BCrypt 密码加密 |
| 角色权限 | Provider 可发布资产；Subscriber 可订阅资产 |
| 资产市场 | 数据集/API 列表、关键字搜索、类型筛选、分页、价格/时间排序 |
| 资产管理 | 发布、查看详情、更新、软删除资产 |
| 订阅系统 | 扫码支付展示、支付成功/失败模拟、防重复订阅、取消订阅 |
| 个人中心 | 用户资料、API Key、角色身份、资产/订阅数量 |
| 数据仪表盘 | 用户数、资产数、订阅数、调用数、资产类型分布、热门资产、近 7 天调用趋势 |

## 技术栈

| 层级 | 技术 |
|------|------|
| 后端 | Java 17, Spring Boot 3.4.5, Spring Web, Spring Security, Spring Data JPA, Validation |
| 认证 | JWT, BCrypt |
| 数据库 | MySQL 8.0；Demo 模式使用 H2 内存数据库 |
| 前端 | Vue 3, Vite 5, Vue Router, Pinia, Axios |
| UI/图表 | Tailwind CSS, ECharts |
| 部署 | Docker, Docker Compose |

## 项目结构

```text
.
├── backend/                         # Spring Boot 后端
│   ├── pom.xml
│   ├── Dockerfile
│   └── src/main/
│       ├── java/com/datahub/
│       │   ├── config/              # Security、CORS 配置
│       │   ├── controller/          # REST API 控制器
│       │   ├── dto/                 # 请求/响应 DTO
│       │   ├── entity/              # JPA 实体
│       │   ├── exception/           # 全局异常处理
│       │   ├── repository/          # 数据访问层
│       │   ├── security/            # JWT 工具与过滤器
│       │   └── service/             # 业务逻辑层
│       └── resources/
│           ├── application.properties.template
│           ├── application-docker.properties
│           ├── application-demo.properties
│           ├── demo-schema.sql
│           └── demo-data.sql
├── frontend/                        # Vue 3 前端
│   ├── Dockerfile
│   ├── package.json
│   └── src/
│       ├── api/                     # Axios 封装
│       ├── assets/                  # 全局样式
│       ├── router/                  # 前端路由
│       ├── stores/                  # Pinia 状态
│       ├── utils/                   # 登录状态工具
│       └── views/                   # 页面组件
├── database/
│   ├── schema.sql                   # MySQL 建表、索引、外键
│   └── test_data.sql                # MySQL 测试数据
├── docs/
│   ├── er_diagram.md
│   ├── defense_ppt.md
│   └── DataHub.postman_collection.json
├── docker-compose.yml               # MySQL + 后端 + 前端
└── docker-compose.demo.yml          # H2 Demo 后端 + 前端
```

## 快速启动

### 方式一：Demo 模式（无需 MySQL）

Demo 模式使用 H2 内存数据库，启动时自动加载 `demo-schema.sql` 和 `demo-data.sql`。

启动后端：

```bash
cd backend
./mvnw spring-boot:run -Dspring-boot.run.profiles=demo
```

启动前端：

```bash
cd frontend
npm install
npm run dev
```

访问地址：

| 服务 | 地址 |
|------|------|
| 前端 | http://localhost:5173 |
| 后端 API | http://localhost:8080 |
| H2 控制台 | http://localhost:8080/h2-console |

H2 控制台连接信息：

```text
JDBC URL: jdbc:h2:mem:datahub
User: sa
Password: 留空
```

### 方式二：本地 MySQL 模式

准备 MySQL 8.0 数据库：

```bash
mysql -u root -p < database/schema.sql
mysql -u root -p < database/test_data.sql
```

创建后端配置：

```bash
cd backend
cp src/main/resources/application.properties.template src/main/resources/application.properties
```

修改 `backend/src/main/resources/application.properties` 中的数据库账号、密码，然后启动后端：

```bash
./mvnw spring-boot:run
```

启动前端：

```bash
cd frontend
npm install
npm run dev
```

### 方式三：Docker Compose

当前后端 Dockerfile 会复制 `backend/target/*.jar`，因此需要先构建后端 jar：

```bash
cd backend
./mvnw clean package -DskipTests
```

回到项目根目录启动完整环境：

```bash
docker compose up --build
```

该方式会启动：

| 容器 | 端口 | 说明 |
|------|------|------|
| datahub-mysql | 3306 | MySQL 8.0，自动执行 `database/schema.sql` 和 `database/test_data.sql` |
| datahub-backend | 8080 | Spring Boot API，使用 `docker` profile |
| datahub-frontend | 5173 | Vite 前端服务 |

如果只想用 H2 Demo 容器：

```bash
docker compose -f docker-compose.demo.yml up --build
```

## 测试账号

初始化数据中的演示账号密码均为 `password123`。

| 角色 | 用户名 | 密码 |
|------|--------|------|
| Provider | zhangsan | password123 |
| Provider | lisi | password123 |
| Subscriber | sub_a | password123 |

MySQL 测试数据还包含 `wangwu`、`sub_b` 等更多账号。

## API 概览

接口统一以 `/api` 为前缀，前端通过 Vite proxy 转发到 `http://localhost:8080`。

### 认证

| 方法 | 路径 | 权限 | 说明 |
|------|------|------|------|
| POST | `/api/auth/register` | 公开 | 注册并返回 JWT |
| POST | `/api/auth/login` | 公开 | 登录并返回 JWT |

### 资产

| 方法 | 路径 | 权限 | 说明 |
|------|------|------|------|
| GET | `/api/assets` | 公开 | 资产列表，支持 `page`、`size`、`keyword`、`assetType` |
| GET | `/api/assets/{id}` | 公开 | 资产详情和订阅人数 |
| POST | `/api/assets` | 登录 | 发布资产 |
| PUT | `/api/assets/{id}` | 登录 | 更新本人发布的资产 |
| DELETE | `/api/assets/{id}` | 登录 | 删除本人发布的资产 |
| GET | `/api/assets/my` | 登录 | 我的资产 |

### 订阅

| 方法 | 路径 | 权限 | 说明 |
|------|------|------|------|
| POST | `/api/subscriptions` | 登录 | 订阅资产，支持 `mockPaymentSuccess` 模拟支付结果 |
| GET | `/api/subscriptions/my` | 登录 | 我的订阅 |
| DELETE | `/api/subscriptions/{id}` | 登录 | 取消订阅 |

### 仪表盘和用户

| 方法 | 路径 | 权限 | 说明 |
|------|------|------|------|
| GET | `/api/dashboard` | 公开 | 平台 KPI 和图表数据 |
| GET | `/api/user/profile` | 登录 | 当前用户资料 |
| GET | `/api/user/stats` | 登录 | 当前用户统计占位接口 |

通用响应格式：

```json
{
  "code": 200,
  "message": "success",
  "data": {}
}
```

## 数据库设计

MySQL 初始化脚本位于 `database/schema.sql`，核心表如下：

| 表 | 说明 |
|----|------|
| `users` | 用户表，保存 Provider/Subscriber、密码哈希、API Key |
| `assets` | 数据资产表，保存数据集/API 信息、价格、状态、标签 |
| `subscriptions` | 订阅表，记录订阅周期、额度、状态，并通过唯一约束防重复订阅 |
| `usage_logs` | 调用日志表，记录请求时间、状态码、响应时间、IP |

关键约束：

- `assets.provider_id -> users.user_id`
- `subscriptions.subscriber_id -> users.user_id`
- `subscriptions.asset_id -> assets.asset_id`
- `usage_logs.sub_id -> subscriptions.sub_id`
- `subscriptions` 上存在 `uk_sub_asset_user(subscriber_id, asset_id)`，避免同一用户重复订阅同一资产

## 常用开发命令

后端：

```bash
cd backend
./mvnw spring-boot:run
./mvnw spring-boot:run -Dspring-boot.run.profiles=demo
./mvnw test
./mvnw clean package -DskipTests
```

前端：

```bash
cd frontend
npm install
npm run dev
npm run build
npm run preview
```

Docker：

```bash
docker compose up --build
docker compose -f docker-compose.demo.yml up --build
docker compose down
```

## 页面路由

| 路由 | 页面 |
|------|------|
| `/` | 首页工作台 |
| `/login` | 登录 |
| `/register` | 注册 |
| `/assets` | 资产市场 |
| `/assets/:id` | 资产详情 |
| `/publish` | 发布资产，仅 Provider 可进入 |
| `/subscriptions` | 我的订阅，需登录 |
| `/dashboard` | 数据仪表盘 |
| `/profile` | 个人中心，需登录 |

## 补充文档

- `docs/er_diagram.md`：ER 图说明
- `docs/defense_ppt.md`：课程答辩 PPT 文案
- `docs/DataHub.postman_collection.json`：Postman 接口集合
