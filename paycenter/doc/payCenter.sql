drop SCHEMA if exists  `zm_payment`;
CREATE SCHEMA `zm_payment` ;


use zm_payment;



drop table if exists  `zm_payment`.`alipay_config`;

CREATE TABLE `zm_payment`.`alipay_config` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `center_id` INT UNSIGNED NOT NULL,
  `pid` char(16) NOT NULL COMMENT '合作者身份ID',
  `app_id` char(32) NOT NULL COMMENT 'appID',
  `rsa_private_key` varchar(1000) NOT NULL COMMENT 'rsa私钥',
  `rsa_public_key` varchar(1000) NOT NULL COMMENT 'rsa公钥',  
  PRIMARY KEY (`id`)
  )ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 
COMMENT = '支付宝信息';


drop table if exists  `zm_payment`.`wxpay_config`;

CREATE TABLE `zm_payment`.`wxpay_config` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `center_id` INT UNSIGNED NOT NULL,
  `cert_path` varchar(200) NULL COMMENT '证书路径',
  `app_id` varchar(50) NOT NULL COMMENT 'appid',
  `mch_id` varchar(50) NOT NULL COMMENT '商户ID',
  `api_key` varchar(100) NOT NULL COMMENT 'key',
  PRIMARY KEY (`id`)
  )ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 
COMMENT = '微信信息';