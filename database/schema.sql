-- ============================================================================
-- DataHub - 开发者数字资产共享平台 数据库设计
-- MySQL 8.0
-- ============================================================================

CREATE DATABASE IF NOT EXISTS datahub
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;

USE datahub;

-- ============================
-- 1. 用户表 (users)
-- ============================
CREATE TABLE users (
  user_id     BIGINT AUTO_INCREMENT PRIMARY KEY,
  username    VARCHAR(50)  NOT NULL UNIQUE,
  password_hash VARCHAR(255) NOT NULL,
  email       VARCHAR(100) NOT NULL UNIQUE,
  api_key     VARCHAR(64)  UNIQUE,
  role        ENUM('PROVIDER', 'SUBSCRIBER') NOT NULL DEFAULT 'SUBSCRIBER',
  avatar      VARCHAR(500) DEFAULT NULL,
  created_at  DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,

  INDEX idx_users_username (username),
  INDEX idx_users_email (email),
  INDEX idx_users_role (role),
  INDEX idx_users_api_key (api_key)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================
-- 2. 资产表 (assets)
-- ============================
CREATE TABLE assets (
  asset_id      BIGINT AUTO_INCREMENT PRIMARY KEY,
  provider_id   BIGINT       NOT NULL,
  title         VARCHAR(200) NOT NULL,
  asset_type    ENUM('DATASET', 'API') NOT NULL,
  description   TEXT         DEFAULT NULL,
  endpoint_url  VARCHAR(500) DEFAULT NULL,
  price_per_month DECIMAL(10,2) NOT NULL DEFAULT 0.00,
  status        ENUM('ACTIVE', 'INACTIVE', 'DELETED') NOT NULL DEFAULT 'ACTIVE',
  tags          VARCHAR(500) DEFAULT NULL,
  created_at    DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,

  INDEX idx_assets_provider (provider_id),
  INDEX idx_assets_type (asset_type),
  INDEX idx_assets_status (status),
  INDEX idx_assets_title (title),
  INDEX idx_assets_tags (tags),

  CONSTRAINT fk_assets_provider
    FOREIGN KEY (provider_id) REFERENCES users(user_id)
    ON DELETE CASCADE
    ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================
-- 3. 订阅表 (subscriptions)
-- ============================
CREATE TABLE subscriptions (
  sub_id        BIGINT AUTO_INCREMENT PRIMARY KEY,
  subscriber_id BIGINT   NOT NULL,
  asset_id      BIGINT   NOT NULL,
  quota_limit   INT      NOT NULL DEFAULT 1000,
  quota_used    INT      NOT NULL DEFAULT 0,
  start_date    DATE     NOT NULL,
  end_date      DATE     NOT NULL,
  status        ENUM('PENDING_PAYMENT', 'ACTIVE', 'FAILED', 'EXPIRED', 'CANCELLED') NOT NULL DEFAULT 'PENDING_PAYMENT',
  created_at    DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,

  INDEX idx_sub_subscriber (subscriber_id),
  INDEX idx_sub_asset (asset_id),
  INDEX idx_sub_status (status),
  INDEX idx_sub_dates (start_date, end_date),

  UNIQUE KEY uk_sub_asset_user (subscriber_id, asset_id),

  CONSTRAINT fk_sub_subscriber
    FOREIGN KEY (subscriber_id) REFERENCES users(user_id)
    ON DELETE CASCADE
    ON UPDATE CASCADE,

  CONSTRAINT fk_sub_asset
    FOREIGN KEY (asset_id) REFERENCES assets(asset_id)
    ON DELETE CASCADE
    ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================
-- 4. 调用日志表 (usage_logs)
-- ============================
CREATE TABLE usage_logs (
  log_id        BIGINT AUTO_INCREMENT PRIMARY KEY,
  sub_id        BIGINT   NOT NULL,
  request_time  DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  status_code   INT      NOT NULL DEFAULT 200,
  response_time INT      NOT NULL DEFAULT 0 COMMENT '响应时间(毫秒)',
  ip_address    VARCHAR(45) DEFAULT NULL,

  INDEX idx_logs_sub (sub_id),
  INDEX idx_logs_request_time (request_time),
  INDEX idx_logs_status (status_code),

  CONSTRAINT fk_logs_sub
    FOREIGN KEY (sub_id) REFERENCES subscriptions(sub_id)
    ON DELETE CASCADE
    ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
