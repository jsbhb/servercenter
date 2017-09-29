drop SCHEMA if exists  `zm_order`;
CREATE SCHEMA `zm_order` ;


use zm_order;



drop table if exists  `zm_order`.`order_base`;

CREATE TABLE `zm_order`.`order_base` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `order_id` CHAR(21) NOT NULL,
  `combination_id` CHAR(20) NULL COMMENT '订单拆分后的总ID',
  `user_id` INT UNSIGNED NOT NULL,
  `order_flag` TINYINT UNSIGNED NOT NULL COMMENT '0:跨境；1一般贸易',
  `express_type` TINYINT UNSIGNED NOT NULL COMMENT '0：快递；1：自提',
  `status` TINYINT UNSIGNED NULL DEFAULT 0 COMMENT '0：初始；1：已付款;2：已发仓库；3：已报海关；4：单证放行；5：已发货；6：已收货；7：退单;8、超时取消',
  `center_id` INT UNSIGNED NULL COMMENT '区域中心ID',
  `shop_id` INT UNSIGNED NULL COMMENT '单店ID',
  `guide_id` INT UNSIGNED NULL COMMENT '导购ID',
  `supplier_id` INT UNSIGNED NULL,
  `tdq` TINYINT UNSIGNED NOT NULL DEFAULT 0,
  `carrier_key` VARCHAR(50) NULL,
  `express_id` VARCHAR(50) NULL,
  `gtime` DATETIME NULL,
  `send_time` DATETIME NULL,
  `create_time` DATETIME NULL,
  `update_time` DATETIME NULL,
  `remark` VARCHAR(200) NULL,
  `is_del` TINYINT UNSIGNED NOT NULL DEFAULT 0 COMMENT '0:否；1是',
  PRIMARY KEY (`id`),
  UNIQUE INDEX `uk_orderId` (`order_id` ASC),
  INDEX `idx_orderId` (`order_id` ASC),
  INDEX `idx_combinationId` (`combination_id` ASC),
  INDEX `idx_userId` (`user_id` ASC),
  INDEX `idx_status` (`status` ASC),
  INDEX `idx_regionalCenterId` (`center_id` ASC),
  INDEX `idx_shopId` (`shop_id` ASC),
  INDEX `idx_supplierId` (`supplier_id` ASC),
  INDEX `idx_createTime` (`create_time` ASC),
  INDEX `idx_orderFlag` (`order_flag` ASC),
  INDEX `idx_is_del` (`is_del` ASC),
  INDEX `idx_expressId` (`express_id` ASC)
  )ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 
COMMENT = '订单基本信息';



drop table if exists  `zm_order`.`order_detail`;

CREATE TABLE `zm_order`.`order_detail` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `order_id` CHAR(21) NOT NULL, 
  `pay_type` TINYINT UNSIGNED NOT NULL COMMENT '1:微信；2支付宝',
  `payment` DECIMAL(10,2) NULL,
  `pay_time` DATETIME NULL,
  `post_fee` DECIMAL(10,2) NULL,
  `fax_fee` DECIMAL(10,2) NULL,
  `pay_no` VARCHAR(100) NULL,
  `custom_status` TINYINT UNSIGNED NULL DEFAULT 0 COMMENT '是否推送海关报关：0：否；1：是',
  `delivery_place` VARCHAR(50)  NULL,
  `carry_address` VARCHAR(200)  NULL COMMENT '自提地址',
  `receive_name` VARCHAR(50)  NULL,
  `receive_phone` CHAR(15)  NULL ,
  `receive_province` VARCHAR(30) NULL ,
  `receive_city` VARCHAR(30) NULL,
  `receive_area` VARCHAR(30) NULL,
  `receive_address` VARCHAR(100) NULL,
  `receive_zip_code` CHAR(6) NULL,
  `create_time` DATETIME NULL,
  `update_time` DATETIME NULL,
  `remark` VARCHAR(200) NULL,
  PRIMARY KEY (`id`),
  INDEX `idx_orderId` (`order_id` ASC),
  UNIQUE INDEX `uk_payNo` (`pay_no` ASC),
  UNIQUE INDEX `uk_orderId` (`order_id` ASC),
  INDEX `idx_customStatus` (`custom_status` ASC),
  INDEX `idx_deliveryPlace` (`delivery_place` ASC)
  )ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 
COMMENT = '订单详细信息';



