drop SCHEMA if exists  `zm_user`;
CREATE SCHEMA `zm_user` ;

use zm_user;

drop table if exists  `user`;

CREATE TABLE `zm_user`.`user` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `user_type` TINYINT UNSIGNED NULL COMMENT '用户类型0:admin,1:海外购、大贸，2、区域中心；3、店铺；4、导购、5消费者',
  `account` VARCHAR(50) NULL COMMENT '账号',
  `phone` VARCHAR(15) NULL COMMENT '手机',
  `email` VARCHAR(100) NULL COMMENT '邮箱',
  `pwd` VARCHAR(50) NULL COMMENT '密码',
  `parent_id` INT UNSIGNED NULL COMMENT '上级ID',
  `band` TINYINT UNSIGNED NULL COMMENT '分销等级',
  `is_phone_validate` TINYINT UNSIGNED NULL DEFAULT 0 COMMENT '手机是否验证',
  `is_email_validate` TINYINT UNSIGNED NULL DEFAULT 0 COMMENT '邮箱是否验证',
  `status` TINYINT UNSIGNED NULL DEFAULT 1 COMMENT '用户状态0：不可用，1正常',
  `center_id` INT NULL COMMENT '注册区域中心ID',
  `shop_id` INT UNSIGNED NULL COMMENT '注册店中店ID',
  `guide_id` INT UNSIGNED NULL COMMENT '导购ID',
  `create_time` DATETIME NULL COMMENT '注册时间', 
  `update_time` DATETIME NULL COMMENT '更新时间',
  `opt` DATETIME NULL COMMENT '操作人',
  `last_login_time` DATETIME NULL COMMENT '最后登录时间',
  `last_login_IP` VARCHAR(20) NULL COMMENT '最后登录IP',
  `ipcity` VARCHAR(20) NULL COMMENT 'IP所属城市',
  PRIMARY KEY (`id`),
  INDEX `idx_account` (`account`),
  INDEX `idx_center_id` (`center_id`),
  INDEX `idx_guide_id` (`guide_id`),
  INDEX `idx_shop_id` (`shop_id`),
  INDEX `idx_phone` (`phone`),
  INDEX `idx_email` (`email`),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC),
  UNIQUE INDEX `account_UNIQUE` (`account` ASC),
  UNIQUE INDEX `phone_UNIQUE` (`phone` ASC),
  UNIQUE INDEX `email_UNIQUE` (`email` ASC)) ENGINE=InnoDB AUTO_INCREMENT=8001 DEFAULT CHARSET=utf8 
COMMENT = '用户表';

drop table if exists  `user_wechat`;

CREATE TABLE `zm_user`.`user_wechat` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `user_id` INT UNSIGNED NOT NULL COMMENT '用户ID',
  `user_type` TINYINT UNSIGNED NOT NULL COMMENT '用户类型',
  `wechat` VARCHAR(100) NOT NULL COMMENT '微信unionid',
  `attribute` VARCHAR(100) COMMENT '备用',
  `create_time` DATETIME NULL,
  `update_time` DATETIME NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE INDEX `awechat_UNIQUE` (`wechat`ASC),
  INDEX `idx_user_id` (`user_id`),
  INDEX `idx_wechat` (`wechat`)
  ) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 
COMMENT = '微信表';


drop table if exists  `user_vip`;

CREATE TABLE `zm_user`.`user_vip` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `user_id` INT UNSIGNED NOT NULL COMMENT '用户ID',
  `center_id` INT NULL COMMENT '那个区域中心的会员',
  `duration` TINYINT UNSIGNED NOT NULL COMMENT '持续时间（月）',
  `vip_level` TINYINT UNSIGNED NOT NULL COMMENT 'vip等级',
  `status` TINYINT UNSIGNED NOT NULL DEFAULT 1 COMMENT '是否有效0：否；1：是',
  `create_time` DATETIME NOT NULL COMMENT '成为vip时间',
  `update_time` DATETIME NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  INDEX `idx_user_id` (`user_id`),
  INDEX `idx_status` (`status`),
  INDEX `idx_center_id` (`center_id`)
  ) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 
