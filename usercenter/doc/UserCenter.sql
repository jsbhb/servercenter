drop SCHEMA if exists  `usercenter`;
CREATE SCHEMA `usercenter` ;

use usercenter;

drop table if exists  `user`;

CREATE TABLE `usercenter`.`user` (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `account` VARCHAR(50) NULL COMMENT '账号',
  `phone` VARCHAR(15) NULL COMMENT '手机',
  `email` VARCHAR(100) NULL COMMENT '邮箱',
  `wechat` VARCHAR(100) NULL COMMENT '微信',
  `qq` VARCHAR(100) NULL COMMENT 'qq',
  `sina_blog` VARCHAR(100) NULL COMMENT '新浪微博',
  `pwd` VARCHAR(50) NULL COMMENT '密码',
  `parent_id` bigint UNSIGNED NULL COMMENT '上级ID',
  `band` TINYINT UNSIGNED NULL COMMENT '分销等级',
  `is_phone_validate` TINYINT UNSIGNED NULL DEFAULT 0 COMMENT '手机是否验证',
  `is_email_validate` TINYINT UNSIGNED NULL DEFAULT 0 COMMENT '邮箱是否验证',
  `status` TINYINT UNSIGNED NULL DEFAULT 0 COMMENT '用户状态',
  `register_center_id` bigint UNSIGNED NULL COMMENT '注册区域中心ID',
  `register_shop_id` bigint UNSIGNED NULL COMMENT '注册店中店ID',
  `create_time` DATETIME NULL COMMENT '注册时间',
  `update_time` DATETIME NULL COMMENT '更新时间',
  `opt` DATETIME NULL COMMENT '操作人',
  `last_login_time` DATETIME NULL COMMENT '最后登录时间',
  `last_login_IP` VARCHAR(20) NULL COMMENT '最后登录IP',
  `ipcity` VARCHAR(20) NULL COMMENT 'IP所属城市',
  PRIMARY KEY (`id`),
  INDEX `idx_account` (`account`),
  INDEX `idx_phone` (`phone`),
  INDEX `idx_email` (`email`),
  INDEX `idx_wechat` (`wechat`),
  INDEX `idx_qq` (`qq`),
  INDEX `idx_sina_blog` (`sina_blog`),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC),
  UNIQUE INDEX `account_UNIQUE` (`account` ASC),
  UNIQUE INDEX `phone_UNIQUE` (`phone` ASC),
  UNIQUE INDEX `email_UNIQUE` (`email` ASC),
  UNIQUE INDEX `wechat_UNIQUE` (`wechat` ASC),
  UNIQUE INDEX `qq_UNIQUE` (`qq` ASC),
  UNIQUE INDEX `sina_blog_UNIQUE` (`sina_blog` ASC)) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 
COMMENT = '用户表';


drop table if exists  `user_detail`;

CREATE TABLE `usercenter`.`user_detail` (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `user_id` bigint UNSIGNED NOT NULL COMMENT '用户ID',
  `type` TINYINT UNSIGNED NOT NULL COMMENT '用户类型',
  `name` VARCHAR(50) NOT NULL COMMENT '真实姓名',
  `nick_name` VARCHAR(50) NOT NULL COMMENT '用户昵称',
  `head_img` VARCHAR(200) NULL COMMENT '头像地址',
  `company` VARCHAR(200) NULL COMMENT '公司',
  `location` VARCHAR(200) NULL COMMENT '所在地',
  `certificates` TINYINT UNSIGNED NULL COMMENT '证件类型',
  `idnum` VARCHAR(50) NULL COMMENT '证件号码',
  `sex` TINYINT UNSIGNED NOT NULL COMMENT '用户性别',
  `create_time` DATETIME NULL COMMENT '创建时间',
  `update_time` DATETIME NULL COMMENT '更新时间',
  `optName` DATETIME NULL COMMENT '操作人',
  PRIMARY KEY (`id`),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC),
  UNIQUE INDEX `user_id_UNIQUE` (`user_id` ASC)) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 
COMMENT = '用户明细表';


drop table if exists  `address`;

CREATE TABLE `usercenter`.`address` (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `user_id` bigint UNSIGNED NOT NULL COMMENT '账号ID',
  `provice` VARCHAR(20) NOT NULL COMMENT '省',
  `city` VARCHAR(20) NOT NULL COMMENT '市',
  `area` VARCHAR(20) NOT NULL COMMENT '区',
  `address` VARCHAR(200) NOT NULL COMMENT '地址',
  `zipcode` VARCHAR(10) NOT NULL COMMENT '邮编',
  `receive_phone` VARCHAR(30) NOT NULL COMMENT '手机',
  `receive_name` VARCHAR(50) NOT NULL COMMENT '收货人',
  `is_default` TINYINT UNSIGNED NULL DEFAULT 0 COMMENT '默认地址',
  `create_time` DATETIME NULL COMMENT '创建时间',
  `update_time` DATETIME NULL COMMENT '更新时间',
  `optName` DATETIME NULL COMMENT '操作人',
  PRIMARY KEY (`id`),
  INDEX `address_user_id` (`user_id`)) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 
COMMENT = '收货地址表';


drop table if exists  `collection`;

CREATE TABLE `usercenter`.`collection` (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `user_id` bigint UNSIGNED NOT NULL COMMENT '账号ID',
  `type` TINYINT UNSIGNED NOT NULL COMMENT '收藏类型',
  `type_id` VARCHAR(20) NOT NULL COMMENT '宝贝id',
  `type_name` VARCHAR(20) NOT NULL COMMENT '宝贝名称',
  `type_url` VARCHAR(200) NOT NULL COMMENT '宝贝链接',
  `create_time` DATETIME NULL COMMENT '创建时间',
  `update_time` DATETIME NULL COMMENT '更新时间',
  `optName` DATETIME NULL COMMENT '操作人',
  PRIMARY KEY (`id`),
  INDEX `collection_user_id` (`user_id`)) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 
COMMENT = '用户收藏表';


drop table if exists  `user_api`;

CREATE TABLE `usercenter`.`user_api` (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `api_url` VARCHAR(100) NOT NULL COMMENT 'api地址',
  `api_name` VARCHAR(50) NOT NULL COMMENT 'api名称',
  `create_time` DATETIME NULL COMMENT '创建时间',
  `update_time` DATETIME NULL COMMENT '更新时间',
  `optName` DATETIME NULL COMMENT '操作人',
  PRIMARY KEY (`id`)) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 
COMMENT = '服务api表';


drop table if exists  `user_search_parameter`;

CREATE TABLE `usercenter`.`user_search_parameter` (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `key` VARCHAR(100) NOT NULL COMMENT '字段名',
  `value` VARCHAR(500) NOT NULL COMMENT '值',
  `html_tag` VARCHAR(50) NOT NULL COMMENT 'HTML标签',
  `type` Int NOT NULL COMMENT '1只读；2读写；3读写删',
  `create_time` DATETIME NULL COMMENT '创建时间',
  `update_time` DATETIME NULL COMMENT '更新时间',
  `optName` DATETIME NULL COMMENT '操作人',
  PRIMARY KEY (`id`)) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 
COMMENT = '检索字段表';

