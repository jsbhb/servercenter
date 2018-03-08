drop SCHEMA if exists  `zm_log`;
CREATE SCHEMA `zm_log` ;


use zm_log;



drop table if exists  `zm_log`.`user_center_log`;

CREATE TABLE `zm_log`.`user_center_log`(
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `center_id` INT NULL COMMENT '区域中心ID',
  `shop_id` INT NULL COMMENT '店铺ID',
  `source` TINYINT UNSIGNED NULL COMMENT '0后台，1前端',
  `type` TINYINT UNSIGNED NULL COMMENT '0新增，1删除，2修改',
  `method_name` VARCHAR(100) NULL COMMENT '方法名称',
  `call_ip` VARCHAR(100) NULL COMMENT 'ip',
  `parameter` VARCHAR(2000) NULL COMMENT '参数',
  `content` VARCHAR(1000) NULL,
  `create_time` DATETIME NULL,
  `opt` VARCHAR(20) NULL,
  PRIMARY KEY (`id`),
  INDEX `idx_centerId` (`center_id` ASC),
  INDEX `idx_source` (`source` ASC),
  INDEX `idx_createTime` (`create_time` ASC),
  INDEX `idx_type` (`type` ASC)
  )ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 
COMMENT = '用户中心日志信息';

drop table if exists  `zm_log`.`order_center_log`;

CREATE TABLE `zm_log`.`order_center_log`(
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `center_id` INT NULL COMMENT '区域中心ID',
  `shop_id` INT NULL COMMENT '店铺ID',
  `source` TINYINT UNSIGNED NULL COMMENT '0后台，1前端',
  `type` TINYINT UNSIGNED NULL COMMENT '0新增，1删除，2修改',
  `method_name` VARCHAR(100) NULL COMMENT '方法名称',
  `call_ip` VARCHAR(100) NULL COMMENT 'ip',
  `parameter` VARCHAR(2000) NULL COMMENT '参数',
  `content` VARCHAR(1000) NULL,
  `create_time` DATETIME NULL,
  `opt` VARCHAR(20) NULL,
  PRIMARY KEY (`id`),
  INDEX `idx_centerId` (`center_id` ASC),
  INDEX `idx_source` (`source` ASC),
  INDEX `idx_createTime` (`create_time` ASC),
  INDEX `idx_type` (`type` ASC)
  )ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 
COMMENT = '订单中心日志信息';

drop table if exists  `zm_log`.`pay_center_log`;

CREATE TABLE `zm_log`.`pay_center_log`(
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `center_id` INT NULL COMMENT '区域中心ID',
  `shop_id` INT NULL COMMENT '店铺ID',
  `source` TINYINT UNSIGNED NULL COMMENT '0后台，1前端',
  `type` TINYINT UNSIGNED NULL COMMENT '0新增，1删除，2修改',
  `method_name` VARCHAR(100) NULL COMMENT '方法名称',
  `call_ip` VARCHAR(100) NULL COMMENT 'ip',
  `parameter` VARCHAR(2000) NULL COMMENT '参数',
  `content` VARCHAR(1000) NULL,
  `create_time` DATETIME NULL,
  `opt` VARCHAR(20) NULL,
  PRIMARY KEY (`id`),
  INDEX `idx_centerId` (`center_id` ASC),
  INDEX `idx_source` (`source` ASC),
  INDEX `idx_createTime` (`create_time` ASC),
  INDEX `idx_type` (`type` ASC)
  )ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 
COMMENT = '支付中心日志信息';

drop table if exists  `zm_log`.`goods_center_log`;

CREATE TABLE `zm_log`.`goods_center_log`(
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `center_id` INT NULL COMMENT '区域中心ID',
  `shop_id` INT NULL COMMENT '店铺ID',
  `source` TINYINT UNSIGNED NULL COMMENT '0后台，1前端',
  `type` TINYINT UNSIGNED NULL COMMENT '0新增，1删除，2修改',
  `method_name` VARCHAR(100) NULL COMMENT '方法名称',
  `call_ip` VARCHAR(100) NULL COMMENT 'ip',
  `parameter` VARCHAR(2000) NULL COMMENT '参数',
  `content` VARCHAR(1000) NULL,
  `create_time` DATETIME NULL,
  `opt` VARCHAR(20) NULL,
  PRIMARY KEY (`id`),
  INDEX `idx_centerId` (`center_id` ASC),
  INDEX `idx_source` (`source` ASC),
  INDEX `idx_createTime` (`create_time` ASC),
  INDEX `idx_type` (`type` ASC)
  )ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 
COMMENT = '商品中心日志信息';

drop table if exists  `zm_log`.`auth_center_log`;

