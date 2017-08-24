drop SCHEMA if exists  `zm_goods`;
CREATE SCHEMA `zm_goods` ;

use zm_goods;

drop table if exists  `goods_base`;

CREATE TABLE `zm_goods`.`goods_base` (
  `id` int UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `brand_id` int UNSIGNED NOT NULL COMMENT '品牌ID',
  `goods_name` VARCHAR(100) NOT NULL COMMENT '商品名称',
  `brand` VARCHAR(100) NOT NULL COMMENT '商品品牌',
  `origin` VARCHAR(100) NULL COMMENT '原产国',
  `taxrate` DECIMAL(4,2) NULL COMMENT '税率',
  `unit` VARCHAR(30) NOT NULL COMMENT '单位',
  `create_time` DATETIME NULL COMMENT '注册时间',
  `update_time` DATETIME NULL COMMENT '更新时间',
  `opt` VARCHAR(20) NULL COMMENT '操作人',
  PRIMARY KEY (`id`),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC),
  INDEX `idx_brand` (`brand`),
  INDEX `idx_brand_id` (`brand_id`),
  INDEX `idx_origin` (`origin`)) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 
COMMENT = '商品信息管理表';


drop table if exists  `goods_item`;

CREATE TABLE `zm_goods`.`goods_item` (
  `id` int UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `supplier_id` int UNSIGNED NOT NULL COMMENT '商家ID',
  `base_id` int UNSIGNED NOT NULL COMMENT '商品基本信息ID',
  `goods_name` VARCHAR(100) NOT NULL COMMENT '商品名称',
  `status` tinyint UNSIGNED NOT NULL COMMENT '商品状态0：停售，1：在售',
  `type` tinyint UNSIGNED NOT NULL COMMENT '商品分类0：大贸；1：跨境',
  `is_popular` tinyint UNSIGNED NOT NULL DEFAULT 0 COMMENT '是否推广0：否，1是',
  `is_hot` tinyint UNSIGNED NOT NULL DEFAULT 0 COMMENT '是否热卖0：否，1是',
  `is_good` tinyint UNSIGNED NOT NULL DEFAULT 0 COMMENT '是否好货0：否，1是',
  `is_choice` tinyint UNSIGNED NOT NULL DEFAULT 0 COMMENT '是否精选0：否，1是',
  `detailPath` VARCHAR(100) NULL COMMENT '商家ID',
  `index_status` tinyint UNSIGNED NOT NULL DEFAULT 0 COMMENT '是否创建lucene0：否，1是',
  `create_time` DATETIME NULL COMMENT '创建时间',
  `update_time` DATETIME NULL COMMENT '更新时间',
  `opt` VARCHAR(20) NULL COMMENT '操作人',
  PRIMARY KEY (`id`),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC),
  INDEX `idx_supplier_id` (`supplier_id`),
  INDEX `idx_base_id` (`base_id`),
  INDEX `idx_status` (`status`),
  INDEX `idx_type` (`type`),
  INDEX `idx_is_popular` (`is_popular`),
  INDEX `idx_is_hot` (`is_hot`),
  INDEX `idx_is_good` (`is_good`),
  INDEX `idx_is_choice` (`is_choice`)) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 
COMMENT = '供应商商品基本信息表';


drop table if exists  `goods_file`;

CREATE TABLE `zm_goods`.`goods_file` (
  `id` int UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `goods_id` int NOT NULL COMMENT '商品ID',
  `path` VARCHAR(100) NULL COMMENT '文件路径',
  `suffix` CHAR(5) NULL COMMENT '后缀',
  `store_type` tinyint UNSIGNED NULL COMMENT '存储类型',
  `type` tinyint UNSIGNED NULL COMMENT '存储类型',
  `create_time` DATETIME NULL COMMENT '创建时间',
  `update_time` DATETIME NULL COMMENT '更新时间',
  `opt` VARCHAR(20) NULL COMMENT '操作人',
  PRIMARY KEY (`id`),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC),
  INDEX `idx_goods_id` (`goods_id`),
  INDEX `idx_suffix` (`suffix`),
  INDEX `idx_store_type` (store_type),
  INDEX `idx_type` (type)
  ) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 
