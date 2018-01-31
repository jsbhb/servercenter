drop SCHEMA if exists  `zm_activity`;
CREATE SCHEMA `zm_activity` ;


use zm_activity;

drop table if exists  `zm_activity`.`activity`;

CREATE TABLE `zm_activity`.`activity` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `activity_id` varchar(100) NOT NULL COMMENT '活动ID',
  `name` varchar(100) NOT NULL COMMENT '活动名称',
  `status` TINYINT UNSIGNED NULL DEFAULT 0 COMMENT '0：未开始，1:开始；2：结束；11：暂停',
  `start_time` DATETIME NULL  COMMENT '开始时间',
  `end_time` DATETIME NULL  COMMENT '结束时间',
  `pic_path` varchar(200) NULL COMMENT '图片地址',
  `attribute` varchar(100) NULL COMMENT '备用',
  `create_time` DATETIME NULL  COMMENT '创建时间',
  `update_time` DATETIME NULL  COMMENT '更新时间',
  `opt` varchar(20) NULL COMMENT '',
  PRIMARY KEY (`id`),
  INDEX `idx_active_id` (`activity_id`),
  INDEX `idx_status` (`status`)
  )ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 
COMMENT = '活动表';


drop table if exists  `zm_activity`.`dictionary`;

CREATE TABLE `zm_activity`.`dictionary` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `dict_name` varchar(100) NOT NULL COMMENT '字典名称',
  `dict_value` varchar(100) NOT NULL COMMENT '字典值',
  `status` TINYINT UNSIGNED NULL DEFAULT 0 COMMENT '0：正常，1:停用；',
  `attribute` varchar(100) NULL COMMENT '备用',
  `create_time` DATETIME NULL  COMMENT '创建时间',
  `update_time` DATETIME NULL  COMMENT '更新时间',
  `opt` varchar(20) NULL COMMENT '',
  PRIMARY KEY (`id`),
  INDEX `idx_dict_value` (`dict_value`),
  INDEX `idx_status` (`status`)
  )ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 
COMMENT = '字典表';


drop table if exists  `zm_activity`.`dictentry`;

CREATE TABLE `zm_activity`.`dictentry` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `dict_value` varchar(100) NOT NULL COMMENT '字典值',
  `entry_name` varchar(100) NOT NULL COMMENT '名称',
  `entry_value` varchar(100) NOT NULL COMMENT '值',
  `status` TINYINT UNSIGNED NULL DEFAULT 0 COMMENT '0：正常，1:停用；',
  `attribute` varchar(100) NULL COMMENT '备用',
  `create_time` DATETIME NULL  COMMENT '创建时间',
  `update_time` DATETIME NULL  COMMENT '更新时间',
  `opt` varchar(20) NULL COMMENT '',
  PRIMARY KEY (`id`),
  INDEX `idx_dict_value` (`dict_value`),
  INDEX `idx_entry_value` (`entry_value`),
  INDEX `idx_status` (`status`)
  )ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 
COMMENT = '字典属性表';



drop table if exists  `zm_activity`.`coupon`;

