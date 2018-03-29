drop table if exists  `goods_tag`;

create table `zm_goods`.`goods_tag`(
  `id` int UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `tag_name` varchar(100)  NOT NULL COMMENT '标签名称',
  `priority` int UNSIGNED  NOT NULL default 1 COMMENT '优先级',
  `description` VARCHAR(200) NULL COMMENT '描述',
  `create_time` DATETIME NULL COMMENT '注册时间',
  `update_time` DATETIME NULL COMMENT '更新时间',
  `opt` VARCHAR(20) NULL COMMENT '操作人',
  PRIMARY KEY (`id`),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC),
  INDEX `tag_name` (`tag_name`)) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 
COMMENT = '商品标签库';

drop table if exists  `goods_tag_bind`;

create table `zm_goods`.`goods_tag_bind`(
  `id` int UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `goods_id` varchar(100)  NOT NULL COMMENT '商品编号',
  `tag_id` int UNSIGNED NOT NULL COMMENT '标签编号',
  `create_time` DATETIME NULL COMMENT '注册时间',
  `update_time` DATETIME NULL COMMENT '更新时间',
  `opt` VARCHAR(20) NULL COMMENT '操作人',
  PRIMARY KEY (`id`),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC),
  INDEX `idx_tagId` (`tag_id`),
  INDEX `idx_goodsId` (`goods_id`)) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 
COMMENT = '商品标签绑定表';