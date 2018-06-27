use zm_goods;

drop table if exists  `eshop_goods_purchase`;

CREATE TABLE `zm_goods`.`eshop_goods_purchase` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `purchase_id` VARCHAR(50) NOT NULL COMMENT '采购单编号',
  `status` TINYINT UNSIGNED NULL DEFAULT 0 COMMENT '0：入库;1：撤销;',
  `mall_id` INT(11) DEFAULT NULL COMMENT '商城ID',
  `grade_id` INT(11) DEFAULT NULL COMMENT 'gradeID',
  `tdq` tinyint(3) unsigned NOT NULL DEFAULT '0',
  `remark` VARCHAR(200) NULL COMMENT '备注',
  `create_time` DATETIME NULL COMMENT '注册时间', 
  `update_time` DATETIME NULL COMMENT '更新时间',
  `opt` VARCHAR(20) NULL COMMENT '操作人',
  PRIMARY KEY (`id`),
  INDEX `idx_purchase_id` (`purchase_id`),
  INDEX `idx_mall_id` (`mall_id`),
  INDEX `idx_grade_id` (`grade_id`),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC),
  UNIQUE INDEX `purchase_id_UNIQUE` (`purchase_id` ASC)) 
  ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 
COMMENT = '商品采购表';

drop table if exists  `eshop_goods_purchase_detail`;

CREATE TABLE `zm_goods`.`eshop_goods_purchase_detail` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `purchase_id` VARCHAR(50) NOT NULL COMMENT '采购单编号',
  `status` TINYINT UNSIGNED NULL DEFAULT 0 COMMENT '0：入库;1：撤销;',
  `item_id` INT(11) DEFAULT NULL COMMENT 'item_id',
  `encode` INT(11) DEFAULT NULL COMMENT 'encode',
  `item_quantity` int(10) unsigned NOT NULL,
  `item_price` decimal(10,2) NOT NULL,
  `actual_price` decimal(10,2) NOT NULL,
  `remark` VARCHAR(200) NULL COMMENT '备注',
  `create_time` DATETIME NULL COMMENT '注册时间', 
  `update_time` DATETIME NULL COMMENT '更新时间',
  `opt` VARCHAR(20) NULL COMMENT '操作人',
  PRIMARY KEY (`id`),
  INDEX `idx_purchase_id` (`purchase_id`),
  INDEX `idx_item_id` (`item_id`),
  INDEX `idx_encode` (`encode`),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC)) 
  ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 
COMMENT = '商品采购明细表';

drop table if exists  `eshop_goods`;

CREATE TABLE `zm_goods`.`eshop_goods` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `mall_id` INT(11) DEFAULT NULL COMMENT '商城ID',
  `grade_id` INT(11) DEFAULT NULL COMMENT 'gradeID',
  `goods_name` VARCHAR(100) NOT NULL COMMENT '商品名称',
  `origin` VARCHAR(100) NULL COMMENT '原产国',
  `first_category` varchar(100) NULL COMMENT '一级分类',
  `brand` VARCHAR(100) NULL COMMENT '品牌名称',
  `status` TINYINT UNSIGNED NULL DEFAULT 0 COMMENT '0：在售;1：停售;',
  `item_id` INT(11) DEFAULT NULL COMMENT 'item_id',
  `encode` INT(11) DEFAULT NULL COMMENT 'encode',
  `proxy_price` decimal(10,2) NULL DEFAULT 0.0 COMMENT '成本价格',
  `retail_price` decimal(10,2) DEFAULT 0.0 NULL COMMENT '零售价格',
  `remark` VARCHAR(200) NULL COMMENT '备注',
  `create_time` DATETIME NULL COMMENT '注册时间', 
  `update_time` DATETIME NULL COMMENT '更新时间',
  `opt` VARCHAR(20) NULL COMMENT '操作人',
  PRIMARY KEY (`id`),
  INDEX `idx_mall_id` (`mall_id`),
  INDEX `idx_grade_id` (`grade_id`),
  INDEX `idx_first_category` (`first_category`),
  INDEX `idx_brand` (`brand`),
  INDEX `idx_item_id` (`item_id`),
  INDEX `idx_encode` (`encode`),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC),
  UNIQUE INDEX `mall_id_grade_id_item_id_UNIQUE` (`mall_id`,`grade_id`,`item_id`)) 
  ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 
COMMENT = '商品表';

drop table if exists  `eshop_goods_stock`;

CREATE TABLE `zm_goods`.`eshop_goods_stock` (
  `id` int UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `mall_id` INT(11) DEFAULT NULL COMMENT '商城ID',
  `grade_id` INT(11) DEFAULT NULL COMMENT 'gradeID',
  `item_id` VARCHAR(50) NOT NULL COMMENT 'itemID',
  `loc` VARCHAR(50) NULL COMMENT 'location',
  `is_default` tinyint(3) unsigned DEFAULT '0' COMMENT '是否默认，0否；1是',
  `quantity` int  NULL COMMENT '数量', 
  `create_time` DATETIME NULL COMMENT '创建时间',
  `update_time` DATETIME NULL COMMENT '更新时间',
  `opt` VARCHAR(20) NULL COMMENT '操作人',
  PRIMARY KEY (`id`),
  INDEX `idx_mall_id` (`mall_id`),
  INDEX `idx_grade_id` (`grade_id`),
  INDEX `idx_item_id` (`item_id`),
  INDEX `idx_loc` (`loc`)
  UNIQUE INDEX `id_UNIQUE` (`id` ASC),
  UNIQUE INDEX `mall_id_grade_id_item_id_UNIQUE` (`mall_id`,`grade_id`,`item_id`)) 
  ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 
COMMENT = '商品库位库存表';

drop table if exists  `eshop_goods_inventory`;

CREATE TABLE `zm_goods`.`eshop_goods_inventory` (
  `id` int UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `mall_id` INT(11) DEFAULT NULL COMMENT '商城ID',
  `grade_id` INT(11) DEFAULT NULL COMMENT 'gradeID',
  `item_id` VARCHAR(50) NOT NULL COMMENT 'itemID',
  `loc` VARCHAR(50) NULL COMMENT 'location',
  `inventory_type` tinyint(3) unsigned DEFAULT '0' COMMENT '盘点类型，0盘盈；1盘库',
  `sys_qty` int  NULL COMMENT '系统数量',
  `check_qty` int  NULL COMMENT '盘点数量',
  `diff_qty` int  NULL COMMENT '差异数量',
  `create_time` DATETIME NULL COMMENT '创建时间',
  `update_time` DATETIME NULL COMMENT '更新时间',
  `opt` VARCHAR(20) NULL COMMENT '操作人',
  PRIMARY KEY (`id`),
  INDEX `idx_mall_id` (`mall_id`),
  INDEX `idx_grade_id` (`grade_id`),
  INDEX `idx_item_id` (`item_id`),
  INDEX `idx_loc` (`loc`)
  UNIQUE INDEX `id_UNIQUE` (`id` ASC)) 
  ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 
COMMENT = '商品盘点记录表';