CREATE TABLE `zm_activity`.`coupon` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `coupon_id` varchar(100) NOT NULL COMMENT '优惠券ID',
  `parent_coupon_id` varchar(100) NULL COMMENT '父级优惠券ID，用于礼包券',
  `rule_id` varchar(100) NOT NULL COMMENT '规则ID',
  `activity_id` varchar(100) NOT NULL COMMENT '活动ID',
  `name` varchar(100) NOT NULL COMMENT '优惠券名称',
  `status` TINYINT UNSIGNED NULL DEFAULT 0 COMMENT '0：初始，1:发放；2：使用；3：过期；11：暂停',
  `issue_start_time` DATETIME NULL COMMENT '发放开始时间',
  `issue_end_time` DATETIME NULL COMMENT '发放结束时间',
  `issue_end_status` TINYINT UNSIGNED DEFAULT 0 COMMENT '0：正常，1:发放结束；此字段预留',
  `start_time` DATETIME NULL  COMMENT '开始时间',
  `end_time` DATETIME NULL  COMMENT '结束时间',
  `node` TINYINT  NULL DEFAULT 0 COMMENT '触发节点 1：注册完成；',
  `num` INT NULL DEFAULT 0 COMMENT '发行数量0表示不限量',
  `receive_num` INT NULL DEFAULT 0 COMMENT '领取数量',
  `use_num` INT NULL DEFAULT 0 COMMENT '使用数量',
  `pic_path` varchar(200) NULL COMMENT '图片地址',
  `attribute` varchar(100) NULL COMMENT '备用',
  `create_time` DATETIME NULL  COMMENT '创建时间',
  `update_time` DATETIME NULL  COMMENT '更新时间',
  `opt` varchar(20) NULL COMMENT '',
  PRIMARY KEY (`id`),
  INDEX `idx_coupon_id` (`coupon_id`),
  INDEX `idx_rule_id` (`rule_id`),
  INDEX `idx_active_id` (`activity_id`),
  INDEX `idx_status` (`status`),
  INDEX `idx_node` (`node`)
  )ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 
COMMENT = '优惠券';

drop table if exists  `zm_activity`.`rule`;

CREATE TABLE `zm_activity`.`rule` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `rule_id` varchar(100) NOT NULL COMMENT '优惠券ID',
  `description` varchar(100) NULL COMMENT '规则描述',
  `condition` DECIMAL(10,2) NOT NULL COMMENT '满条件',
  `deductible_value` DECIMAL(10,2) NOT NULL COMMENT '抵扣额',
  `value_type` TINYINT NOT NULL COMMENT  '0:折扣；1金额',
  `range` TINYINT NOT NULL COMMENT '使用范围0全场，1一级分了；2二级分类；3三级分类；4特定商品',
  `category` varchar(100)  NULL COMMENT '分类ID，range为1,2,3时不为空',
  `is_superposition` INT NULL DEFAULT 0 COMMENT '是否可叠加：0否，1是',
  `create_time` DATETIME NULL  COMMENT '创建时间',
  `update_time` DATETIME NULL  COMMENT '更新时间',
  `weight` INT NULL  COMMENT '权重',
  `attribute` varchar(100) NULL COMMENT '备用',
  `opt` varchar(20) NULL COMMENT '',
  PRIMARY KEY (`id`)
  )ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 
COMMENT = '规则表';

drop table if exists  `zm_activity`.`coupon_goods`;

CREATE TABLE `zm_activity`.`coupon_goods` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `rule_id` varchar(100) NOT NULL COMMENT '规则ID',
  `goods_id` varchar(50) NOT NULL COMMENT '商品ID',
  `attribute` varchar(100) NULL COMMENT '备用',
  `opt` varchar(20) NULL COMMENT '',
  PRIMARY KEY (`id`),
  INDEX `idx_coupon_id` (`rule_id`),
  INDEX `idx_goods_id` (`goods_id`),
  UNIQUE INDEX `id_UNIQUE` (`rule_id`,`goods_id`)
  )ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 
COMMENT = '优惠券商品';


drop table if exists  `zm_activity`.`user_coupon`;

CREATE TABLE `zm_activity`.`user_coupon` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `user_id` INT UNSIGNED NOT NULL COMMENT '账号ID',
  `coupon_id` varchar(100) NOT NULL COMMENT '优惠券ID',
  `center_id` INT NULL COMMENT '区域中心ID',
  `status` TINYINT UNSIGNED NULL DEFAULT 0 COMMENT '0：未使用；1：已使用',
  `create_time` DATETIME NULL  COMMENT '创建时间',
  PRIMARY KEY (`id`),
  INDEX `idx_user_id` (`user_id`),
  INDEX `idx_status` (`status`),
  INDEX `idx_center_id` (`center_id`),
  UNIQUE INDEX `id_UNIQUE` (`user_id`,`coupon_id`,`center_id`)
  ) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 
COMMENT = '用户优惠券绑定表';