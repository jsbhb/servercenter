drop table if exists  `zm_thirdplugin`.`page_view`;
CREATE TABLE `zm_thirdplugin`.`page_view` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `page_name` varchar(100) NULL COMMENT '页面名称',
  `type` TINYINT UNSIGNED NOT NULL COMMENT '0：访问页面；1：进入',
  `num` INT UNSIGNED NULL default 0 COMMENT '访问数量',
  `create_time` DATETIME NULL COMMENT '注册时间',
  `update_time` DATETIME NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
  )ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 
COMMENT = '页面访问数量';

drop table if exists  `zm_thirdplugin`.`unique_visitor`;
CREATE TABLE `zm_thirdplugin`.`unique_visitor` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `page_name` varchar(100) NULL COMMENT '页面名称',
  `ip_num` INT UNSIGNED NULL default 0 COMMENT 'ip数量',
  `create_time` DATETIME NULL COMMENT '注册时间',
  `update_time` DATETIME NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
  )ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 
COMMENT = 'IP数量';