COMMENT = '供应商商品文件表';


drop table if exists  `goods_first_category`;

CREATE TABLE `zm_goods`.`goods_first_category` (
  `id` int UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `name` VARCHAR(100) NULL COMMENT '一级分类名称',
  `create_time` DATETIME NULL COMMENT '创建时间',
  `update_time` DATETIME NULL COMMENT '更新时间',
  `opt` VARCHAR(20) NULL COMMENT '操作人',
  PRIMARY KEY (`id`)) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 
COMMENT = '供应商商品一级分类表';


drop table if exists  `goods_second_categroy`;

CREATE TABLE `zm_goods`.`goods_second_categroy` (
  `id` int UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `first_id` int NOT NULL COMMENT 'first_id',
  `name` VARCHAR(100) NULL COMMENT '二级分类名称',
  `create_time` DATETIME NULL COMMENT '创建时间',
  `update_time` DATETIME NULL COMMENT '更新时间',
  `opt` VARCHAR(20) NULL COMMENT '操作人',
  PRIMARY KEY (`id`),
  INDEX `goods_second_categroy_first_id` (`first_id`)) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 
COMMENT = '供应商商品二级分类表';


drop table if exists  `goods_third_category`;

CREATE TABLE `zm_goods`.`goods_third_category` (
  `id` int UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `second_id` int NOT NULL COMMENT 'second_id',
  `name` VARCHAR(100) NULL COMMENT '三级分类名称',
  `create_time` DATETIME NULL COMMENT '创建时间',
  `update_time` DATETIME NULL COMMENT '更新时间',
  `opt` VARCHAR(20) NULL COMMENT '操作人',
  PRIMARY KEY (`id`),
  INDEX `goods_third_category_second_id` (`second_id`)) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 
COMMENT = '供应商商品三级分类表';


drop table if exists  `goods_category_brand`;

CREATE TABLE `zm_goods`.`goods_category_brand` (
  `id` int UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `brand` VARCHAR(100) NULL COMMENT '品牌名称',
  `type` tinyint UNSIGNED NOT NULL COMMENT '0:大贸；1：跨境；2：海蒸鲜',
  `first_category` int UNSIGNED NOT NULL COMMENT '一级分类id',
  `first_name` VARCHAR(100) NULL COMMENT '一级分类名称',
  `second_category` int UNSIGNED NOT NULL COMMENT '二级分类id',
  `second_name` VARCHAR(100) NULL COMMENT '二级分类名称',
  `third_category` int UNSIGNED NOT NULL COMMENT '三级分类id',
  `third_name` VARCHAR(100) NULL COMMENT '三级分类名称',
  `create_time` DATETIME NULL COMMENT '创建时间',
  `update_time` DATETIME NULL COMMENT '更新时间',
  `opt` VARCHAR(20) NULL COMMENT '操作人',
  PRIMARY KEY (`id`),
  INDEX `goods_category_brand_brand` (`brand`),
  INDEX `idx_first_category` (`first_category`),
  INDEX `idx_type` (`type`),
  INDEX `idx_second_category` (`second_category`),
  INDEX `idx_third_category` (`third_category`)
  ) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 
COMMENT = '商品分类品牌表';


drop table if exists  `goods_specs`;

CREATE TABLE `zm_goods`.`goods_specs` (
  `id` int UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `goods_id` int UNSIGNED NOT NULL COMMENT '商品ID',
  `item_id` varchar(50) NOT NULL COMMENT '供销内部商品ID',
  `item_code` varchar(50) NOT NULL COMMENT '商家自有编码',
  `sku` VARCHAR(100) NULL COMMENT 'sku信息',
  `is_promotion` tinyint UNSIGNED NOT NULL DEFAULT 0 COMMENT '是否促销0：否；1：是',
  `info` VARCHAR(1000) NULL COMMENT '规格信息',
  `create_time` DATETIME NULL COMMENT '创建时间',
  `update_time` DATETIME NULL COMMENT '更新时间',
  `opt` VARCHAR(20) NULL COMMENT '操作人',
  PRIMARY KEY (`id`),
  INDEX `idx_goods_id` (`goods_id`),
  INDEX `idx_item_id` (`item_id`),
  INDEX `idx_is_promotion` (`is_promotion`),
  INDEX `idx_sku` (`sku`)
  ) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 
