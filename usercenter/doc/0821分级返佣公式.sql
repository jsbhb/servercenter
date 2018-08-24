CREATE TABLE `zm_user`.`grade_type_rebate_formula` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `grade_type_id` INT NULL COMMENT '分级类型ID',
  `formula` VARCHAR(200) NULL COMMENT '公式字符串',
  `status` TINYINT UNSIGNED NULL DEFAULT 1 COMMENT '状态0禁用，1使用',
  `backup` VARCHAR(200) NULL DEFAULT NULL COMMENT '备用',
  `remark` VARCHAR(100)  DEFAULT NULL COMMENT '备注',
  `create_time` DATETIME NULL COMMENT '注册时间', 
  `update_time` DATETIME NULL COMMENT '更新时间',
  `opt` VARCHAR(50) NULL COMMENT '操作人',
  PRIMARY KEY (`id`),
  INDEX `idx_grade_type_id` (`grade_type_id`),
  INDEX `idx_status` (`status`),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC)) 
  ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 
COMMENT = '分级返佣公式';