drop table if exists  `zm_order`.`order_goods`;

CREATE TABLE `zm_order`.`order_goods` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `order_id` CHAR(21) NOT NULL,
  `item_id` VARCHAR(100) NOT NULL,
  `sku` VARCHAR(50) NULL,
  `item_name` VARCHAR(100) NULL,
  `item_img` VARCHAR(100) NULL,
  `item_info` VARCHAR(200) NULL,
  `item_code` VARCHAR(100) NULL,
  `item_quantity` INT UNSIGNED NOT NULL,
  `item_price` DECIMAL(10,2) NOT NULL,
  `actual_price` DECIMAL(10,2) NOT NULL,
  `create_time` DATETIME NULL,
  `update_time` DATETIME NULL,
  `remark` VARCHAR(200) NULL,
  PRIMARY KEY (`id`),
  INDEX `idx_orderId` (`order_id` ASC),
  INDEX `idx_itemId` (`item_id` ASC),
  INDEX `idx_sku` (`sku` ASC),
  INDEX `idx_itemCode` (`item_code` ASC)
  )ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 
COMMENT = '订单商品';




drop table if exists  `zm_order`.`customs_status`;

CREATE TABLE `zm_order`.`customs_status` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `order_id` CHAR(21) NOT NULL,
  `status` TINYINT UNSIGNED NOT NULL,
  `create_time` DATETIME NULL,
  `update_time` DATETIME NULL,
  `remark` VARCHAR(200) NULL,
  PRIMARY KEY (`id`),
  INDEX `idx_orderId` (`order_id` ASC)
  )ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 
COMMENT = '跨境订单海关状态';




drop table if exists  `zm_order`.`order_shopping_cart`;

CREATE TABLE `zm_order`.`order_shopping_cart` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `user_id` INT UNSIGNED NOT NULL,
  `item_id` VARCHAR(100) NOT NULL,
  `center_id` INT UNSIGNED NOT NULL,
  `item_quantity` INT UNSIGNED NOT NULL,
  `supplier_id` INT UNSIGNED NOT NULL,
  `goods_name` VARCHAR(200) NOT NULL,
  `create_time` DATETIME NULL,
  `update_time` DATETIME NULL,
  `remark` VARCHAR(200) NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `uk_shoppingcart` (`user_id`,`item_id`,`center_id` ASC),
  INDEX `idx_center_id` (`center_id` ASC),
  INDEX `idx_create_time` (`create_time` ASC),
  INDEX `idx_userId` (`user_id` ASC)
  )ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 
COMMENT = '购物车';




drop table if exists  `zm_order`.`order_back_record`;

CREATE TABLE `zm_order`.`order_back_record` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `order_id` CHAR(21) NOT NULL,
  `status` TINYINT UNSIGNED NULL COMMENT '0:未发送，1：发送成功',
  `send_time` DATETIME NULL,
  `create_time` DATETIME NULL,
  `update_time` DATETIME NULL,
  `remark` VARCHAR(200) NULL,
  PRIMARY KEY (`id`),
  INDEX `idx_orderId` (`order_id` ASC)
  )ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 
COMMENT = '退单记录';




drop table if exists  `zm_order`.`order_api`;

CREATE TABLE `zm_order`.`order_api` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `api_url` VARCHAR(100) NOT NULL,
  `name` VARCHAR(100) NOT NULL,
  `create_time` DATETIME NULL,
  `update_time` DATETIME NULL,
  `opt` VARCHAR(20) NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `api_url_UNIQUE` (`api_url` ASC),
  UNIQUE INDEX `name_UNIQUE` (`name` ASC)
  )ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 
COMMENT = '订单API';




drop table if exists  `zm_order`.`order_search_parameter`;

CREATE TABLE `zm_order`.`order_search_parameter` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `api_name` VARCHAR(100) NOT NULL,
  `key_name` VARCHAR(45) NOT NULL COMMENT '字段中文名',
  `key` VARCHAR(45) NULL,
  `value` VARCHAR(100) NULL,
  `html_tag` VARCHAR(30) NULL,
  `type` TINYINT UNSIGNED NULL,
  `create_time` DATETIME NULL,
  `update_time` DATETIME NULL,
  `opt` VARCHAR(20) NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `uk_apiName` (`api_name` ASC),
  INDEX `idx_apiName` (`api_name` ASC)
  )ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 
COMMENT = '订单字段表';