COMMENT = '用户会员表';


drop table if exists  `user_vip_order`;

CREATE TABLE `zm_user`.`user_vip_order` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `order_id` char(21) NOT NULL COMMENT '订单号',
  `user_id` INT UNSIGNED NOT NULL COMMENT '用户ID',
  `vip_price_id` INT UNSIGNED NOT NULL COMMENT 'vip价格ID',
  `status` TINYINT UNSIGNED NOT NULL DEFAULT 0 COMMENT '0：初始；1：已支付',
  `pay_time` DATETIME NULL COMMENT '支付时间',
  `create_time` DATETIME NOT NULL COMMENT '',
  `update_time` DATETIME NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  INDEX `idx_user_id` (`user_id`),
  INDEX `idx_order_id` (`order_id`),
  INDEX `idx_vip_price_id` (`vip_price_id`),
  INDEX `idx_status` (`status`)
  ) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 
COMMENT = '用户会员订单表';


drop table if exists  `user_detail`;

CREATE TABLE `zm_user`.`user_detail` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `user_id` INT UNSIGNED NOT NULL COMMENT '用户ID',
  `name` VARCHAR(50) NULL COMMENT '真实姓名',
  `nick_name` VARCHAR(50) NULL COMMENT '用户昵称',
  `head_img` VARCHAR(200) NULL COMMENT '头像地址',
  `company` VARCHAR(200) NULL COMMENT '公司',
  `location` VARCHAR(200) NULL COMMENT '所在地',
  `certificates` TINYINT UNSIGNED NULL COMMENT '证件类型',
  `idnum` VARCHAR(50) NULL COMMENT '证件号码',
  `sex` TINYINT UNSIGNED NULL COMMENT '用户性别',
  `create_time` DATETIME NULL COMMENT '创建时间',
  `update_time` DATETIME NULL COMMENT '更新时间',
  `opt` VARCHAR(50) NULL COMMENT '操作人',
  PRIMARY KEY (`id`),
  UNIQUE INDEX `user_id_UNIQUE` (`user_id` ASC)) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 
COMMENT = '用户明细表';


drop table if exists  `address`;

CREATE TABLE `zm_user`.`address` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `user_id` INT UNSIGNED NOT NULL COMMENT '账号ID',
  `province` VARCHAR(20) NOT NULL COMMENT '省',
  `city` VARCHAR(20) NOT NULL COMMENT '市',
  `area` VARCHAR(20) NULL COMMENT '区',
  `address` VARCHAR(200) NOT NULL COMMENT '地址',
  `zipcode` VARCHAR(10) NULL COMMENT '邮编',
  `receive_phone` VARCHAR(30) NOT NULL COMMENT '手机',
  `receive_name` VARCHAR(50) NOT NULL COMMENT '收货人',
  `is_default` TINYINT UNSIGNED NULL DEFAULT 0 COMMENT '默认地址',
  `create_time` DATETIME NULL COMMENT '创建时间',
  `update_time` DATETIME NULL COMMENT '更新时间',
  `opt` VARCHAR(50) NULL COMMENT '操作人',
  PRIMARY KEY (`id`),
  INDEX `address_user_id` (`user_id`)) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 
COMMENT = '收货地址表';


drop table if exists  `collection`;

CREATE TABLE `zm_user`.`collection` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `user_id` INT UNSIGNED NOT NULL COMMENT '账号ID',
  `type` TINYINT UNSIGNED NOT NULL COMMENT '收藏类型',
  `type_id` VARCHAR(20) NOT NULL COMMENT '宝贝id',
  `type_name` VARCHAR(20) NOT NULL COMMENT '宝贝名称',
  `type_url` VARCHAR(200) NOT NULL COMMENT '宝贝链接',
  `create_time` DATETIME NULL COMMENT '创建时间',
  `update_time` DATETIME NULL COMMENT '更新时间',
  `opt` VARCHAR(50) NULL COMMENT '操作人',
  PRIMARY KEY (`id`),
  INDEX `collection_user_id` (`user_id`)) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 
COMMENT = '用户收藏表';

