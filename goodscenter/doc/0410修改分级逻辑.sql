drop table if exists  `goods_rebate`;

CREATE TABLE `goods_rebate` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `item_id` VARCHAR(50) NOT NULL COMMENT '商品明细ID',
  `grade_type` INT UNSIGNED NOT NULL COMMENT '客户类型',
  `proportion` decimal(10,2) DEFAULT 0 COMMENT '返佣比例',
  `remark` VARCHAR(200) NULL COMMENT '备注',
  `create_time` DATETIME NULL COMMENT '注册时间', 
  `update_time` DATETIME NULL COMMENT '更新时间',
  `opt` VARCHAR(20) NULL COMMENT '操作人',
  PRIMARY KEY (`id`),
  INDEX `idx_item_id` (`item_id`),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC),
  UNIQUE INDEX `item_id_UNIQUE` (`item_id`,`grade_type` ASC)) 
  ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 
COMMENT = '商品返佣表';