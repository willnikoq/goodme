CREATE TABLE `order_item` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `order_id` BIGINT(20) NOT NULL COMMENT '订单ID',
  `product_id` BIGINT(20) NOT NULL COMMENT '商品ID',
  `product_name` VARCHAR(100) NOT NULL COMMENT '商品名称',
  `product_image` VARCHAR(255) DEFAULT NULL COMMENT '商品图片',
  `sku_name` VARCHAR(100) DEFAULT NULL COMMENT '规格名称',
  `spec_json` VARCHAR(500) DEFAULT NULL COMMENT '规格JSON',
  `price` DECIMAL(10,2) NOT NULL COMMENT '单价',
  `quantity` INT(11) NOT NULL COMMENT '数量',
  `total_price` DECIMAL(10,2) NOT NULL COMMENT '小计',
  PRIMARY KEY (`id`),
  KEY `idx_order_id` (`order_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单商品明细表'; 