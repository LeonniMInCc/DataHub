# DataHub ER 图

```
┌──────────────────────────────────────────────────────────────┐
│                       developers                             │
├──────────────────────────────────────────────────────────────┤
│ PK  dev_id        BIGINT          AUTO_INCREMENT             │
│     username      VARCHAR(50)     NOT NULL UNIQUE             │
│     password_hash VARCHAR(255)    NOT NULL                    │
│     email         VARCHAR(100)    NOT NULL UNIQUE             │
│     api_key       VARCHAR(64)     UNIQUE                      │
│     role          ENUM('PROVIDER','SUBSCRIBER') NOT NULL      │
│     avatar        VARCHAR(500)                                │
│     created_at    DATETIME        NOT NULL DEFAULT NOW()      │
├──────────────────────────────────────────────────────────────┤
│ INDEX: idx_developers_username (username)                     │
│ INDEX: idx_developers_email (email)                           │
│ INDEX: idx_developers_role (role)                             │
│ INDEX: idx_developers_api_key (api_key)                       │
└────────────┬─────────────────────────────────────┬───────────┘
             │ (1:N, provider_id)                  │ (1:N, subscriber_id)
             │                                     │
             ▼                                     ▼
┌──────────────────────────────────────┐ ┌──────────────────────────────────────┐
│           digital_assets             │ │           subscriptions               │
├──────────────────────────────────────┤ ├──────────────────────────────────────┤
│ PK  asset_id      BIGINT AUTO_INC   │ │ PK  sub_id        BIGINT AUTO_INC    │
│ FK  provider_id   BIGINT NOT NULL   │ │ FK  subscriber_id BIGINT NOT NULL     │
│     title         VARCHAR(200)      │ │ FK  asset_id      BIGINT NOT NULL     │
│     asset_type    ENUM(DATASET,API) │ │     quota_limit   INT DEFAULT 1000    │
│     description   TEXT              │ │     quota_used    INT DEFAULT 0       │
│     endpoint_url  VARCHAR(500)      │ │     start_date    DATE NOT NULL       │
│     price_per_mo  DECIMAL(10,2)     │ │     end_date      DATE NOT NULL       │
│     status ENUM(ACTIVE,INACTIVE,    │ │     status ENUM(ACTIVE,EXPIRED,       │
│                  DELETED)           │ │                  CANCELLED)           │
│     tags          VARCHAR(500)      │ │     created_at    DATETIME            │
│     created_at    DATETIME          │ ├──────────────────────────────────────┤
├──────────────────────────────────────┤ │ UNIQUE: uk_sub_asset_user            │
│ FK: fk_assets_provider → developers  │ │        (subscriber_id, asset_id)      │
│     ON DELETE CASCADE               │ │ FK: fk_sub_subscriber → developers    │
│ INDEX: provider_id, asset_type,     │ │ FK: fk_sub_asset → digital_assets     │
│        status, title, tags           │ │ INDEX: subscriber_id, asset_id,       │
└──────────────┬───────────────────────┘ │        status, start_date, end_date   │
               │ (1:N, asset_id)         └──────────────┬───────────────────────┘
               │                                        │ (1:N, sub_id)
               │                                        │
               ▼                                        ▼
                                  ┌──────────────────────────────────────┐
                                  │            usage_logs                │
                                  ├──────────────────────────────────────┤
                                  │ PK  log_id        BIGINT AUTO_INC   │
                                  │ FK  sub_id        BIGINT NOT NULL   │
                                  │     request_time  DATETIME NOT NULL  │
                                  │     status_code   INT DEFAULT 200    │
                                  │     response_time INT DEFAULT 0      │
                                  │     ip_address    VARCHAR(45)        │
                                  ├──────────────────────────────────────┤
                                  │ FK: fk_logs_sub → subscriptions      │
                                  │     ON DELETE CASCADE                │
                                  │ INDEX: sub_id, request_time,         │
                                  │        status_code                   │
                                  └──────────────────────────────────────┘
```

## 关系说明

| 关系 | 类型 | 说明 |
|------|------|------|
| developers → digital_assets | 1:N | 一个 Provider 可以发布多个资产 |
| developers → subscriptions | 1:N | 一个 Subscriber 可以有多个订阅 |
| digital_assets → subscriptions | 1:N | 一个资产可以被多人订阅 |
| subscriptions → usage_logs | 1:N | 一个订阅可以产生多条调用日志 |

## 关键约束

- `subscriptions.uk_sub_asset_user`: 同一用户不能重复订阅同一资产
- 所有外键设定 `ON DELETE CASCADE`，保证数据一致性
- `digital_assets.status`: ACTIVE / INACTIVE / DELETED（软删除）
- `subscriptions.status`: ACTIVE / EXPIRED / CANCELLED（状态追踪）
