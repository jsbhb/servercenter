drop table if exists  `rebate`;

CREATE TABLE `zm_financial`.`rebate` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `grade_id` INT NULL COMMENT 'gradeID',
  `can_be_presented` DECIMAL(12,2) DEFAULT 0 COMMENT '可提现',
  `already_presented` DECIMAL(12,2) DEFAULT 0 COMMENT '已提现',
  `stay_to_account` DECIMAL(12,2) DEFAULT 0 COMMENT '待到账',
  `remark` VARCHAR(200) NULL COMMENT '备注',
  `create_time` DATETIME NULL COMMENT '注册时间', 
  `update_time` DATETIME NULL COMMENT '更新时间',
  `opt` VARCHAR(20) NULL COMMENT '操作人',
  PRIMARY KEY (`id`),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC)) 
  ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 
COMMENT = '返佣表';


drop table if exists  `rebate_detail`;

CREATE TABLE `zm_financial`.`rebate_detail` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `order_id` VARCHAR(30) NULL COMMENT '订单号',
  `grade_id` INT NULL COMMENT 'gradeID',
  `rebate_money` DECIMAL(12,2) DEFAULT 0 COMMENT '返佣金额',
  `remark` VARCHAR(200) NULL COMMENT '备注',
  `create_time` DATETIME NULL COMMENT '注册时间', 
  `opt` VARCHAR(20) NULL COMMENT '操作人',
  PRIMARY KEY (`id`),
  INDEX `order_id` (`order_id`),
  INDEX `grade_id` (`grade_id`),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC),
  UNIQUE INDEX `order_id_UNIQUE` (`order_id`,`grade_id` ASC)) 
  ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 
COMMENT = '返佣记录表';

drop table if exists  `capitalpool_redis`;

CREATE TABLE `zm_financial`.`capitalpool_redis` (
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
  `level` TINYINT UNSIGNED DEFAULT 0 COMMENT '星级',
  `status` TINYINT UNSIGNED NULL DEFAULT 1 COMMENT '状态0停用，1启用',
  `create_time` DATETIME NULL COMMENT '创建时间', 
  PRIMARY KEY (`id`),
  INDEX `idx_center_id` (`center_id`),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC)) 
  ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 
COMMENT = '资金池Redis,如果redis崩溃后资金池回滚';

alter table withdrawals_detail change operator_id grade_id INT NULL COMMENT 'gradeId';

alter table bind_card change type_id grade_id INT NULL COMMENT 'gradeId';

alter table capitalpool modify level TINYINT UNSIGNED default 0 COMMENT '星级';