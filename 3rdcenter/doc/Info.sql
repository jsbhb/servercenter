use zm_thirdplugin;

drop table if exists  `zm_thirdplugin`.`info`;
CREATE TABLE `zm_thirdplugin`.`info` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `info_title` varchar(150) NULL COMMENT '文章标题',
  `type` tinyint unsigned NULL COMMENT '0:商品介绍',
  `invite_path` varchar(250) NULL COMMENT '访问链接',
  `cover_pic` varchar(250) NULL COMMENT '封面路径',
  `introduction` varchar(350) NULL COMMENT '介绍',
  `attr` varchar(100) DEFAULT NULL COMMENT '备用',
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `opt` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8 COMMENT='商品介绍文章存储';