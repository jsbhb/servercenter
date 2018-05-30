drop table if exists  `shopkeeper`;

CREATE TABLE `zm_user`.`shopkeeper` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `shop_name` VARCHAR(100) NULL COMMENT '店铺名称',
  `province` VARCHAR(20) NOT NULL COMMENT '省',
  `city` VARCHAR(20) NOT NULL COMMENT '市',
  `area` VARCHAR(20) NULL COMMENT '区',
  `address` VARCHAR(200) NOT NULL COMMENT '地址',
  `shop_path` VARCHAR(300) NULL COMMENT '店铺链接',
  `pic_path` VARCHAR(100) NULL COMMENT '店主图片',
  `keeper_name` VARCHAR(100) NULL COMMENT '店主姓名',
  `occupation` VARCHAR(100) NULL COMMENT '职业',
  `sales` DECIMAL(12,2) NULL COMMENT '销售额',
  `phone` char(15) NULL COMMENT '手机',
  `level` tinyint unsigned NULL COMMENT '等级',
  `attribute` VARCHAR(100) COMMENT '备用',
  `create_time` DATETIME NULL,
  `update_time` DATETIME NULL COMMENT '更新时间',
  `opt` VARCHAR(100) COMMENT '操作人',
  PRIMARY KEY (`id`),
  INDEX `idx_shop_name` (`shop_name`)
  ) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 
COMMENT = '微店主';


drop table if exists  `partner`;

CREATE TABLE `zm_user`.`partner` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `partner_name` VARCHAR(100) NULL COMMENT '企业名称',
  `province` VARCHAR(20) NOT NULL COMMENT '省',
  `city` VARCHAR(20) NOT NULL COMMENT '市',
  `area` VARCHAR(20) NULL COMMENT '区',
  `address` VARCHAR(200) NOT NULL COMMENT '地址',
  `aboutus_path` VARCHAR(300) NULL COMMENT '企业介绍链接',
  `pic_path` VARCHAR(100) NULL COMMENT '企业图片',
  `person_in_charge` VARCHAR(100) NULL COMMENT '负责人姓名',
  `phone` char(15) NULL COMMENT '手机',
  `attribute` VARCHAR(100) COMMENT '备用',
  `create_time` DATETIME NULL,
  `update_time` DATETIME NULL COMMENT '更新时间',
  `opt` VARCHAR(100) COMMENT '操作人',
  PRIMARY KEY (`id`),
  UNIQUE INDEX `partner_name_UNIQUE` (`partner_name`ASC),
  INDEX `idx_partner_name` (`partner_name`)
  ) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 
COMMENT = '合伙人';