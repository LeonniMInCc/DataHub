# DataHub 项目答辩 PPT 内容

## 第1页 - 封面

**DataHub —— 开发者数字资产共享平台**

《数据库系统原理》课程项目答辩

---

## 第2页 - 项目背景

### 问题与机遇
- 开发者对高质量数据集和 API 的需求日益增长
- 数据资产缺乏统一的发布和交易平台
- 传统的数据交易平台复杂、门槛高

### 解决方案
构建一个轻量级、开发者友好的数字资产共享平台，连接数据提供者和数据消费者。

---

## 第3页 - 功能介绍

### 核心功能
1. **用户系统**：注册/登录，JWT 认证，角色区分（Provider / Subscriber）
2. **资产管理**：发布、编辑、删除、搜索、分类筛选数据集和 API
3. **订阅系统**：订阅/取消订阅，自动分配调用额度，防重复订阅
4. **调用统计**：记录调用时间、状态码、响应时间
5. **数据仪表盘**：KPI 卡片、ECharts 可视化图表

---

## 第4页 - 数据库设计（重点）

### ER 图

```
┌─────────────┐       ┌──────────────────┐       ┌───────────────┐
│  developers  │       │  digital_assets   │       │ subscriptions │
├─────────────┤       ├──────────────────┤       ├───────────────┤
│ dev_id (PK) │──1:N──│ asset_id (PK)    │──1:N──│ sub_id (PK)   │──1:N──┐
│ username    │       │ provider_id (FK) │       │ subscriber_id  │       │
│ password    │       │ title            │       │ asset_id (FK)  │       │
│ email       │       │ asset_type       │       │ quota_limit    │       │
│ api_key     │       │ description      │       │ quota_used     │       │
│ role        │       │ endpoint_url     │       │ start_date     │       │
│ avatar      │       │ price_per_month  │       │ end_date       │       │
│ created_at  │       │ status           │       │ status         │       │
└─────────────┘       │ tags             │       │ created_at     │       │
                      │ created_at       │       └───────────────┘       │
                      └──────────────────┘                               │
                                                                         │
                                              ┌──────────────────┐       │
                                              │   usage_logs     │       │
                                              ├──────────────────┤       │
                                              │ log_id (PK)      │◄──────┘
                                              │ sub_id (FK)      │
                                              │ request_time     │
                                              │ status_code      │
                                              │ response_time    │
                                              │ ip_address       │
                                              └──────────────────┘
```

### 设计要点
- 4 张核心表，关系清晰
- 外键约束保证数据完整性
- 索引优化查询性能
- 枚举类型约束字段值

---

## 第5页 - 系统架构

```
┌─────────────────────────────────────────────────────┐
│                     前端 (Vue 3)                      │
│    Tailwind CSS  │  Pinia  │  Vue Router  │  ECharts  │
│                 Axios (HTTP Client)                  │
└─────────────────────┬───────────────────────────────┘
                      │ RESTful API (JSON)
┌─────────────────────▼───────────────────────────────┐
│                后端 (Spring Boot 3)                   │
│   Spring Security + JWT  │  JPA/Hibernate            │
│   Controllers → Services → Repositories              │
└─────────────────────┬───────────────────────────────┘
                      │ JDBC
┌─────────────────────▼───────────────────────────────┐
│                MySQL 8.0 数据库                       │
│   developers │ digital_assets │ subscriptions │ usage_logs │
└─────────────────────────────────────────────────────┘
```

---

## 第6页 - 技术实现亮点

### 1. 安全性
- Spring Security + JWT 无状态认证
- BCrypt 密码加密
- API 权限控制（角色隔离）

### 2. 数据库设计
- 规范化的表结构，3NF
- 外键 + 索引优化
- 唯一约束防止重复订阅

### 3. UI/UX
- Glassmorphism 毛玻璃效果
- 暗色主题科技感设计
- 响应式布局（移动端适配）

### 4. 数据可视化
- ECharts 实现饼图、柱状图、折线图
- 实时仪表盘数据

---

## 第7页 - 数据库关键 DDL

```sql
-- 订阅表：防重复订阅的唯一约束
UNIQUE KEY uk_sub_asset_user (subscriber_id, asset_id)

-- 外键级联
CONSTRAINT fk_assets_provider
  FOREIGN KEY (provider_id) REFERENCES developers(dev_id)
  ON DELETE CASCADE ON UPDATE CASCADE

-- 索引优化
INDEX idx_assets_provider (provider_id)
INDEX idx_assets_type (asset_type)
INDEX idx_logs_request_time (request_time)
```

---

## 第8页 - 创新点

1. **角色系统**：Provider / Subscriber 清晰分离，不同角色不同功能
2. **调用额度管理**：每笔订阅自动分配调用配额，跟踪使用情况
3. **防重复订阅**：数据库 UNIQUE 约束 + 业务层校验双重保障
4. **现代 UI**：SaaS 风格暗色玻璃拟态界面，参考 Vercel、Linear 设计
5. **完整全栈**：从数据库到前端的一体化实现

---

## 第9页 - 项目总结

### 完成情况
- ✅ 数据库设计与实现（4表 + 索引 + 外键 + 测试数据）
- ✅ RESTful API（认证、资产、订阅、仪表盘）
- ✅ 前端页面（登录、注册、首页、市场、详情、发布、订阅、仪表盘、个人中心）
- ✅ JWT 认证与权限管理
- ✅ 数据可视化仪表盘
- ✅ Glassmorphism 暗色 UI

### 技术收获
- 掌握了 Spring Boot 全栈开发流程
- 深入理解了数据库设计与优化
- 实践了前后端分离架构
- 学习了 JWT 认证机制
- 应用了 ECharts 数据可视化

---

## 第10页 - Q&A

感谢聆听，欢迎提问！
