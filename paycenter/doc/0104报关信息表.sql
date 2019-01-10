drop table if exists  `zm_payment`.`custom_config`;

CREATE TABLE `zm_payment`.`custom_config` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `supplier_id` INT NOT NULL,
  `merchant_customs_code` varchar(200) NOT NULL COMMENT '商户在海关备案的编号',
  `merchant_customs_name` varchar(200) NOT NULL COMMENT '商户海关备案名称',
  `customs_place` varchar(200)  NULL COMMENT '海关编号',
  `backup` varchar(200)  NULL COMMENT '备用1',
  `remark` varchar(200)  NULL COMMENT '备注',
  `create_time` DATETIME NULL COMMENT '注册时间', 
  `update_time` DATETIME NULL COMMENT '更新时间',
  `opt` VARCHAR(20) NULL COMMENT '操作人',
  PRIMARY KEY (`id`)
  )ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 
COMMENT = '支付单报关信息';