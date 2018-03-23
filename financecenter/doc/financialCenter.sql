drop SCHEMA if exists  `zm_financial`;
CREATE SCHEMA `zm_financial` ;

use zm_financial;

drop table if exists  `capitalpool`;

CREATE TABLE `zm_financial`.`capitalpool` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `center_id` INT NULL COMMENT '区域中心ID',
  `money` DECIMAL(12,2) DEFAULT 0 COMMENT '可用金额',
  `frozen_money` DECIMAL(12,2) DEFAULT 0 COMMENT '冻结可用',
  `preferential` DECIMAL(12,2) DEFAULT 0 COMMENT '优惠金额',
  `frozen_preferential` DECIMAL(12,2) DEFAULT 0 COMMENT '冻结优惠',
  `use_money` DECIMAL(12,2) DEFAULT 0 COMMENT '使用金额',
  `use_preferential` DECIMAL(12,2) DEFAULT 0 COMMENT '使用优惠',
  `count_money` DECIMAL(12,2) DEFAULT 0 COMMENT '累计金额',
  `count_preferential` DECIMAL(12,2) DEFAULT 0 COMMENT '累计优惠',
  `level` TINYINT UNSIGNED NULL COMMENT '星级',
  `status` TINYINT UNSIGNED NULL DEFAULT 1 COMMENT '状态0停用，1启用',
  `remark` VARCHAR(200) NULL COMMENT '备注',
  `create_time` DATETIME NULL COMMENT '注册时间', 
  `update_time` DATETIME NULL COMMENT '更新时间',
  `opt` VARCHAR(20) NULL COMMENT '操作人',
  PRIMARY KEY (`id`),
  INDEX `idx_center_id` (`center_id`),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC),
  UNIQUE INDEX `center_id_UNIQUE` (`center_id` ASC)) 
  ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 
COMMENT = '资金池主表';

drop table if exists  `capitalpool_detail`;

CREATE TABLE `zm_financial`.`capitalpool_detail` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `center_id` INT NULL COMMENT '区域中心ID',
  `pay_type` TINYINT UNSIGNED NULL COMMENT '支付类型0:收入,1:支出',
  `business_type` TINYINT UNSIGNED NULL COMMENT '业务类型0:现金,1:返佣,2:赠送,3:抵用券',
  `money` DECIMAL(12,2) NULL COMMENT '金额',
  `pay_no` VARCHAR(30) NULL COMMENT '流水号',
  `order_id` VARCHAR(30) NULL COMMENT '订单号',
  `remark` VARCHAR(200) NULL COMMENT '备注',
  `create_time` DATETIME NULL COMMENT '注册时间', 
  `opt` VARCHAR(20) NULL COMMENT '操作人',
  PRIMARY KEY (`id`),
  INDEX `idx_center_id` (`center_id`),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC)) 
  ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 
COMMENT = '资金池记录表';

drop table if exists  `center_rebate_count`;

CREATE TABLE `zm_financial`.`center_rebate_count` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `center_id` INT NULL COMMENT '区域ID',
  `can_be_presented` DECIMAL(12,2) DEFAULT 0 COMMENT '可提现',
  `already_presented` DECIMAL(12,2) DEFAULT 0 COMMENT '已提现',
  `stay_to_account` DECIMAL(12,2) DEFAULT 0 COMMENT '待到账',
  `refilling` DECIMAL(12,2) DEFAULT 0 COMMENT '已返充',
  `remark` VARCHAR(200) NULL COMMENT '备注',
  `create_time` DATETIME NULL COMMENT '注册时间', 
  `update_time` DATETIME NULL COMMENT '更新时间',
  `opt` VARCHAR(20) NULL COMMENT '操作人',
  PRIMARY KEY (`id`),
  INDEX `center_id` (`center_id`),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC),
  UNIQUE INDEX `center_id_UNIQUE` (`center_id` ASC)) 
  ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 
COMMENT = '区域返佣统计表';

drop table if exists  `shop_rebate_count`;

CREATE TABLE `zm_financial`.`shop_rebate_count` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `shop_id` INT NULL COMMENT '店铺ID',
  `can_be_presented` DECIMAL(12,2) DEFAULT 0 COMMENT '可提现',
  `already_presented` DECIMAL(12,2) DEFAULT 0 COMMENT '已提现',
  `stay_to_account` DECIMAL(12,2) DEFAULT 0 COMMENT '待到账',
  `remark` VARCHAR(200) NULL COMMENT '备注',
  `create_time` DATETIME NULL COMMENT '注册时间', 
  `update_time` DATETIME NULL COMMENT '更新时间',
  `opt` VARCHAR(20) NULL COMMENT '操作人',
  PRIMARY KEY (`id`),
  INDEX `shop_id` (`shop_id`),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC),
  UNIQUE INDEX `shop_id_UNIQUE` (`shop_id` ASC)) 
  ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 
COMMENT = '店铺返佣统计表';

drop table if exists  `push_rebate_count`;

