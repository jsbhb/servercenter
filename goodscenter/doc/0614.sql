use zm_goods;

drop table if exists  `goods_extension_label`;

CREATE TABLE `zm_goods`.`goods_extension_label` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `goods_id` VARCHAR(50) NOT NULL COMMENT '商品ID',
  `goods_path` VARCHAR(200) NOT NULL COMMENT '商品图片路径',
  `goods_name` VARCHAR(100) NOT NULL COMMENT '商品名称',
  `brand` VARCHAR(100) NOT NULL COMMENT '商品品牌',
  `specs` VARCHAR(100) NOT NULL COMMENT '商品规格',
  `origin` VARCHAR(100) NOT NULL COMMENT '原产国',
  `use_age` VARCHAR(100) NOT NULL COMMENT '适用年龄',
  `reason` VARCHAR(200) NOT NULL COMMENT '推荐理由',
  `remark` VARCHAR(200) NULL COMMENT '备注',
  `create_time` DATETIME NULL COMMENT '注册时间', 
  `update_time` DATETIME NULL COMMENT '更新时间',
  `opt` VARCHAR(20) NULL COMMENT '操作人',
  PRIMARY KEY (`id`),
  INDEX `idx_goods_id` (`goods_id`),
  INDEX `idx_brand` (`brand`),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC),
  UNIQUE INDEX `goods_id_UNIQUE` (`goods_id` ASC)) 
  ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 
COMMENT = '商品推广标签表';