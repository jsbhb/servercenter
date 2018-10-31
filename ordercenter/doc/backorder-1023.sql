alter table order_detail add rebate_fee DECIMAL(10,2) default null comment '返佣支付金额';

drop table if exists  `rebate_order`;

CREATE TABLE `zm_order`.`rebate_order` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `grade_id` INT NULL COMMENT 'gradeID',
  `order_id` VARCHAR(200) NOT NULL COMMENT '订单号',
  `money` DECIMAL(12,2) DEFAULT 0 COMMENT '返佣支付金额',
  `remark` VARCHAR(200) NULL COMMENT '备注',
  `create_time` DATETIME NULL COMMENT '注册时间', 
  `update_time` DATETIME NULL COMMENT '更新时间',
  `opt` VARCHAR(20) NULL COMMENT '操作人',
  PRIMARY KEY (`id`),
  UNIQUE INDEX `uk_orderId` (`order_id` ASC),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC)) 
  ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 
COMMENT = '返佣支付订单,如果财务中心记录出错，先记录在此处';
