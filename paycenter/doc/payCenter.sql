drop SCHEMA if exists  `zm_payment`;
CREATE SCHEMA `zm_payment` ;


use zm_payment;



drop table if exists  `zm_payment`.`alipay_config`;

CREATE TABLE `zm_payment`.`alipay_config` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `center_id` INT NOT NULL,
  `pid` char(16)  NULL COMMENT '合作者身份ID',
  `app_id` char(32)  NULL COMMENT 'appID',
  `app_key` char(50)  NULL COMMENT 'MD5Key',
  `rsa_private_key` varchar(1000)  NULL COMMENT 'rsa私钥',
  `rsa_public_key` varchar(1000)  NULL COMMENT 'rsa公钥',  
  PRIMARY KEY (`id`)
  )ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 
COMMENT = '支付宝信息';


drop table if exists  `zm_payment`.`wxpay_config`;

CREATE TABLE `zm_payment`.`wxpay_config` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `center_id` INT NOT NULL,
  `cert_path` varchar(200) NULL COMMENT '证书路径',
  `app_id` varchar(50)  NULL COMMENT 'appid',
  `mch_id` varchar(50)  NULL COMMENT '商户ID',
  `api_key` varchar(100)  NULL COMMENT 'key',
  PRIMARY KEY (`id`)
  )ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 
COMMENT = '微信信息';


drop table if exists  `zm_payment`.`unionpay_config`;

CREATE TABLE `zm_payment`.`unionpay_config` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `center_id` INT NOT NULL,
  `mer_id` varchar(50) NULL COMMENT '商户ID',
  `tr_id` varchar(50) NULL COMMENT 'token支付ID',
  `signCert_path` varchar(200) NULL COMMENT '签名证书路径',
  `signCert_pwd` varchar(50)  NULL COMMENT '签名证书密码',
  `encryptCert_path` varchar(200)  NULL COMMENT '敏感信息证书路径',
  `middleCert_path` varchar(200)  NULL COMMENT '中级证书路径',
  `rootCert_path` varchar(200)  NULL COMMENT '根证书路径',
  PRIMARY KEY (`id`)
  )ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 
COMMENT = '银联配置信息';