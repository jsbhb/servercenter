alter table goods add column is_free_tax tinyint UNSIGNED NOT NULL DEFAULT 0 COMMENT '是否包税0：否，1是';
alter table goods add index goods_id(goods_id);
CREATE UNIQUE INDEX uk_goodsId ON goods(goods_id);
alter table goods drop index idx_is_popular;
alter table goods drop index idx_is_new;
alter table goods drop index idx_is_hot;
alter table goods drop index idx_is_good;
alter table goods drop index idx_is_choice;

drop table if exists  `goods_tag`;

create table `zm_goods`.`goods_tag`(
  `id` int UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `tag_fun_id` int UNSIGNED NULL COMMENT '标签绑定的功能，默认无',
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
  `item_id` varchar(100)  NOT NULL COMMENT '商品编号',
  `tag_id` int UNSIGNED NOT NULL COMMENT '标签编号',
  `create_time` DATETIME NULL COMMENT '注册时间',
  `update_time` DATETIME NULL COMMENT '更新时间',
  `opt` VARCHAR(20) NULL COMMENT '操作人',
  PRIMARY KEY (`id`),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC),
  UNIQUE INDEX `item_tag` (`item_id`,`tag_id` ASC),
  INDEX `idx_tagId` (`tag_id`),
  INDEX `idx_goodsId` (`item_id`)) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 
COMMENT = '商品标签绑定表';

drop table if exists  `tag_fun`;

create table `zm_goods`.`tag_fun`(
  `id` int UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `fun_name` varchar(100)  NOT NULL COMMENT '功能名称',
  `create_time` DATETIME NULL COMMENT '注册时间',
  `update_time` DATETIME NULL COMMENT '更新时间',
  `opt` VARCHAR(20) NULL COMMENT '操作人',
  PRIMARY KEY (`id`),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC)) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 
COMMENT = '标签功能表';

insert into tag_fun(fun_name,create_time) values ('卡单功能',now());
insert into goods_tag(tag_fun_id,tag_name,priority,description) values ('1','预售','1','预售商品发货需要等待');