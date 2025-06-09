ALTER TABLE `cart_item`
ADD COLUMN `shop_id` bigint(20) NOT NULL COMMENT '店铺ID' AFTER `user_id`,
ADD COLUMN `spec_json` varchar(500) DEFAULT NULL COMMENT '规格JSON字符串' AFTER `sku_id`,
ADD KEY `idx_shop_id` (`shop_id`); 