COMMENT = '商品规格表';


drop table if exists  `goods_price_contrast`;

CREATE TABLE `zm_goods`.`goods_price_contrast` (
  `id` int UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `item_id` varchar(50) NOT NULL COMMENT '内部商品ID',
  `sh_price` decimal(10,2) NOT NULL COMMENT '上海价格',
  `bj_price` decimal(10,2) NOT NULL COMMENT '北京价格',
  `price` decimal(10,2) NOT NULL COMMENT '自己价格',
  `reserve` decimal(10,2) NULL COMMENT '备用',
  `contrast_time` varchar(15) NOT NULL COMMENT '比较时间',
  `create_time` DATETIME NULL COMMENT '创建时间',
  `update_time` DATETIME NULL COMMENT '更新时间',
  `opt` VARCHAR(20) NULL COMMENT '操作人',
  PRIMARY KEY (`id`),
  INDEX `idx_item_id` (`item_id`),
  UNIQUE INDEX `uq_contrast_time` (`contrast_time` ASC)
  ) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 
COMMENT = '商品价格对比表';


drop table if exists  `goods_price`;

CREATE TABLE `zm_goods`.`goods_price` (
  `id` int UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `item_id` varchar(50) NOT NULL COMMENT '商品ID',
  `min` int UNSIGNED NULL COMMENT '最小数量',
  `max` int UNSIGNED NULL COMMENT '最大数量',
  `price` decimal(10,2) NULL COMMENT '价格',
  `vip_price` decimal(10,2) NULL COMMENT '会员价格',
  `discount` decimal(10,2) NULL COMMENT '促销折扣',
  `create_time` DATETIME NULL COMMENT '创建时间',
  `update_time` DATETIME NULL COMMENT '更新时间',
  `opt` VARCHAR(20) NULL COMMENT '操作人',
  PRIMARY KEY (`id`),
  INDEX `idx_item_id` (`item_id`)) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 
COMMENT = '商品价格表';


drop table if exists  `goods_stock`;

CREATE TABLE `zm_goods`.`goods_stock` (
  `id` int UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `item_id` varchar(50) NOT NULL COMMENT '内部商品ID',
  `qpqty` int UNSIGNED NULL COMMENT '良品数量', 
  `defqty` int UNSIGNED NULL COMMENT '次品数量',
  `frozenqty` int UNSIGNED NULL COMMENT '冻结数量',
  `lockedqty` int UNSIGNED NULL COMMENT '锁定数量',
  `create_time` DATETIME NULL COMMENT '创建时间',
  `update_time` DATETIME NULL COMMENT '更新时间',
  `opt` VARCHAR(20) NULL COMMENT '操作人',
  PRIMARY KEY (`id`),
  INDEX `idx_item_id` (`item_id`)) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 
COMMENT = '商品库存表';


drop table if exists  `goods_api`;

CREATE TABLE `zm_goods`.`goods_api` (
  `id` int UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `api_url` VARCHAR(100) NOT NULL COMMENT 'api地址',
  `api_name` VARCHAR(50) NOT NULL COMMENT 'api名称',
  `create_time` DATETIME NULL COMMENT '创建时间',
  `update_time` DATETIME NULL COMMENT '更新时间',
  `opt` VARCHAR(20) NULL COMMENT '操作人',
  PRIMARY KEY (`id`)) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 
COMMENT = '服务api表';


drop table if exists  `goods_search_parameter`;

CREATE TABLE `zm_goods`.`goods_search_parameter` (
  `id` int UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `key` VARCHAR(100) NOT NULL COMMENT '字段名',
  `value` VARCHAR(500) NOT NULL COMMENT '值',
  `html_tag` VARCHAR(50) NOT NULL COMMENT 'HTML标签',
  `type` Int NOT NULL COMMENT '1只读；2读写；3读写删',
  `create_time` DATETIME NULL COMMENT '创建时间',
  `update_time` DATETIME NULL COMMENT '更新时间',
  `opt` VARCHAR(20) NULL COMMENT '操作人',
  PRIMARY KEY (`id`)) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 
COMMENT = '检索字段表';

