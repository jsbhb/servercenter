use zm_thirdplugin;

drop table if exists  `zm_thirdplugin`.`express_interface`;
CREATE TABLE `zm_thirdplugin`.`express_interface` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `express_code` varchar(50) NOT NULL COMMENT '快递公司代码',
  `target_object` varchar(50) NOT NULL COMMENT '目标类',
  `app_key` varchar(100) NOT NULL COMMENT 'app_key',
  `app_secret` varchar(100) NOT NULL COMMENT 'app_secret',
  `url` varchar(200) DEFAULT NULL COMMENT 'url',
  `trade_id` varchar(50) DEFAULT NULL COMMENT 'trade_id',
  `version` varchar(50) DEFAULT NULL COMMENT 'version',
  `buz_type` varchar(50) DEFAULT NULL COMMENT 'buz_type',
  `format` varchar(50) DEFAULT NULL COMMENT 'format',
  `retry_limit` INT UNSIGNED NULL DEFAULT 0 COMMENT 'retry_limit',
  `attr` varchar(100) DEFAULT NULL COMMENT '备用',
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `opt` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `express_code_UNIQUE` (`express_code`),
  KEY `idx_express_code` (`express_code`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8 COMMENT='快递公司接口信息';