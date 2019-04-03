drop table if exists  `zm_payment`.`pay_origin_data`;

CREATE TABLE `zm_payment`.`pay_origin_data` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `order_id` varchar(30) NOT NULL,
  `pay_type` tinyint NOT NULL COMMENT '支付方式1/微信，2、支付宝',
  `type` tinyint NOT NULL COMMENT '报文类型；1：支付请求报文,2:支付返回报文',
  `content` text NULL COMMENT '支付原始报文',
  `remark` varchar(200)  NULL COMMENT '备注',
  `create_time` DATETIME NULL COMMENT '创建时间', 
  `update_time` DATETIME NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  INDEX idx_orderId (order_id)
  )ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 
COMMENT = '支付原始数据';

alter table zm_payment.custom_config add column wx_customs_place varchar(30) comment '微信申报地址';