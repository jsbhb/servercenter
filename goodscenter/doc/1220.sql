use zm_goods;
alter table goods_second_category add column tag_path varchar(200) null comment '图标路径';
alter table goods_third_category add column tag_path varchar(200) null comment '图标路径';
alter table component_page add column name varchar(200) NULL comment '模块别名';

alter table goods_price add column line_price DECIMAL(10,2) NULL DEFAULT 0.0 comment '商品划线价';
alter table goods_item add column sale_num int(11) DEFAULT 0 comment '商品销量';

drop table if exists  `big_sale_record`;

CREATE TABLE `zm_goods`.`big_sale_record` (
  `id` int UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `item_id` varchar(50) NOT NULL COMMENT '商品编号',
  `new_rebate` DECIMAL(12,6) NULL COMMENT '特卖时的返佣',
  `new_retail_price` DECIMAL(10,2) NULL COMMENT '特卖时的价格',
  `old_rebate` DECIMAL(12,6) NULL COMMENT '原来的返佣',
  `old_retail_price` DECIMAL(10,2) NULL COMMENT '原来的零售价',
  `line_price` DECIMAL(10,2) NULL COMMENT '划线价',
  `pic_path` VARCHAR(200) NOT NULL COMMENT '主图地址',
  `year` int(11) DEFAULT NULL COMMENT '年份',
  `week` int(11) DEFAULT NULL COMMENT '周期',
  `sort` tinyint(5) DEFAULT NULL COMMENT '顺序',
  `goods_id` varchar(50) NOT NULL COMMENT '商品id',
  `start_time` DATETIME NULL COMMENT '启用时间',
  `create_time` DATETIME NULL COMMENT '注册时间',
  `update_time` DATETIME NULL COMMENT '更新时间',
  `opt` VARCHAR(20) NULL COMMENT '操作人',
  PRIMARY KEY (`id`),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC),
  INDEX `idx_item_id` (`item_id`),
  INDEX `idx_year` (`year`),
  INDEX `idx_week` (`week`),
  INDEX `idx_sort` (`sort`),
  INDEX `idx_goods_id` (`goods_id`)) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 
COMMENT = '特价商品记录表';