CREATE TABLE `zm_log`.`auth_center_log`(
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `center_id` INT NULL COMMENT '区域中心ID',
  `shop_id` INT NULL COMMENT '店铺ID',
  `source` TINYINT UNSIGNED NULL COMMENT '0后台，1前端',
  `type` TINYINT UNSIGNED NULL COMMENT '0新增，1删除，2修改',
  `method_name` VARCHAR(100) NULL COMMENT '方法名称',
  `call_ip` VARCHAR(100) NULL COMMENT 'ip',
  `parameter` VARCHAR(2000) NULL COMMENT '参数',
  `content` VARCHAR(1000) NULL,
  `create_time` DATETIME NULL,
  `opt` VARCHAR(20) NULL,
  PRIMARY KEY (`id`),
  INDEX `idx_centerId` (`center_id` ASC),
  INDEX `idx_source` (`source` ASC),
  INDEX `idx_createTime` (`create_time` ASC),
  INDEX `idx_type` (`type` ASC)
  )ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 
COMMENT = '权限中心日志信息';

drop table if exists  `zm_log`.`third_center_log`;

CREATE TABLE `zm_log`.`third_center_log`(
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `center_id` INT NULL COMMENT '区域中心ID',
  `shop_id` INT NULL COMMENT '店铺ID',
  `source` TINYINT UNSIGNED NULL COMMENT '0后台，1前端',
  `type` TINYINT UNSIGNED NULL COMMENT '0新增，1删除，2修改',
  `method_name` VARCHAR(100) NULL COMMENT '方法名称',
  `call_ip` VARCHAR(100) NULL COMMENT 'ip',
  `parameter` VARCHAR(2000) NULL COMMENT '参数',
  `content` VARCHAR(1000) NULL,
  `create_time` DATETIME NULL,
  `opt` VARCHAR(20) NULL,
  PRIMARY KEY (`id`),
  INDEX `idx_centerId` (`center_id` ASC),
  INDEX `idx_source` (`source` ASC),
  INDEX `idx_createTime` (`create_time` ASC),
  INDEX `idx_type` (`type` ASC)
  )ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 
COMMENT = '第三方服务中心日志信息';

drop table if exists  `zm_log`.`activity_center_log`;

CREATE TABLE `zm_log`.`activity_center_log`(
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `center_id` INT NULL COMMENT '区域中心ID',
  `shop_id` INT NULL COMMENT '店铺ID',
  `source` TINYINT UNSIGNED NULL COMMENT '0后台，1前端',
  `type` TINYINT UNSIGNED NULL COMMENT '0新增，1删除，2修改',
  `method_name` VARCHAR(100) NULL COMMENT '方法名称',
  `call_ip` VARCHAR(100) NULL COMMENT 'ip',
  `parameter` VARCHAR(2000) NULL COMMENT '参数',
  `content` VARCHAR(1000) NULL,
  `create_time` DATETIME NULL,
  `opt` VARCHAR(20) NULL,
  PRIMARY KEY (`id`),
  INDEX `idx_centerId` (`center_id` ASC),
  INDEX `idx_source` (`source` ASC),
  INDEX `idx_createTime` (`create_time` ASC),
  INDEX `idx_type` (`type` ASC)
  )ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 
COMMENT = '活动中心日志信息';

drop table if exists  `zm_log`.`supplier_center_log`;

CREATE TABLE `zm_log`.`supplier_center_log`(
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `center_id` INT NULL COMMENT '区域中心ID',
  `shop_id` INT NULL COMMENT '店铺ID',
  `source` TINYINT UNSIGNED NULL COMMENT '0后台，1前端',
  `type` TINYINT UNSIGNED NULL COMMENT '0新增，1删除，2修改',
  `method_name` VARCHAR(100) NULL COMMENT '方法名称',
  `call_ip` VARCHAR(100) NULL COMMENT 'ip',
  `parameter` VARCHAR(2000) NULL COMMENT '参数',
  `content` VARCHAR(1000) NULL,
  `create_time` DATETIME NULL,
  `opt` VARCHAR(20) NULL,
  PRIMARY KEY (`id`),
  INDEX `idx_centerId` (`center_id` ASC),
  INDEX `idx_source` (`source` ASC),
  INDEX `idx_createTime` (`create_time` ASC),
  INDEX `idx_type` (`type` ASC)
  )ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 
COMMENT = '供应商中心日志信息';

drop table if exists  `zm_log`.`timetask_center_log`;

