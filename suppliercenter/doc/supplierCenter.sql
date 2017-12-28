drop SCHEMA if exists  `zm_supplier`;
CREATE SCHEMA `zm_supplier` ;


use zm_supplier;



drop table if exists  `zm_supplier`.`supplier_base`;

CREATE TABLE `zm_supplier`.`supplier_base`(
  `id` bigint Unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `type`  tinyint Unsigned NOT NULL COMMENT '0:跨境供应商，1：大贸供应商',
  `supplier_name` VARCHAR(200) NOT NULL COMMENT '供应商名称',
  `reputation`  tinyint Unsigned NOT NULL DEFAULT 50 COMMENT '商家信誉度',
  `country` VARCHAR(20) NULL COMMENT '国家',
  `province` VARCHAR(20) NULL COMMENT '省',
  `city` VARCHAR(20) NULL COMMENT '市',
  `area` VARCHAR(20) NULL COMMENT '区',
  `address` VARCHAR(200) NULL COMMENT '地址',
  `operator` VARCHAR(20) NULL,
  `phone` CHAR(15) NULL,
  `mobile` CHAR(15) NULL,
  `fax` CHAR(15) NULL,
  `email` VARCHAR(50) NULL,
  `qq` CHAR(15) NULL,
  `enter_time` DATETIME NULL COMMENT '入驻时间',
  `update_time` DATETIME NULL COMMENT '最后修改时间',
  `opt` varchar(20) NULL COMMENT '修改人',
  `is_delete`  tinyint Unsigned NOT NULL DEFAULT 0 COMMENT '是否废除 0:否；1：是',
  `mode` tinyint Unsigned NOT NULL COMMENT '0:自营，1：非自营',
  PRIMARY KEY (`id`),
  UNIQUE `uk_supplierName` (`supplier_name`),
  INDEX `idx_supplierName` (`supplier_name`),
  INDEX `idx_mode` (`mode`),
  INDEX `idx_type` (`type`),
  INDEX `idx_isDelete` (`is_delete`)
  ) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 
COMMENT = '供应商表';


drop table if exists  `zm_supplier`.`supplier_interface`;

CREATE TABLE `zm_supplier`.`supplier_interface` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `supplier_id` BIGINT UNSIGNED NOT NULL,
  `target_object` VARCHAR(50) NOT NULL COMMENT '目标类',
  `pid` VARCHAR(100) NOT NULL COMMENT '第三方秘钥',
  `key` VARCHAR(100) NOT NULL COMMENT '第三方秘钥',
  `attr` VARCHAR(100) NULL COMMENT '备用',
  `create_time` DATETIME NULL,
  `update_time` DATETIME NULL,
  `opt` VARCHAR(20) NULL,
  PRIMARY KEY (`id`),
  INDEX `idx_supplierId` (`supplier_id` ASC),
  UNIQUE INDEX `supplier_id_UNIQUE` (`supplier_id` ASC)
  ) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 
COMMENT = '供应商接口信息';

drop table if exists  `zm_supplier`.`supplier_express`;

CREATE TABLE `zm_supplier`.`supplier_express` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `supplier_id` BIGINT UNSIGNED NOT NULL,
  `express_key` VARCHAR(50) NOT NULL COMMENT '快递公司key',
  `express_name` VARCHAR(100) NOT NULL COMMENT '快递公司名称',
  `target_key` VARCHAR(100) NOT NULL COMMENT '供应商的快递公司key',
  `attr` VARCHAR(100) NULL COMMENT '备用',
  `create_time` DATETIME NULL,
  `update_time` DATETIME NULL,
  `opt` VARCHAR(20) NULL,
  PRIMARY KEY (`id`),
  INDEX `idx_supplierId` (`supplier_id` ASC),
  INDEX `idx_express_key` (`express_key` ASC),
  UNIQUE INDEX `supplier_id_UNIQUE` (`supplier_id`,`express_key` ASC)
  ) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 
COMMENT = '供应商物流公司信息';


drop table if exists  `zm_supplier`.`supplier_api`;

CREATE TABLE `zm_supplier`.`supplier_api` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `api_url` VARCHAR(100) NOT NULL,
  `name` VARCHAR(100) NOT NULL,
  `create_time` DATETIME NULL,
  `update_time` DATETIME NULL,
  `opt` VARCHAR(20) NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `api_url_UNIQUE` (`api_url` ASC),
  UNIQUE INDEX `name_UNIQUE` (`name` ASC)
  )ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 
COMMENT = '供应商API';




drop table if exists  `zm_supplier`.`supplier_search_parameter`;

CREATE TABLE `zm_supplier`.`supplier_search_parameter` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
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
COMMENT = '供应商字段表';

INSERT INTO `zm_supplier`.`supplier_base` (`id`, `type`, `supplier_name`, `country`, `province`, `city`, `area`, `mode`, `enter_time`) VALUES ('1', '0', '天天仓', '中国', '浙江', '宁波', '北仑', '0', now());
INSERT INTO `zm_supplier`.`supplier_base` (`id`, `type`, `supplier_name`, `country`, `province`, `city`, `area`, `mode`, `enter_time`) VALUES ('2', '0', '粮油仓(宁波)', '中国', '浙江', '宁波', '北仑', '1', now());
INSERT INTO `zm_supplier`.`supplier_base` (`id`, `type`, `supplier_name`, `country`, `province`, `city`, `area`, `mode`, `enter_time`) VALUES ('3', '0', '行云仓', '中国', '浙江', '宁波', '北仑', '1', now());
INSERT INTO `zm_supplier`.`supplier_base` (`id`, `type`, `supplier_name`, `country`, `province`, `city`, `area`, `mode`, `enter_time`) VALUES ('4', '0', '富邦仓', '中国', '浙江', '宁波', '北仑', '1', now());
