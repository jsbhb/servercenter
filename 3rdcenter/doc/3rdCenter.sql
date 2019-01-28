drop SCHEMA if exists  `zm_thirdplugin`;
CREATE SCHEMA `zm_thirdplugin` ;


use zm_thirdplugin;



drop table if exists  `zm_thirdplugin`.`wxlogin_config`;

CREATE TABLE `zm_thirdplugin`.`wxlogin_config` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `center_id` INT NULL,
  `login_type` TINYINT UNSIGNED NOT NULL COMMENT '登录类型：1：公众号，2：手机网站，3：PC网站，4：app',
  `secret` varchar(100) NOT NULL COMMENT '',
  `app_id` varchar(100) NOT NULL COMMENT 'appID',
  `attribute` varchar(100) NULL COMMENT '备用',
  PRIMARY KEY (`id`)
  )ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 
COMMENT = '微信登录配置信息';
