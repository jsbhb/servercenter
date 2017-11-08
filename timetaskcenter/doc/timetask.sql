drop SCHEMA if exists  `zm_timetask`;

CREATE SCHEMA `zm_timetask` ;

use zm_timetask;

drop table if exists  `zm_timetask`.`schedulejob`;

CREATE TABLE `zm_timetask`.`schedulejob` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `job_name` VARCHAR(45) NOT NULL COMMENT '任务名称',
  `job_status` TINYINT UNSIGNED NULL DEFAULT 0 COMMENT '0:禁用；1：启用',
  `cron_expression` VARCHAR(45) NOT NULL COMMENT '定时时间表达式',
  `concurrent` TINYINT UNSIGNED NULL DEFAULT 0 COMMENT '0：不允许并发；1：允许',
  `description` VARCHAR(1000) NULL COMMENT '描述',
  `job_group` VARCHAR(45) NOT NULL,
  `target_object` VARCHAR(100) NOT NULL COMMENT '目标任务类',
  `target_method` VARCHAR(100) NULL,
  `create_time` DATETIME NULL,
  `update_time` DATETIME NULL,
  `opt` VARCHAR(45) NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `uq_index` (`job_name` ASC, `job_group` ASC),
  INDEX `status_idx` (`job_status` ASC)
  )ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 
COMMENT = '任务调度表';

