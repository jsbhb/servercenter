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

alter table withdrawals_detail change operator_id grade_id INT NULL COMMENT 'gradeId';

alter table bind_card change type_id grade_id INT NULL COMMENT 'gradeId';