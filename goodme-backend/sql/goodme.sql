-- 创建数据库
CREATE DATABASE IF NOT EXISTS goodme DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;

USE goodme;

-- 用户表
CREATE TABLE IF NOT EXISTS `user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `openid` varchar(64) DEFAULT NULL COMMENT '微信openid',
  `phone` varchar(20) DEFAULT NULL COMMENT '手机号',
  `password` varchar(64) DEFAULT NULL COMMENT '密码（加密存储）',
  `nickname` varchar(50) DEFAULT NULL COMMENT '昵称',
  `avatar` varchar(255) DEFAULT NULL COMMENT '头像URL',
  `gender` tinyint(1) DEFAULT '0' COMMENT '性别：0未知，1男，2女',
  `birthday` date DEFAULT NULL COMMENT '生日',
  `member_level_id` bigint(20) DEFAULT '1' COMMENT '会员等级ID',
  `points` int(11) DEFAULT '0' COMMENT '积分',
  `status` tinyint(1) DEFAULT '1' COMMENT '状态：0禁用，1正常',
  `last_login_time` datetime DEFAULT NULL COMMENT '最后登录时间',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint(1) DEFAULT '0' COMMENT '逻辑删除：0未删除，1已删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_openid` (`openid`),
  UNIQUE KEY `idx_phone` (`phone`),
  KEY `idx_member_level` (`member_level_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- 用户地址表
CREATE TABLE IF NOT EXISTS `user_address` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '地址ID',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `receiver_name` varchar(50) NOT NULL COMMENT '收货人姓名',
  `receiver_phone` varchar(20) NOT NULL COMMENT '收货人手机号',
  `province` varchar(20) DEFAULT NULL COMMENT '省份',
  `city` varchar(20) DEFAULT NULL COMMENT '城市',
  `district` varchar(20) DEFAULT NULL COMMENT '区县',
  `detail_address` varchar(200) NOT NULL COMMENT '详细地址',
  `is_default` tinyint(1) DEFAULT '0' COMMENT '是否默认：0否，1是',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint(1) DEFAULT '0' COMMENT '逻辑删除：0未删除，1已删除',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户地址表';

-- 商品分类表
CREATE TABLE IF NOT EXISTS `category` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '分类ID',
  `name` varchar(50) NOT NULL COMMENT '分类名称',
  `image` varchar(255) DEFAULT NULL COMMENT '分类图片',
  `parent_id` bigint(20) DEFAULT '0' COMMENT '父分类ID，0表示一级分类',
  `level` tinyint(1) DEFAULT '1' COMMENT '分类层级：1一级，2二级',
  `sort` int(11) DEFAULT '0' COMMENT '排序值，越小越靠前',
  `status` tinyint(1) DEFAULT '1' COMMENT '状态：0禁用，1启用',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint(1) DEFAULT '0' COMMENT '逻辑删除：0未删除，1已删除',
  PRIMARY KEY (`id`),
  KEY `idx_parent_id` (`parent_id`),
  KEY `idx_sort` (`sort`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品分类表';

-- 商品表
CREATE TABLE IF NOT EXISTS `product` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '商品ID',
  `name` varchar(100) NOT NULL COMMENT '商品名称',
  `category_id` bigint(20) NOT NULL COMMENT '分类ID',
  `thumbnail` varchar(255) DEFAULT NULL COMMENT '缩略图',
  `images` varchar(1000) DEFAULT NULL COMMENT '商品图片，多个图片用逗号分隔',
  `description` text COMMENT '商品描述',
  `price` decimal(10,2) NOT NULL COMMENT '商品价格',
  `stock` int(11) DEFAULT '0' COMMENT '库存',
  `stock_warning` int(11) DEFAULT '10' COMMENT '库存预警值',
  `sales` int(11) DEFAULT '0' COMMENT '销量',
  `unit` varchar(10) DEFAULT NULL COMMENT '单位',
  `weight` decimal(10,2) DEFAULT NULL COMMENT '重量(克)',
  `status` tinyint(1) DEFAULT '1' COMMENT '状态：0下架，1上架',
  `is_new` tinyint(1) DEFAULT '0' COMMENT '是否新品：0否，1是',
  `is_hot` tinyint(1) DEFAULT '0' COMMENT '是否热销：0否，1是',
  `sort` int(11) DEFAULT '0' COMMENT '排序值，越小越靠前',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint(1) DEFAULT '0' COMMENT '逻辑删除：0未删除，1已删除',
  PRIMARY KEY (`id`),
  KEY `idx_category_id` (`category_id`),
  KEY `idx_status` (`status`),
  KEY `idx_sort` (`sort`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品表';

-- 商品规格表
CREATE TABLE IF NOT EXISTS `product_spec` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '规格ID',
  `name` varchar(50) NOT NULL COMMENT '规格名称（如：杯型、温度、甜度、加料）',
  `sort` int(11) DEFAULT '0' COMMENT '排序值',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint(1) DEFAULT '0' COMMENT '逻辑删除：0未删除，1已删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品规格表';

-- 规格值表
CREATE TABLE IF NOT EXISTS `product_spec_value` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '规格值ID',
  `product_id` bigint(20) NOT NULL COMMENT '商品ID',
  `spec_id` bigint(20) NOT NULL COMMENT '规格ID',
  `value` varchar(50) NOT NULL COMMENT '规格值（如：中杯、常温、全糖、珍珠）',
  `extra_price` decimal(10,2) DEFAULT '0.00' COMMENT '额外价格',
  `is_default` tinyint(1) DEFAULT '0' COMMENT '是否默认：0否，1是',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint(1) DEFAULT '0' COMMENT '逻辑删除：0未删除，1已删除',
  PRIMARY KEY (`id`),
  KEY `idx_product_spec` (`product_id`,`spec_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品规格值表';

-- 订单表
CREATE TABLE IF NOT EXISTS `order` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '订单ID',
  `order_no` varchar(32) NOT NULL COMMENT '订单编号',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `shop_id` bigint(20) NOT NULL COMMENT '店铺ID',
  `order_type` tinyint(1) DEFAULT '1' COMMENT '订单类型：1自取，2外卖',
  `total_amount` decimal(10,2) NOT NULL COMMENT '订单总金额',
  `pay_amount` decimal(10,2) NOT NULL COMMENT '实付金额',
  `discount_amount` decimal(10,2) DEFAULT '0.00' COMMENT '优惠金额',
  `coupon_id` bigint(20) DEFAULT NULL COMMENT '优惠券ID',
  `pay_type` tinyint(1) DEFAULT NULL COMMENT '支付方式：1微信，2支付宝',
  `pay_time` datetime DEFAULT NULL COMMENT '支付时间',
  `pay_transaction_id` varchar(64) DEFAULT NULL COMMENT '支付交易号',
  `order_status` tinyint(1) DEFAULT '0' COMMENT '订单状态：0待支付，1已支付，2制作中，3待取餐，4已完成，5已取消',
  `cancel_reason` varchar(200) DEFAULT NULL COMMENT '取消原因',
  `remark` varchar(200) DEFAULT NULL COMMENT '订单备注',
  `pickup_code` varchar(10) DEFAULT NULL COMMENT '取餐码',
  `pickup_time` datetime DEFAULT NULL COMMENT '预约取餐时间',
  `delivery_fee` decimal(10,2) DEFAULT '0.00' COMMENT '配送费',
  `address_id` bigint(20) DEFAULT NULL COMMENT '收货地址ID',
  `receiver_name` varchar(50) DEFAULT NULL COMMENT '收货人姓名',
  `receiver_phone` varchar(20) DEFAULT NULL COMMENT '收货人手机号',
  `receiver_address` varchar(255) DEFAULT NULL COMMENT '收货地址',
  `expire_time` datetime DEFAULT NULL COMMENT '订单过期时间',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint(1) DEFAULT '0' COMMENT '逻辑删除：0未删除，1已删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_order_no` (`order_no`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_shop_id` (`shop_id`),
  KEY `idx_order_status` (`order_status`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单表';

-- 订单明细表
CREATE TABLE IF NOT EXISTS `order_item` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '订单项ID',
  `order_id` bigint(20) NOT NULL COMMENT '订单ID',
  `product_id` bigint(20) NOT NULL COMMENT '商品ID',
  `product_name` varchar(100) NOT NULL COMMENT '商品名称',
  `product_image` varchar(255) DEFAULT NULL COMMENT '商品图片',
  `price` decimal(10,2) NOT NULL COMMENT '商品单价',
  `quantity` int(11) NOT NULL COMMENT '购买数量',
  `spec_json` varchar(500) DEFAULT NULL COMMENT '规格JSON字符串',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint(1) DEFAULT '0' COMMENT '逻辑删除：0未删除，1已删除',
  PRIMARY KEY (`id`),
  KEY `idx_order_id` (`order_id`),
  KEY `idx_product_id` (`product_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单明细表';

-- 会员等级表
CREATE TABLE IF NOT EXISTS `member_level` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '等级ID',
  `name` varchar(50) NOT NULL COMMENT '等级名称',
  `icon` varchar(255) DEFAULT NULL COMMENT '等级图标',
  `min_points` int(11) NOT NULL COMMENT '最小积分',
  `max_points` int(11) DEFAULT NULL COMMENT '最大积分',
  `discount` decimal(3,2) DEFAULT '1.00' COMMENT '折扣率',
  `description` varchar(500) DEFAULT NULL COMMENT '等级描述',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint(1) DEFAULT '0' COMMENT '逻辑删除：0未删除，1已删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='会员等级表';

-- 积分记录表
CREATE TABLE IF NOT EXISTS `member_points_record` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '记录ID',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `points` int(11) NOT NULL COMMENT '积分变动值',
  `type` tinyint(1) NOT NULL COMMENT '类型：1消费获得，2签到，3活动，4兑换消耗，5过期',
  `source_id` varchar(64) DEFAULT NULL COMMENT '来源ID（如订单ID）',
  `description` varchar(200) DEFAULT NULL COMMENT '描述',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `deleted` tinyint(1) DEFAULT '0' COMMENT '逻辑删除：0未删除，1已删除',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='积分记录表';

-- 优惠券表
CREATE TABLE IF NOT EXISTS `coupon` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '优惠券ID',
  `name` varchar(50) NOT NULL COMMENT '优惠券名称',
  `type` tinyint(1) NOT NULL COMMENT '类型：1满减，2折扣',
  `amount` decimal(10,2) DEFAULT NULL COMMENT '优惠金额（满减类型）',
  `discount` decimal(3,2) DEFAULT NULL COMMENT '折扣率（折扣类型）',
  `min_amount` decimal(10,2) DEFAULT '0.00' COMMENT '最低消费金额',
  `start_time` datetime NOT NULL COMMENT '生效时间',
  `end_time` datetime NOT NULL COMMENT '失效时间',
  `total` int(11) DEFAULT '-1' COMMENT '发行总量，-1表示无限制',
  `remain` int(11) DEFAULT '-1' COMMENT '剩余数量',
  `limit_per_user` int(11) DEFAULT '1' COMMENT '每人限领数量',
  `status` tinyint(1) DEFAULT '1' COMMENT '状态：0禁用，1启用',
  `description` varchar(500) DEFAULT NULL COMMENT '使用说明',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint(1) DEFAULT '0' COMMENT '逻辑删除：0未删除，1已删除',
  PRIMARY KEY (`id`),
  KEY `idx_status` (`status`),
  KEY `idx_time` (`start_time`,`end_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='优惠券表';

-- 用户优惠券表
CREATE TABLE IF NOT EXISTS `user_coupon` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '记录ID',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `coupon_id` bigint(20) NOT NULL COMMENT '优惠券ID',
  `status` tinyint(1) NOT NULL DEFAULT '0' COMMENT '状态：0未使用，1已使用，2已过期',
  `order_id` bigint(20) DEFAULT NULL COMMENT '使用的订单ID',
  `get_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '领取时间',
  `use_time` datetime DEFAULT NULL COMMENT '使用时间',
  `deleted` tinyint(1) DEFAULT '0' COMMENT '逻辑删除：0未删除，1已删除',
  PRIMARY KEY (`id`),
  KEY `idx_user_coupon` (`user_id`,`coupon_id`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户优惠券表';

-- 促销活动表
CREATE TABLE IF NOT EXISTS `promotion` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '活动ID',
  `name` varchar(100) NOT NULL COMMENT '活动名称',
  `type` tinyint(1) NOT NULL COMMENT '活动类型：1拼团，2秒杀，3满减',
  `start_time` datetime NOT NULL COMMENT '开始时间',
  `end_time` datetime NOT NULL COMMENT '结束时间',
  `status` tinyint(1) DEFAULT '0' COMMENT '状态：0未开始，1进行中，2已结束，3已取消',
  `rule_json` text COMMENT '活动规则JSON',
  `description` varchar(500) DEFAULT NULL COMMENT '活动描述',
  `image` varchar(255) DEFAULT NULL COMMENT '活动图片',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint(1) DEFAULT '0' COMMENT '逻辑删除：0未删除，1已删除',
  PRIMARY KEY (`id`),
  KEY `idx_time` (`start_time`,`end_time`),
  KEY `idx_status` (`status`),
  KEY `idx_type` (`type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='促销活动表';

-- 店铺表
CREATE TABLE IF NOT EXISTS `shop` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '店铺ID',
  `name` varchar(100) NOT NULL COMMENT '店铺名称',
  `logo` varchar(255) DEFAULT NULL COMMENT '店铺logo',
  `contact_phone` varchar(20) DEFAULT NULL COMMENT '联系电话',
  `province` varchar(20) DEFAULT NULL COMMENT '省份',
  `city` varchar(20) DEFAULT NULL COMMENT '城市',
  `district` varchar(20) DEFAULT NULL COMMENT '区县',
  `address` varchar(200) NOT NULL COMMENT '详细地址',
  `longitude` decimal(10,6) DEFAULT NULL COMMENT '经度',
  `latitude` decimal(10,6) DEFAULT NULL COMMENT '纬度',
  `business_hours` varchar(100) DEFAULT NULL COMMENT '营业时间',
  `status` tinyint(1) DEFAULT '1' COMMENT '状态：0关闭，1营业中',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint(1) DEFAULT '0' COMMENT '逻辑删除：0未删除，1已删除',
  PRIMARY KEY (`id`),
  KEY `idx_location` (`city`,`district`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='店铺表';

-- 店铺商品表
CREATE TABLE IF NOT EXISTS `shop_product` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `shop_id` bigint(20) NOT NULL COMMENT '店铺ID',
  `product_id` bigint(20) NOT NULL COMMENT '商品ID',
  `stock` int(11) DEFAULT '0' COMMENT '库存',
  `stock_warning` int(11) DEFAULT '10' COMMENT '库存预警值',
  `status` tinyint(1) DEFAULT '1' COMMENT '状态：0下架，1上架',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint(1) DEFAULT '0' COMMENT '逻辑删除：0未删除，1已删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_shop_product` (`shop_id`,`product_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='店铺商品表';

-- 评价表
CREATE TABLE IF NOT EXISTS `comment` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '评价ID',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `order_id` bigint(20) NOT NULL COMMENT '订单ID',
  `product_id` bigint(20) NOT NULL COMMENT '商品ID',
  `shop_id` bigint(20) NOT NULL COMMENT '店铺ID',
  `content` text COMMENT '评价内容',
  `images` varchar(1000) DEFAULT NULL COMMENT '评价图片，多个图片用逗号分隔',
  `score` tinyint(1) NOT NULL DEFAULT '5' COMMENT '评分：1-5',
  `taste_score` tinyint(1) DEFAULT '5' COMMENT '口味评分',
  `service_score` tinyint(1) DEFAULT '5' COMMENT '服务评分',
  `reply` text COMMENT '商家回复',
  `reply_time` datetime DEFAULT NULL COMMENT '回复时间',
  `status` tinyint(1) DEFAULT '1' COMMENT '状态：0隐藏，1显示',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint(1) DEFAULT '0' COMMENT '逻辑删除：0未删除，1已删除',
  PRIMARY KEY (`id`),
  KEY `idx_product_id` (`product_id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_order_id` (`order_id`),
  KEY `idx_shop_id` (`shop_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='评价表';

-- 初始化会员等级数据
INSERT INTO `member_level` (`id`, `name`, `icon`, `min_points`, `max_points`, `discount`, `description`) VALUES
(1, '普通会员', 'https://example.com/icons/level1.png', 0, 999, 1.00, '普通会员，无折扣'),
(2, '银卡会员', 'https://example.com/icons/level2.png', 1000, 4999, 0.98, '银卡会员，享受98折优惠'),
(3, '金卡会员', 'https://example.com/icons/level3.png', 5000, 9999, 0.95, '金卡会员，享受95折优惠'),
(4, '钻石会员', 'https://example.com/icons/level4.png', 10000, NULL, 0.90, '钻石会员，享受9折优惠');

-- 初始化商品规格数据
INSERT INTO `product_spec` (`id`, `name`, `sort`) VALUES
(1, '杯型', 1),
(2, '温度', 2),
(3, '甜度', 3),
(4, '加料', 4); 