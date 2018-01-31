drop SCHEMA if exists  `zm_log`;
CREATE SCHEMA `zm_log` ;


use zm_log;



drop table if exists  `zm_log`.`log`;

CREATE TABLE `zm_log`.`log`(
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `center_id` INT NOT NULL COMMENT '服务中心ID',
  `client_id` INT NOT NULL COMMENT '客户端ID',
  `api_id` TINYINT UNSIGNED NOT NULL,
  `center_name` VARCHAR(20) NULL,
  `api_name` VARCHAR(20) NULL,
  `content` VARCHAR(1000) NULL,
  `create_time` DATETIME NULL,
  `opt` VARCHAR(20) NULL,
  PRIMARY KEY (`id`),
  INDEX `idx_centerId` (`center_id` ASC),
  INDEX `idx_createTime` (`create_time` ASC),
  INDEX `idx_apiId` (`api_id` ASC)
  )ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 
COMMENT = '日志信息';




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
