drop table if exists  `rebate_order`;

CREATE TABLE `zm_financial`.`rebate_order` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `grade_id` INT NULL COMMENT 'gradeID',
  `orderId` VARCHAR(200) NOT NULL COMMENT '订单号',
  `money` DECIMAL(12,2) DEFAULT 0 COMMENT '返佣支付金额',
  `remark` VARCHAR(200) NULL COMMENT '备注',
  `create_time` DATETIME NULL COMMENT '注册时间', 
  `update_time` DATETIME NULL COMMENT '更新时间',
  `opt` VARCHAR(20) NULL COMMENT '操作人',
  PRIMARY KEY (`id`),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC)) 
  ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 
COMMENT = '返佣支付订单';


alter table rebate add column already_check DECIMAL(12,2) default null comment '已对账的金额'