drop table if exists  `grade`;

CREATE TABLE `zm_user`.`grade` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `parent_id` INT UNSIGNED NULL COMMENT '父级ID',
  `grade_type` TINYINT UNSIGNED NOT NULL COMMENT '类型0:跨境；1大贸，100总部',
  `grade_name` VARCHAR(50) NOT NULL COMMENT '名称',
  `grade_level` TINYINT UNSIGNED NOT NULL COMMENT '等级',
  `person_in_charge` VARCHAR(50) NOT NULL COMMENT '负责人',
  `phone` CHAR(11) NOT NULL COMMENT '负责人电话',
  `attribute` VARCHAR(200) NULL COMMENT '备用字段',
  `grade_person_in_charge` INT UNSIGNED NULL COMMENT '负责该等级的负责人userID',
  `redirect_url` varchar(1000) NULL COMMENT '区域中心PC域名',  
  `create_time` DATETIME NULL COMMENT '创建时间',
  `update_time` DATETIME NULL COMMENT '更新时间',
  `opt` VARCHAR(50) NULL COMMENT '操作人',
  `company` varchar(50) DEFAULT NULL,
  `person_in_charge_id` int(11) DEFAULT NULL,
  `mobile_url` varchar(1000) NULL COMMENT '区域中心手机端域名', 
  PRIMARY KEY (`id`),
  INDEX `idx_grade_type` (`grade_type`),
  INDEX `idx_parent_id` (`parent_id`),
  INDEX `idx_grade_person_in_charge` (`grade_person_in_charge`)
  ) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 
COMMENT = '等级表';


drop table if exists  `grade_config`;

CREATE TABLE `zm_user`.`grade_config` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `grade_id` INT UNSIGNED NULL COMMENT '等级ID',
  `head_img` VARCHAR(200) NULL COMMENT '微店头像',
  `aboutus` VARCHAR(200) NULL COMMENT '微店简介',
  `name` VARCHAR(50) NULL COMMENT '微店名称',
  `attribute` VARCHAR(200) NULL COMMENT '备用字段',
  `create_time` DATETIME NULL COMMENT '创建时间',
  `update_time` DATETIME NULL COMMENT '更新时间',
  `opt` VARCHAR(50) NULL COMMENT '操作人',
  PRIMARY KEY (`id`),
  INDEX `idx_grade_id` (`grade_id`),
  UNIQUE INDEX `grade_id_UNIQUE` (`grade_id` ASC)
  ) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 
COMMENT = '微店配置表';


drop table if exists  `push_user`;

CREATE TABLE `zm_user`.`push_user` ( 
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `user_id` INT UNSIGNED NULL COMMENT '用户ID',
  `grade_id` INT UNSIGNED NULL COMMENT '哪个等级的推手',
  `status` TINYINT UNSIGNED DEFAULT 0 COMMENT '0未审核，1审核未通过，2审核通过，11清退',
  `type` TINYINT UNSIGNED DEFAULT 0 COMMENT '0普通用户，1店长',
  `phone` VARCHAR(15) NULL COMMENT '手机号',
  `name` VARCHAR(15) NULL COMMENT '姓名',
  `inviter` VARCHAR(15) NULL COMMENT '邀请人',
  `specialty_channel` VARCHAR(200) NULL COMMENT '擅长推广的渠道',
  `remark` VARCHAR(200) NULL COMMENT '备注',
  `attribute` VARCHAR(200) NULL COMMENT '备用字段',
  `create_time` DATETIME NULL COMMENT '创建时间',
  `update_time` DATETIME NULL COMMENT '更新时间',
  `opt` VARCHAR(50) NULL COMMENT '操作人',
  PRIMARY KEY (`id`),
  UNIQUE INDEX `uq_user_grade_id` (`grade_id`,`user_id` ASC),
  INDEX `idx_status` (`status`),
  INDEX `idx_grade_id` (`grade_id`),
  INDEX `idx_user_id` (`user_id`)
  ) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 
COMMENT = '推手表';

drop table if exists  `vip_price`;

