drop table if exists  `custom_order_express`;

CREATE TABLE `zm_order`.`custom_order_express` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `order_id` VARCHAR(100) NOT NULL COMMENT '订单号',
  `json_str` VARCHAR(1000) NULL COMMENT '订单对应的物流企业信息json串',
  `remark` VARCHAR(200) NULL COMMENT '备注',
  `create_time` DATETIME NULL COMMENT '注册时间', 
  `update_time` DATETIME NULL COMMENT '更新时间',
  `opt` VARCHAR(20) NULL COMMENT '操作人',
  PRIMARY KEY (`id`),
  UNIQUE INDEX `uk_orderId` (`order_id` ASC),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC)) 
  ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 
COMMENT = '订单对应的物流信息，备用，用于申报海关';
