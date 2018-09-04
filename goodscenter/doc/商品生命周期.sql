drop table if exists  `goods_life_cycle`;

CREATE TABLE `zm_goods`.`goods_life_cycle` (
  `id` int UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `item_id` varchar(100)  NOT NULL COMMENT 'itemId',
  `status` tinyint unsigned NOT NULL COMMENT '商品状态0下架(初始化)，1上架',
  `is_fx` tinyint unsigned NOT NULL COMMENT '是否分销',
  `welfare` tinyint unsigned NULL COMMENT '是否福利',
  `remark` varchar(100) NULL COMMENT '描述',
  `ip` varchar(100) NULL COMMENT 'ip',
  `create_time` DATETIME NULL COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC),
  INDEX `idx_itemId` (`item_id`),
  INDEX `idx_status` (`status`)) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 
COMMENT = '商品生命周期表';