-- 客户表
-- Customer table
CREATE TABLE IF NOT EXISTS `customer` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID / Primary key ID',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID / User ID',
  `stripe_customer_id` varchar(100) NOT NULL COMMENT 'Stripe客户ID / Stripe customer ID',
  `email` varchar(100) NOT NULL COMMENT '客户邮箱 / Customer email',
  `name` varchar(100) DEFAULT NULL COMMENT '客户名称 / Customer name',
  `phone` varchar(50) DEFAULT NULL COMMENT '电话号码 / Phone number',
  `address` varchar(500) DEFAULT NULL COMMENT '地址 / Address',
  `default_payment_method` varchar(100) DEFAULT NULL COMMENT '默认支付方式ID / Default payment method ID',
  `create_by` varchar(50) DEFAULT NULL COMMENT '创建人 / Creator',
  `create_at` bigint(20) NOT NULL COMMENT '创建时间（时间戳） / Create time (timestamp)',
  `status` tinyint(1) NOT NULL DEFAULT '0' COMMENT '状态：0-正常 1-删除 / Status: 0-normal 1-deleted',
  `update_at` bigint(20) DEFAULT NULL COMMENT '修改时间（时间戳） / Update time (timestamp)',
  `update_by` varchar(50) DEFAULT NULL COMMENT '修改人 / Updater',
  `version` int(11) NOT NULL DEFAULT '0' COMMENT '乐观锁版本号 / Optimistic lock version',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_id` (`user_id`),
  UNIQUE KEY `uk_stripe_customer_id` (`stripe_customer_id`),
  KEY `idx_email` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='客户表 / Customer table';

-- 插入测试数据
-- Insert test data
INSERT INTO `customer` (`user_id`, `stripe_customer_id`, `email`, `name`, `phone`, `create_by`, `create_at`) VALUES
(1, 'cus_test_001', 'user1@example.com', 'Test User 1', '13800138001', 'admin', UNIX_TIMESTAMP() * 1000),
(2, 'cus_test_002', 'user2@example.com', 'Test User 2', '13800138002', 'admin', UNIX_TIMESTAMP() * 1000);