CREATE TABLE `zm_log`.`timetask_center_log`(
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `center_id` INT NULL COMMENT '区域中心ID',
  `shop_id` INT NULL COMMENT '店铺ID',
  `source` TINYINT UNSIGNED NULL COMMENT '0后台，1前端',
  `type` TINYINT UNSIGNED NULL COMMENT '0新增，1删除，2修改',
  `method_name` VARCHAR(100) NULL COMMENT '方法名称',
  `call_ip` VARCHAR(100) NULL COMMENT 'ip',
  `parameter` VARCHAR(2000) NULL COMMENT '参数',
  `content` VARCHAR(1000) NULL,
  `create_time` DATETIME NULL,
  `opt` VARCHAR(20) NULL,
  PRIMARY KEY (`id`),
  INDEX `idx_centerId` (`center_id` ASC),
  INDEX `idx_source` (`source` ASC),
  INDEX `idx_createTime` (`create_time` ASC),
  INDEX `idx_type` (`type` ASC)
  )ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 
COMMENT = '定时器中心日志信息';


drop table if exists  `zm_log`.`exception_log`;

CREATE TABLE `zm_log`.`exception_log`(
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `server_id` INT NULL COMMENT '微服务ID',
  `server_name` VARCHAR(20) NULL COMMENT '微服务名称',
  `method_name` VARCHAR(100) NULL COMMENT '方法名称',
  `content` VARCHAR(2000) NULL,
  `create_time` DATETIME NULL,
  `attr` VARCHAR(2000) NULL,
  PRIMARY KEY (`id`),
  INDEX `idx_serverId` (`server_id` ASC),
  INDEX `idx_createTime` (`create_time` ASC)
  )ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 
COMMENT = '错误日志信息';


drop table if exists  `zm_log`.`open_inf_log`;

CREATE TABLE `zm_log`.`open_inf_log`(
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `center_id` INT NULL COMMENT '区域中心ID',
  `server_id` INT NULL COMMENT '微服务ID',
  `server_name` VARCHAR(20) NULL COMMENT '微服务名称',
  `method_name` VARCHAR(100) NULL COMMENT '方法名称',
  `call_ip` VARCHAR(100) NULL COMMENT 'ip',
  `parameter` VARCHAR(2000) NULL COMMENT '参数',
  `content` VARCHAR(2000) NULL,
  `create_time` DATETIME NULL,
  `attr` VARCHAR(2000) NULL,
  PRIMARY KEY (`id`),
  INDEX `idx_serverId` (`server_id` ASC),
  INDEX `idx_centerId` (`center_id` ASC),
  INDEX `idx_createTime` (`create_time` ASC)
  )ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 
COMMENT = '对接日志信息';




drop table if exists  `zm_log`.`log_api`;

CREATE TABLE `zm_log`.`log_api`(
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `api_url` VARCHAR(100) NOT NULL,
  `name` VARCHAR(100) NOT NULL,
  `create_time` DATETIME NULL,
  `update_time` DATETIME NULL,
  `opt` VARCHAR(20) NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `api_url_UNIQUE` (`api_url` ASC),
  UNIQUE INDEX `name_UNIQUE` (`name` ASC)
  )ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 
COMMENT = '日志API';




drop table if exists  `zm_log`.`log_search_parameter`;

CREATE TABLE `zm_log`.`log_search_parameter`(
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `api_name` VARCHAR(100) NOT NULL,
  `key_name` VARCHAR(45) NOT NULL COMMENT '字段中文名',
  `key` VARCHAR(45) NULL,
  `value` VARCHAR(100) NULL,
  `html_tag` VARCHAR(30) NULL,
  `type` TINYINT UNSIGNED NULL,
  `create_time` DATETIME NULL,
  `update_time` DATETIME NULL,
  `opt` VARCHAR(20) NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `uk_apiName` (`api_name` ASC),
  INDEX `idx_apiName` (`api_name` ASC)
  )ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 
COMMENT = '日志字段表';

drop table if exists  `zm_log`.`coop_bakc_log`;

CREATE TABLE `zm_log`.`coop_bakc_log`(
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `center_id` INT NULL COMMENT '区域中心ID',
  `shop_id` INT NULL COMMENT '店铺ID',
  `source` TINYINT UNSIGNED NULL COMMENT '0后台，1前端',
  `type` TINYINT UNSIGNED NULL COMMENT '0新增，1删除，2修改',
  `method_name` VARCHAR(100) NULL COMMENT '方法名称',
  `call_ip` VARCHAR(100) NULL COMMENT 'ip',
  `parameter` VARCHAR(2000) NULL COMMENT '参数',
  `content` VARCHAR(1000) NULL,
  `create_time` DATETIME NULL,
  `opt` VARCHAR(20) NULL,
  PRIMARY KEY (`id`),
  INDEX `idx_centerId` (`center_id` ASC),
  INDEX `idx_source` (`source` ASC),
  INDEX `idx_createTime` (`create_time` ASC),
  INDEX `idx_type` (`type` ASC)
  )ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 
COMMENT = '统一后台日志信息';
