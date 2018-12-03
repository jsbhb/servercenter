use zm_goods;

CREATE TABLE `zm_goods`.`activity_info` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `name` VARCHAR(50) NOT NULL COMMENT '活动名称', 
  `type` TINYINT UNSIGNED NOT NULL DEFAULT 1 COMMENT '活动类型，1：砍价', 
  `description` VARCHAR(100) NULL COMMENT '活动描述', 
  `status` TINYINT UNSIGNED NOT NULL DEFAULT 0 COMMENT '活动状态，0：未开始;1：开始',
  `start_time` DATETIME NULL COMMENT '活动开始时间', 
  `end_time` DATETIME NULL COMMENT '活动结束时间', 
  `create_time` DATETIME NULL COMMENT '创建时间',
  `update_time` DATETIME NULL COMMENT '更新时间',
  `opt` VARCHAR(20) NULL COMMENT '操作人',
  PRIMARY KEY (`id`),
  INDEX `idx_name` (`name`),
  INDEX `idx_type` (`type`),
  INDEX `idx_status` (`status`),
  INDEX `idx_start_time` (`start_time`),
  INDEX `idx_end_time` (`end_time`),
  INDEX `idx_create_time` (`create_time`),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC)) 
  ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 
COMMENT = '活动信息记录表';

CREATE TABLE `zm_goods`.`activity_goods_role` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `activity_id` INT UNSIGNED NOT NULL COMMENT '活动ID',
  `item_id` VARCHAR(50) NOT NULL COMMENT '商品编号', 
  `init_price` DECIMAL(10,2) NOT NULL DEFAULT 0.0 COMMENT '商品原价', 
  `floor_price` DECIMAL(10,2) NOT NULL DEFAULT 0.0 COMMENT '商品底价', 
  `max_count` INT UNSIGNED NULL DEFAULT 0 COMMENT '该商品最多被砍几刀，0：无限',
  `first_min_ratio` INT UNSIGNED NULL DEFAULT 1 COMMENT '第一刀最小比例',
  `first_max_ratio` INT UNSIGNED NULL DEFAULT 1 COMMENT '第一刀最大比例',
  `min_ratio` INT UNSIGNED NULL DEFAULT 1 COMMENT '每砍一刀最小比例',
  `max_ratio` INT UNSIGNED NULL DEFAULT 1 COMMENT '每砍一刀最大比例',
  `type` INT UNSIGNED NULL DEFAULT 1 COMMENT '活动商品类型，1:普通，2：接龙',
  `duration` INT UNSIGNED NULL DEFAULT 1 COMMENT '持续时间（小时）',
  `less_min_price` DECIMAL(10,2) NOT NULL DEFAULT 0.0 COMMENT '现价和底价小于该值时直接砍到底价',
  `status` TINYINT UNSIGNED NOT NULL DEFAULT 0 COMMENT '商品活动状态，0：未开始;1：开始',
  `start_time` DATETIME NULL COMMENT '活动开始时间', 
  `end_time` DATETIME NULL COMMENT '活动结束时间', 
  `create_time` DATETIME NULL COMMENT '创建时间',
  `update_time` DATETIME NULL COMMENT '更新时间',
  `opt` VARCHAR(20) NULL COMMENT '操作人',
  PRIMARY KEY (`id`),
  INDEX `idx_activity_id` (`activity_id`),
  INDEX `idx_item_id` (`item_id`),
  INDEX `idx_type` (`type`),
  INDEX `idx_status` (`status`),
  INDEX `idx_start_time` (`start_time`),
  INDEX `idx_end_time` (`end_time`),
  INDEX `idx_create_time` (`create_time`),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC),
  UNIQUE INDEX `item_id_UNIQUE` (`item_id` ASC)) 
  ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 
COMMENT = '活动商品规则记录表';

CREATE TABLE `zm_goods`.`activity_goods_record` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `goods_role_id` INT UNSIGNED NOT NULL COMMENT '规则ID',
  `user_id` INT UNSIGNED NOT NULL COMMENT '用户编号', 
  `init_price` DECIMAL(10,2) NOT NULL DEFAULT 0.0 COMMENT '商品原价', 
  `floor_price` DECIMAL(10,2) NOT NULL DEFAULT 0.0 COMMENT '商品底价', 
  `status` TINYINT UNSIGNED NOT NULL DEFAULT 1 COMMENT '商品活动状态，0：结束;1：开始',
  `create_time` DATETIME NULL COMMENT '创建时间',
  `update_time` DATETIME NULL COMMENT '更新时间',
  `opt` VARCHAR(20) NULL COMMENT '操作人',
  PRIMARY KEY (`id`),
  INDEX `idx_goods_role_id` (`goods_role_id`),
  INDEX `idx_user_id` (`user_id`),
  INDEX `idx_status` (`status`),
  INDEX `idx_create_time` (`create_time`),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC),
  UNIQUE INDEX `goods_role_id_user_id_UNIQUE` (`goods_role_id`,`user_id`)) 
  ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 
COMMENT = '活动商品发起者记录表';

CREATE TABLE `zm_goods`.`activity_goods_participant` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `goods_record_id` INT UNSIGNED NOT NULL COMMENT '发起ID',
  `user_id` INT UNSIGNED NOT NULL COMMENT '用户编号', 
  `bargain_price` DECIMAL(10,2) NOT NULL DEFAULT 0.0 COMMENT '砍价金额', 
  `buy` INT UNSIGNED NOT NULL DEFAULT 0 COMMENT '接龙模式下是否已经购买， 0：未购买，1：已购买', 
  `create_time` DATETIME NULL COMMENT '创建时间',
  `update_time` DATETIME NULL COMMENT '更新时间',
  `opt` VARCHAR(20) NULL COMMENT '操作人',
  PRIMARY KEY (`id`),
  INDEX `idx_goods_record_id` (`goods_record_id`),
  INDEX `idx_user_id` (`user_id`),
  INDEX `idx_buy` (`buy`),
  INDEX `idx_create_time` (`create_time`),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC),
  UNIQUE INDEX `goods_record_id_user_id_UNIQUE` (`goods_record_id`,`user_id`)) 
  ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 
COMMENT = '活动商品参与者记录表';