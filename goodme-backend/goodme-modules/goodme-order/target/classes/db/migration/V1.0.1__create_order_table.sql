CREATE TABLE `order` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `order_no` VARCHAR(32) NOT NULL COMMENT '订单号',
  `user_id` BIGINT(20) NOT NULL COMMENT '用户ID',
  `shop_id` BIGINT(20) NOT NULL COMMENT '店铺ID',
  `total_amount` DECIMAL(10,2) NOT NULL COMMENT '订单总金额',
  `status` VARCHAR(20) NOT NULL COMMENT '订单状态',
  `pickup_code` VARCHAR(10) DEFAULT NULL COMMENT '取餐码',
  `pay_type` VARCHAR(10) DEFAULT NULL COMMENT '支付方式',
  `pay_time` DATETIME DEFAULT NULL COMMENT '支付时间',
  `cancel_time` DATETIME DEFAULT NULL COMMENT '取消时间',
  `remark` VARCHAR(255) DEFAULT NULL COMMENT '备注',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_order_no` (`order_no`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_shop_id` (`shop_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单表'; 