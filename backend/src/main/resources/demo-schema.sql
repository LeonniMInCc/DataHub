CREATE TABLE users (
  user_id BIGINT AUTO_INCREMENT PRIMARY KEY,
  username VARCHAR(50) NOT NULL UNIQUE,
  password_hash VARCHAR(255) NOT NULL,
  email VARCHAR(100) NOT NULL UNIQUE,
  api_key VARCHAR(64) UNIQUE,
  role VARCHAR(20) NOT NULL DEFAULT 'SUBSCRIBER',
  avatar VARCHAR(500) DEFAULT NULL,
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_users_username ON users (username);
CREATE INDEX idx_users_email ON users (email);
CREATE INDEX idx_users_role ON users (role);
CREATE INDEX idx_users_api_key ON users (api_key);

CREATE TABLE assets (
  asset_id BIGINT AUTO_INCREMENT PRIMARY KEY,
  provider_id BIGINT NOT NULL,
  title VARCHAR(200) NOT NULL,
  asset_type VARCHAR(20) NOT NULL,
  description VARCHAR(4000) DEFAULT NULL,
  endpoint_url VARCHAR(500) DEFAULT NULL,
  price_per_month DECIMAL(10,2) NOT NULL DEFAULT 0.00,
  status VARCHAR(20) NOT NULL DEFAULT 'PENDING_PAYMENT',
  tags VARCHAR(500) DEFAULT NULL,
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT fk_assets_provider FOREIGN KEY (provider_id) REFERENCES users(user_id)
);

CREATE INDEX idx_assets_provider ON assets (provider_id);
CREATE INDEX idx_assets_type ON assets (asset_type);
CREATE INDEX idx_assets_status ON assets (status);
CREATE INDEX idx_assets_title ON assets (title);
CREATE INDEX idx_assets_tags ON assets (tags);

CREATE TABLE subscriptions (
  sub_id BIGINT AUTO_INCREMENT PRIMARY KEY,
  subscriber_id BIGINT NOT NULL,
  asset_id BIGINT NOT NULL,
  quota_limit INT NOT NULL DEFAULT 1000,
  quota_used INT NOT NULL DEFAULT 0,
  start_date DATE NOT NULL,
  end_date DATE NOT NULL,
  status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT uk_sub_asset_user UNIQUE (subscriber_id, asset_id),
  CONSTRAINT fk_sub_subscriber FOREIGN KEY (subscriber_id) REFERENCES users(user_id),
  CONSTRAINT fk_sub_asset FOREIGN KEY (asset_id) REFERENCES assets(asset_id)
);

CREATE INDEX idx_sub_subscriber ON subscriptions (subscriber_id);
CREATE INDEX idx_sub_asset ON subscriptions (asset_id);
CREATE INDEX idx_sub_status ON subscriptions (status);
CREATE INDEX idx_sub_dates ON subscriptions (start_date, end_date);

CREATE TABLE usage_logs (
  log_id BIGINT AUTO_INCREMENT PRIMARY KEY,
  sub_id BIGINT NOT NULL,
  request_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  status_code INT NOT NULL DEFAULT 200,
  response_time INT NOT NULL DEFAULT 0,
  ip_address VARCHAR(45) DEFAULT NULL,
  CONSTRAINT fk_logs_sub FOREIGN KEY (sub_id) REFERENCES subscriptions(sub_id)
);

CREATE INDEX idx_logs_sub ON usage_logs (sub_id);
CREATE INDEX idx_logs_request_time ON usage_logs (request_time);
CREATE INDEX idx_logs_status ON usage_logs (status_code);
