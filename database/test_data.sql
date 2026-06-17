-- ============================================================================
-- DataHub 测试数据
-- ============================================================================
USE datahub;

-- 演示账号密码均为 password123
INSERT INTO users (username, password_hash, email, api_key, role, avatar, created_at) VALUES
('zhangsan', '$2a$10$MZHBk1EsPGzMOZaefNDHv.kJTs7FdSxF6yGf3b5KxAzZsaLYFDNr.', 'zhangsan@datahub.io', 'dh_key_prov_001', 'PROVIDER', 'https://api.dicebear.com/7.x/initials/svg?seed=ZS', '2026-01-15 08:00:00'),
('lisi', '$2a$10$MZHBk1EsPGzMOZaefNDHv.kJTs7FdSxF6yGf3b5KxAzZsaLYFDNr.', 'lisi@datahub.io', 'dh_key_prov_002', 'PROVIDER', 'https://api.dicebear.com/7.x/initials/svg?seed=LS', '2026-02-10 09:30:00'),
('wangwu', '$2a$10$MZHBk1EsPGzMOZaefNDHv.kJTs7FdSxF6yGf3b5KxAzZsaLYFDNr.', 'wangwu@datahub.io', 'dh_key_prov_003', 'PROVIDER', 'https://api.dicebear.com/7.x/initials/svg?seed=WW', '2026-02-20 10:00:00'),
('sub_a', '$2a$10$MZHBk1EsPGzMOZaefNDHv.kJTs7FdSxF6yGf3b5KxAzZsaLYFDNr.', 'sub_a@datahub.io', 'dh_key_sub_001', 'SUBSCRIBER', 'https://api.dicebear.com/7.x/initials/svg?seed=SA', '2026-04-01 08:30:00'),
('sub_b', '$2a$10$MZHBk1EsPGzMOZaefNDHv.kJTs7FdSxF6yGf3b5KxAzZsaLYFDNr.', 'sub_b@datahub.io', 'dh_key_sub_002', 'SUBSCRIBER', 'https://api.dicebear.com/7.x/initials/svg?seed=SB', '2026-04-05 09:00:00');

INSERT INTO assets (provider_id, title, asset_type, description, endpoint_url, price_per_month, status, tags, created_at) VALUES
(1, '中国城市天气数据集', 'DATASET', '包含中国300+城市过去5年的天气数据，包括温度、湿度、降雨量、风速等指标，CSV格式，每日更新。', 'https://api.datahub.io/v1/datasets/weather-china', 29.90, 'ACTIVE', '天气,气象,中国,CSV', '2026-01-20 09:00:00'),
(1, '新闻情感分析API', 'API', '基于深度学习的新闻文本情感分析接口，支持中英文，返回正面、负面、中性分类及置信度。', 'https://api.datahub.io/v1/api/sentiment', 49.90, 'ACTIVE', 'NLP,情感分析,AI,API', '2026-02-01 10:00:00'),
(2, '地图地理编码API', 'API', '地址转经纬度和逆地理编码服务，覆盖全球，支持批量查询。', 'https://api.datahub.io/v1/api/geocoding', 59.90, 'ACTIVE', '地图,地理编码,LBS,API', '2026-03-10 10:00:00'),
(3, '股票市场数据集', 'DATASET', 'A股市场历史日线数据，包含开高低收、成交量、换手率等指标，适合量化分析。', 'https://api.datahub.io/v1/datasets/stock-market', 89.90, 'ACTIVE', '股票,金融,量化,CSV', '2026-03-20 16:00:00');

INSERT INTO subscriptions (subscriber_id, asset_id, quota_limit, quota_used, start_date, end_date, status, created_at) VALUES
(4, 1, 1000, 342, '2026-04-15', '2026-07-15', 'ACTIVE', '2026-04-15 09:00:00'),
(4, 2, 2000, 891, '2026-04-16', '2026-07-16', 'ACTIVE', '2026-04-16 10:00:00'),
(5, 1, 1000, 678, '2026-04-10', '2026-07-10', 'ACTIVE', '2026-04-10 08:30:00');

INSERT INTO usage_logs (sub_id, request_time, status_code, response_time, ip_address) VALUES
(1, '2026-06-01 09:15:30', 200, 45, '192.168.1.100'),
(1, '2026-06-02 10:22:15', 200, 52, '192.168.1.100'),
(1, '2026-06-03 11:10:05', 200, 41, '192.168.1.101'),
(1, '2026-06-04 09:55:33', 500, 120, '192.168.1.103'),
(2, '2026-06-05 08:00:42', 200, 33, '192.168.1.104'),
(3, '2026-06-06 13:35:27', 200, 48, '192.168.1.100');
