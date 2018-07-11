use zm_goods;

drop table if exists  `goods_ratio_platform`;

CREATE TABLE `zm_goods`.`goods_ratio_platform` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `ratio_platform_name` VARCHAR(50) NOT NULL COMMENT '比价平台名称',
  /*
  `ratio_platform_address` VARCHAR(100) NULL COMMENT '比价平台网址',
  `grab_role` VARCHAR(300) NULL COMMENT '抓取规则',
  `status` TINYINT UNSIGNED NULL DEFAULT 0 COMMENT '是否启用抓取规则 0：不启用;1：启用',
  */
  `is_use` TINYINT UNSIGNED NULL DEFAULT 0 COMMENT '是否使用，0：是;1：否',
  `create_time` DATETIME NULL COMMENT '注册时间', 
  `update_time` DATETIME NULL COMMENT '更新时间',
  `opt` VARCHAR(20) NULL COMMENT '操作人',
  PRIMARY KEY (`id`),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC)) 
  ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 
COMMENT = '比价平台表';

drop table if exists  `goods_price_ratio`;

CREATE TABLE `zm_goods`.`goods_price_ratio` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `item_id` VARCHAR(50) NOT NULL COMMENT 'itemId',
  `ratio_platform_id` VARCHAR(50) NOT NULL COMMENT '比价平台id',
/*`ratio_platform_item_id` VARCHAR(50) NULL COMMENT '比价平台itemId',*/
  `ratio_price` decimal(10,2) DEFAULT 0.0 NULL COMMENT '平台价格',
  `evaluate_qty` int  NULL COMMENT '评价数', 
  `sales_volume` int  NULL COMMENT '销量', 
  `status` TINYINT UNSIGNED NULL DEFAULT 0 COMMENT '是否根据网址抓取 0：不抓取;1：抓取',
  `grab_address` VARCHAR(300) NULL COMMENT '抓取地址',
  `create_time` DATETIME NULL COMMENT '注册时间', 
  `update_time` DATETIME NULL COMMENT '更新时间',
  `opt` VARCHAR(20) NULL COMMENT '操作人',
  PRIMARY KEY (`id`),
  INDEX `idx_item_id` (`item_id`),
  INDEX `idx_ratio_platform_id` (`ratio_platform_id`),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC),
  UNIQUE INDEX `item_id_ratio_platform_id_UNIQUE` (`item_id`,`ratio_platform_id`)) 
  ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 
COMMENT = '商品比价表';