CREATE TABLE `zm_financial`.`push_rebate_count` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `user_id` INT NULL COMMENT '推手ID',
  `shop_id` INT NULL COMMENT '店铺ID',
  `can_be_presented` DECIMAL(12,2) DEFAULT 0 COMMENT '可提现',
  `already_presented` DECIMAL(12,2) DEFAULT 0 COMMENT '已提现',
  `stay_to_account` DECIMAL(12,2) DEFAULT 0 COMMENT '待到账',
  `remark` VARCHAR(200) NULL COMMENT '备注',
  `create_time` DATETIME NULL COMMENT '注册时间', 
  `update_time` DATETIME NULL COMMENT '更新时间',
  `opt` VARCHAR(20) NULL COMMENT '操作人',
  PRIMARY KEY (`id`),
  UNIQUE INDEX `user_id` (`user_id`),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC)) 
  ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 
COMMENT = '推手返佣统计表';

drop table if exists  `rebate_detail`;

CREATE TABLE `zm_financial`.`rebate_detail` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `order_id` VARCHAR(30) NULL COMMENT '订单号',
  `center_id` INT NULL COMMENT '区域ID',
  `center_rebate_money` DECIMAL(12,2) DEFAULT 0 COMMENT '区域返佣金额',
  `shop_id` INT NULL COMMENT '店铺ID',
  `shop_rebate_money` DECIMAL(12,2) DEFAULT 0 COMMENT '店铺返佣金额',
  `user_id` INT NULL COMMENT '推手ID',
  `user_rebate_money` DECIMAL(12,2) DEFAULT 0 COMMENT '推手返佣金额',
  `remark` VARCHAR(200) NULL COMMENT '备注',
  `create_time` DATETIME NULL COMMENT '注册时间', 
  `opt` VARCHAR(20) NULL COMMENT '操作人',
  PRIMARY KEY (`id`),
  INDEX `order_id` (`order_id`),
  INDEX `center_id` (`center_id`),
  INDEX `shop_id` (`shop_id`),
  INDEX `user_id` (`user_id`),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC),
  UNIQUE INDEX `order_id_UNIQUE` (`order_id` ASC)) 
  ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 
COMMENT = '返佣记录表';

drop table if exists  `bind_card`;

CREATE TABLE `zm_financial`.`bind_card` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `type_id` INT NULL COMMENT '区域ID/店铺ID/推手ID',
  `type` TINYINT UNSIGNED COMMENT '0区域；1店铺；2推手',
  `card_bank` VARCHAR(50) NULL COMMENT '开户行',
  `card_no` VARCHAR(20) NULL COMMENT '卡号',
  `is_default` TINYINT UNSIGNED DEFAULT 1 COMMENT '默认0否，1是',
  `card_name` VARCHAR(20) NULL COMMENT '持卡人姓名',
  `card_mobile` VARCHAR(20) NULL COMMENT '预留电话',
  `remark` VARCHAR(200) NULL COMMENT '备注',
  `create_time` DATETIME NULL COMMENT '注册时间', 
  `update_time` DATETIME NULL COMMENT '更新时间',
  `opt` VARCHAR(20) NULL COMMENT '操作人',
  PRIMARY KEY (`id`),
  INDEX `type_id` (`type_id`),
  INDEX `type` (`type`),
  INDEX `card_no` (`card_no`),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC),
  UNIQUE INDEX `card_no_UNIQUE` (`card_no` ASC)) 
  ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 
COMMENT = '绑卡记录表';

drop table if exists  `withdrawals_detail`;

CREATE TABLE `zm_financial`.`withdrawals_detail` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `operator_id` INT NULL COMMENT '操作id，对应operator_type',
  `operator_type` TINYINT UNSIGNED NULL COMMENT '操作者类型,0：区域中心ID,1:店铺ID，2：推手ID',
  `start_money` DECIMAL(12,2) NULL COMMENT '起始金额',
  `out_money` DECIMAL(12,2) NULL COMMENT '提现金额',
  `status` TINYINT UNSIGNED NULL DEFAULT 1 COMMENT '状态1申请中，2已同意，3已拒绝',
  `card_bank` VARCHAR(50) NULL COMMENT '开户行',
  `card_no` VARCHAR(20) NULL COMMENT '卡号',
  `card_name` VARCHAR(20) NULL COMMENT '持卡人姓名',
  `pay_no` VARCHAR(30) NULL COMMENT '流水号',
  `remark` VARCHAR(200) NULL COMMENT '备注',
  `create_time` DATETIME NULL COMMENT '注册时间', 
  `update_time` DATETIME NULL COMMENT '更新时间',
  `opt` VARCHAR(20) NULL COMMENT '操作人',
  PRIMARY KEY (`id`),
  INDEX `operator_id` (`operator_id`),
  INDEX `status` (`status`),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC)) 
  ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 
COMMENT = '提现记录表';

drop table if exists  `refilling_detail`;

CREATE TABLE `zm_financial`.`refilling_detail` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `center_id` INT NULL COMMENT '区域id',
  `money` DECIMAL(12,2) NULL COMMENT '反充金额',
  `status` TINYINT UNSIGNED NULL DEFAULT 1 COMMENT '状态1申请中，2已同意，3已拒绝',
  `remark` VARCHAR(200) NULL COMMENT '备注',
  `create_time` DATETIME NULL COMMENT '注册时间', 
  `update_time` DATETIME NULL COMMENT '更新时间',
  `opt` VARCHAR(20) NULL COMMENT '操作人',
  PRIMARY KEY (`id`),
  INDEX `center_id` (`center_id`),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC)) 
  ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 
COMMENT = '返充记录表';