CREATE TABLE `zm_user`.`vip_price` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `vip_level` TINYINT UNSIGNED NOT NULL COMMENT 'vip等级',
  `center_id` INT NULL COMMENT '区域中心',
  `duration` TINYINT UNSIGNED NOT NULL COMMENT '持续时间（月）',
  `price` DECIMAL(10,2) NOT NULL COMMENT '价格',
  `attribute` VARCHAR(200) NULL COMMENT '备用字段',
  `create_time` DATETIME NULL COMMENT '创建时间',
  `update_time` DATETIME NULL COMMENT '更新时间',
  `opt` VARCHAR(50) NULL COMMENT '操作人',
  PRIMARY KEY (`id`),
  INDEX `idx_center_id` (`center_id`)) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 
COMMENT = '会员价格表';


drop table if exists  `user_api`;

CREATE TABLE `zm_user`.`user_api` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `api_url` VARCHAR(100) NOT NULL COMMENT 'api地址',
  `api_name` VARCHAR(50) NOT NULL COMMENT 'api名称',
  `create_time` DATETIME NULL COMMENT '创建时间',
  `update_time` DATETIME NULL COMMENT '更新时间',
  `opt` VARCHAR(50) NULL COMMENT '操作人',
  PRIMARY KEY (`id`)) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 
COMMENT = '服务api表';


drop table if exists  `user_search_parameter`;

CREATE TABLE `zm_user`.`user_search_parameter` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `key` VARCHAR(100) NOT NULL COMMENT '字段名',
  `value` VARCHAR(500) NOT NULL COMMENT '值',
  `html_tag` VARCHAR(50) NOT NULL COMMENT 'HTML标签',
  `type` Int NOT NULL COMMENT '1只读；2读写；3读写删',
  `create_time` DATETIME NULL COMMENT '创建时间',
  `update_time` DATETIME NULL COMMENT '更新时间',
  `opt` VARCHAR(50) NULL COMMENT '操作人',
  PRIMARY KEY (`id`)) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 
COMMENT = '检索字段表';


INSERT INTO `zm_user`.`grade` (`id`, `grade_type`, `grade_name`, `person_in_charge`, `phone`, `grade_level`, `create_time`, `company`) VALUES ('1', '100', '鑫海总部', 'admin', '13456789012', '1', now(), '鑫海通达');
INSERT INTO `zm_user`.`user` (`user_type`, `account`, `phone`, `is_phone_validate`, `status`, `center_id`, `create_time`) VALUES ('0', 'admin', '13456789012', '1', '1', '1', now());

drop table if exists  `grade_data`;

CREATE TABLE `zm_user`.`grade_data` (
  `id` INT UNSIGNED NOT NULL COMMENT 'gradeID',
  `store_name` VARCHAR(100) NOT NULL COMMENT '门店名称',
  `contacts` VARCHAR(20) NOT NULL COMMENT '门店联系人',
  `contacts_phone` VARCHAR(20) NOT NULL COMMENT '联系人电话',
  `province` VARCHAR(20) NULL COMMENT '省',
  `city` VARCHAR(20) NULL COMMENT '市',
  `district` VARCHAR(20) NULL COMMENT '区',
  `address` VARCHAR(200) NOT NULL COMMENT '门店地址',
  `store_operator` VARCHAR(20) NOT NULL COMMENT '门店经营者',
  `operator_idnum` VARCHAR(50) NOT NULL COMMENT '经营者证件号',
  `pic1_path` VARCHAR(200) NOT NULL COMMENT '证件照片',
  `pic2_path` VARCHAR(200) NOT NULL COMMENT '营业执照',
  `pic3_path` VARCHAR(200) NOT NULL COMMENT '门店照片',
  `pic4_path` VARCHAR(200) NOT NULL COMMENT '供销货架照片',
  `create_time` DATETIME NULL COMMENT '创建时间',
  `update_time` DATETIME NULL COMMENT '更新时间',
  `opt` VARCHAR(50) NULL COMMENT '操作人',
  PRIMARY KEY (`id`)
  ) ENGINE=InnoDB DEFAULT CHARSET=utf8 
COMMENT = '等级数据表';

