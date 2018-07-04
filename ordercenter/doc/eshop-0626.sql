use zm_order;

alter table order_base add column is_eshop_in tinyint unsigned default 0 comment '是否eshop入库，0未操作/撤销;1进货;2入库';

drop table if exists  `eshop_sell_order`;

CREATE TABLE `zm_order`.`eshop_sell_order` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `order_id` VARCHAR(50) NOT NULL COMMENT '订单编号',
  `status` TINYINT UNSIGNED NULL DEFAULT 0 COMMENT '0：待收款;1：已收款;',
  `mall_id` INT(11) DEFAULT NULL COMMENT '商城ID',
  `grade_id` INT(11) DEFAULT NULL COMMENT 'gradeID',
  `tdq` tinyint(3) unsigned NOT NULL DEFAULT '0',
  `pay_type` TINYINT UNSIGNED NOT NULL COMMENT '1：微信;2：支付宝;3：现金;',
  `payment` DECIMAL(10,2) NULL COMMENT '总计金额',
  `actual_payment` DECIMAL(10,2) NULL COMMENT '收款金额',
  `gross_profit` DECIMAL(10,2) NULL COMMENT '毛利',
  `remark` VARCHAR(200) NULL COMMENT '备注',
  `create_time` DATETIME NULL COMMENT '注册时间', 
  `update_time` DATETIME NULL COMMENT '更新时间',
  `opt` VARCHAR(20) NULL COMMENT '操作人',
  PRIMARY KEY (`id`),
  INDEX `idx_order_id` (`order_id`),
  INDEX `idx_mall_id` (`mall_id`),
  INDEX `idx_grade_id` (`grade_id`),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC),
  UNIQUE INDEX `mall_id_grade_id_order_id_UNIQUE` (`mall_id`,`grade_id`,`order_id`)) 
  ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 
COMMENT = '销售订单表';

drop table if exists  `eshop_sell_order_detail`;

CREATE TABLE `zm_order`.`eshop_sell_order_detail` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `order_id` VARCHAR(50) NOT NULL COMMENT '订单编号',
  `item_id` VARCHAR(100) NOT NULL,
  `item_name` VARCHAR(100) NOT NULL COMMENT '商品名称',
  `item_img` VARCHAR(200) NULL COMMENT '商品图片',
  `encode` VARCHAR(100) NULL,
  `item_quantity` INT UNSIGNED NOT NULL,
  `proxy_price` decimal(10,2) NULL DEFAULT 0.0 COMMENT '成本价格',
  `retail_price` decimal(10,2) NULL DEFAULT 0.0 COMMENT '零售价格',
  `remark` VARCHAR(200) NULL COMMENT '备注',
  `create_time` DATETIME NULL COMMENT '注册时间', 
  `update_time` DATETIME NULL COMMENT '更新时间',
  `opt` VARCHAR(20) NULL COMMENT '操作人',
  PRIMARY KEY (`id`),
  INDEX `idx_order_id` (`order_id`),
  INDEX `idx_item_id` (`item_id`),
  INDEX `idx_encode` (`encode`),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC),
  UNIQUE INDEX `order_id_item_id_encode_UNIQUE` (`order_id`,`item_id`,`encode`)) 
  ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 
COMMENT = '销售订单明细表';