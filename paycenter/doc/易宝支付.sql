drop table if exists  `zm_payment`.`yop_config`;

CREATE TABLE `zm_payment`.`yop_config` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `center_id` INT NOT NULL,
  `parentMerchantNo` varchar(100)  NULL COMMENT '父商编',
  `merchantNo` varchar(100)  NULL COMMENT '子商编',
  `privatekey` varchar(2048)  NULL COMMENT '私钥',
  `publickey` varchar(2048)  NULL COMMENT '公钥',
  PRIMARY KEY (`id`)
  )ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 
COMMENT = '易宝支付信息';