alter table express_rule add parameter varchar(100) null comment '参数名称，json串';
alter table rule_template add parameter_id int unsigned null comment '规则参数ID';
alter table express_template add rule_name varchar(500) null comment '规则描述';

drop table if exists  `zm_order`.`rule_parameter`;

CREATE TABLE `zm_order`.`rule_parameter` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `rule_id` INT UNSIGNED NULL comment '规则ID',
  `parameter_value` varchar(200) NULL comment '规则参数，json串',
  `create_time` DATETIME NULL,
  `update_time` DATETIME NULL,
  `remark` VARCHAR(200) NULL,
  `opt` VARCHAR(30) NULL,
  PRIMARY KEY (`id`),
  INDEX `idx_ruleId` (`rule_id` ASC)
  )ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 
COMMENT = '规则参数';
