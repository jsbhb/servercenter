drop table if exists  `zm_thirdplugin`.`customs_request`;
CREATE TABLE `zm_thirdplugin`.`customs_request` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `order_id` varchar(100) NOT NULL COMMENT '订单号',
  `session_id` varchar(100) NOT NULL COMMENT '海关调用的sessionId',
  `service_time` varchar(15) NOT NULL COMMENT '海关调用时间戳，毫秒',
  `status` tinyint NOT NULL default 0 COMMENT '状态：0：未处理，1已处理，2失败',
  `remark` text NULL COMMENT '备注',
  `create_time` DATETIME NULL,
  `update_time` DATETIME NULL,
  PRIMARY KEY (`id`)
  )ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 
COMMENT = '海关调用请求实时支付数据记录';