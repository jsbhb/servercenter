alter table goods_item add is_tmpdown tinyint(4) not null default 0 comment "是否临时下架，0否；1是";

alter table goods_item add index idx_is_tmpdown (is_tmpdown);

drop table if exists  `goods_shelve_record`;

CREATE TABLE `zm_goods`.`goods_shelve_record` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `create_time` DATETIME NULL COMMENT '记录时间', 
  `shelve_type` TINYINT UNSIGNED NULL DEFAULT 0 COMMENT '类型，0：上架;1：下架',
  `shelve_mode` TINYINT UNSIGNED NULL DEFAULT 0 COMMENT '模式，0：自动',
  `number` INT NULL DEFAULT 0 COMMENT '数量',
  `detail` TEXT NULL COMMENT '明细',
  PRIMARY KEY (`id`),
  INDEX `idx_create_time` (`create_time`),
  INDEX `idx_shelve_type` (`shelve_type`),
  INDEX `idx_shelve_mode` (`shelve_mode`),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC)) 
  ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 
COMMENT = '商品上下架记录表';