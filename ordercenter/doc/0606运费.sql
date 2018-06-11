alter table express_fee add column template_id int unsigned comment '模板ID';
alter table express_fee_2 add column template_id int unsigned comment '模板ID';
alter table free_express_fee add column supplier_id int unsigned comment '供应商ID';
alter table free_express_fee_2 add column supplier_id int unsigned comment '供应商ID';
alter table express_fee modify column express_key varchar(50) comment '快递公司编号';
alter table express_fee modify column name varchar(50) comment '快递公司名称';
alter table express_fee_2 modify column express_key varchar(50) comment '快递公司编号';
alter table express_fee_2 modify column name varchar(50) comment '快递公司名称';

drop table if exists  `zm_order`.`express_template`;

CREATE TABLE `zm_order`.`express_template` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `supplier_id` INT UNSIGNED NOT NULL,
  `platform` INT UNSIGNED NULL comment '平台，大贸可能有不一样的模板',
  `free_post_fee` tinyint unsigned default 0 comment '0不包邮，1包邮，2邮费到付',
  `free_tax_fee` tinyint unsigned default 0 comment '0不包税，1包税',
  `enable` tinyint unsigned default 0 comment '0未启用，1启用',
  `template_name` VARCHAR(100) NULL comment '模板名称',
  `attr` VARCHAR(200) NULL,
  `create_time` DATETIME NULL,
  `update_time` DATETIME NULL,
  `remark` VARCHAR(200) NULL,
  `opt` VARCHAR(30) NULL,
  PRIMARY KEY (`id`),
  INDEX `idx_supplierId` (`supplier_id` ASC)
  )ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 
COMMENT = '运费模板';

drop table if exists  `zm_order`.`express_rule`;

CREATE TABLE `zm_order`.`express_rule` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `rule_description` VARCHAR(100) NULL comment '规则描述',
  `attr` VARCHAR(200) NULL,
  `create_time` DATETIME NULL,
  `update_time` DATETIME NULL,
  `remark` VARCHAR(200) NULL,
  `opt` VARCHAR(30) NULL,
  PRIMARY KEY (`id`)
  )ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 
COMMENT = '运费模板规则';

drop table if exists  `zm_order`.`rule_template`;

CREATE TABLE `zm_order`.`rule_template` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `rule_id` INT UNSIGNED NULL comment '规则ID',
  `template_id` INT UNSIGNED NULL comment '模板ID',
  `create_time` DATETIME NULL,
  `update_time` DATETIME NULL,
  `remark` VARCHAR(200) NULL,
  `opt` VARCHAR(30) NULL,
  PRIMARY KEY (`id`),
  INDEX `idx_templateId` (`template_id` ASC)
  )ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 
COMMENT = '运费模板规则';

