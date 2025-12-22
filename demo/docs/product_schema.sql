-- 通用产品表
CREATE TABLE IF NOT EXISTS `product` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `packcode` varchar(50) NOT NULL COMMENT '产品包代码',
  `appid` varchar(100) NOT NULL COMMENT '应用ID',
  `tier` int(11) NOT NULL COMMENT '产品层级（数值越大层级越高，用于判断升降级）',
  `level` int(11) NOT NULL COMMENT '等级（同一tier内的等级，1-5等）',
  `name` varchar(100) NOT NULL COMMENT '产品名称',
  `description` varchar(500) DEFAULT NULL COMMENT '产品描述',
  `create_by` varchar(50) DEFAULT NULL COMMENT '创建人',
  `create_at` bigint(20) NOT NULL COMMENT '创建时间（时间戳）',
  `status` tinyint(1) NOT NULL DEFAULT '0' COMMENT '状态：0-正常 1-删除',
  `update_at` bigint(20) DEFAULT NULL COMMENT '修改时间（时间戳）',
  `update_by` varchar(50) DEFAULT NULL COMMENT '修改人',
  `version` int(11) NOT NULL DEFAULT '0' COMMENT '乐观锁版本号',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_packcode_appid` (`packcode`, `appid`),
  KEY `idx_appid` (`appid`),
  KEY `idx_tier` (`tier`),
  KEY `idx_level` (`level`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='产品表（通用）';

-- Stripe平台产品表
CREATE TABLE IF NOT EXISTS `stripe_product` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `product_id` bigint(20) NOT NULL COMMENT '关联的通用产品ID',
  `stripe_product_id` varchar(100) NOT NULL COMMENT 'Stripe平台的产品ID',
  `stripe_price_id` varchar(100) NOT NULL COMMENT 'Stripe平台的价格ID',
  `name` varchar(100) NOT NULL COMMENT '产品名称（Stripe平台）',
  `description` varchar(500) DEFAULT NULL COMMENT '产品描述（Stripe平台）',
  `unit_amount` bigint(20) NOT NULL COMMENT '价格（美分）',
  `currency` varchar(10) NOT NULL DEFAULT 'usd' COMMENT '货币类型',
  `interval` varchar(20) NOT NULL COMMENT '计费周期：month/year',
  `create_by` varchar(50) DEFAULT NULL COMMENT '创建人',
  `create_at` bigint(20) NOT NULL COMMENT '创建时间（时间戳）',
  `status` tinyint(1) NOT NULL DEFAULT '0' COMMENT '状态：0-正常 1-删除',
  `update_at` bigint(20) DEFAULT NULL COMMENT '修改时间（时间戳）',
  `update_by` varchar(50) DEFAULT NULL COMMENT '修改人',
  `version` int(11) NOT NULL DEFAULT '0' COMMENT '乐观锁版本号',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_stripe_product_id` (`stripe_product_id`),
  UNIQUE KEY `uk_stripe_price_id` (`stripe_price_id`),
  KEY `idx_product_id` (`product_id`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Stripe平台产品表';
