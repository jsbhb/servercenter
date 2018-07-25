drop table if exists `zm_goods`.`component`;
CREATE TABLE IF NOT EXISTS `zm_goods`.`component` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NULL COMMENT '组件名称',
  `key` VARCHAR(45) NOT NULL COMMENT '组件key',
  `area` VARCHAR(45) NOT NULL COMMENT '所在区域',
  `client` VARCHAR(45) NULL COMMENT '系统',
  `data` VARCHAR(2000) NULL COMMENT '数据json串',
  `create_time` DATETIME NULL,
  `update_time` DATETIME NULL,
  `remark` VARCHAR(45) NULL,
  `opt` VARCHAR(45) NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `key_UNIQUE` (`key` ASC))
ENGINE = InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8
COMMENT = '组件表';

drop table if exists `zm_goods`.`page`;
CREATE TABLE IF NOT EXISTS `zm_goods`.`page` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NULL COMMENT '页面名称',
  `type` TINYINT(3) UNSIGNED NULL COMMENT '类型:1首页,2活动,3商详',
  `status` TINYINT(3) UNSIGNED NULL DEFAULT 0 COMMENT '状态:0编辑中,1草稿,2发布',
  `client`  VARCHAR(45) NOT NULL COMMENT '终端:pcMall,mpMall',
  `grade_id` INT UNSIGNED NOT NULL COMMENT '分级ID',
  `path` VARCHAR(300) NULL COMMENT '前端路径',
  `page` VARCHAR(45) NULL,
  `file` VARCHAR(45) NULL,
  `description` VARCHAR(200) NULL COMMENT '描述',
  `create_time` DATETIME NULL,
  `update_time` DATETIME NULL,
  `opt` VARCHAR(45) NULL,
  PRIMARY KEY (`id`),
  INDEX `gradeId` (`grade_id` ASC))
ENGINE = InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8
COMMENT = '用户页面模板表';

drop table if exists `zm_goods`.`component_page`;
CREATE TABLE IF NOT EXISTS `zm_goods`.`component_page` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `page_id` INT UNSIGNED NULL COMMENT '页面ID',
  `key` VARCHAR(45) NULL COMMENT '组件key',
  `sort` TINYINT(5) UNSIGNED NULL COMMENT '排序',
  `backup` VARCHAR(45) NULL COMMENT '备用',
  `create_time` DATETIME NULL,
  `update_time` DATETIME NULL,
  `opt` VARCHAR(45) NULL,
  PRIMARY KEY (`id`),
  INDEX `pageId` (`page_id` ASC))
ENGINE = InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8
COMMENT = '用户页面模板组件';


drop table if exists `zm_goods`.`page_seo`;
CREATE TABLE IF NOT EXISTS `zm_goods`.`page_seo` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `page_id` INT NULL,
  `keywords` TEXT NULL,
  `title` VARCHAR(2000) NULL,
  `description` VARCHAR(2000) NULL,
  `backup` VARCHAR(45) NULL,
  `create_time` DATETIME NULL,
  `update_time` DATETIME NULL,
  `opt` VARCHAR(45) NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `pageId_UNIQUE` (`page_id` ASC),
  INDEX `pageId` (`page_id` ASC))
ENGINE = InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8
COMMENT = '页面SEO配置';


drop table if exists `zm_goods`.`component_data`;
CREATE TABLE IF NOT EXISTS `zm_goods`.`component_data` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `cp_id` INT UNSIGNED NOT NULL,
  `type` tinyint(2) UNSIGNED NOT NULL comment '0:模块描述,1:模块数据',
  `picPath` VARCHAR(300) NULL,
  `title` VARCHAR(200) NULL,
  `description` VARCHAR(300) NULL,
  `origin` VARCHAR(45) NULL,
  `price` DECIMAL(12) NULL,
  `href` VARCHAR(200) NULL COMMENT '跳转',
  `specs` VARCHAR(100) NULL COMMENT '规格',
  `hot` TINYINT(2) NULL DEFAULT 0 COMMENT '热销',
  `free_post` TINYINT(2) NULL DEFAULT 0 COMMENT '包邮',
  `free_tax` TINYINT(2) NULL DEFAULT 0 COMMENT '包税',
  `popular` TINYINT(2) NULL DEFAULT 0 COMMENT '推广',
  `promotion` TINYINT(2) NULL DEFAULT 0 COMMENT '促销',
  `goods_id` VARCHAR(45) NULL,
  `sort` TINYINT(5) NULL,
  `tag_path` VARCHAR(300) NULL COMMENT '图标地址',
  `enname` VARCHAR(45) NULL COMMENT '英文名称',
  `create_time` DATETIME NULL,
  `update_time` DATETIME NULL,
  `opt` VARCHAR(45) NULL,
  PRIMARY KEY (`id`),
  INDEX `pcID` (`cp_id` ASC))
ENGINE = InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8
COMMENT = '组件数据';


drop table if exists `zm_goods`.`goods_seo`;
CREATE TABLE IF NOT EXISTS `zm_goods`.`goods_seo` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `goods_id` VARCHAR(45) NULL,
  `keywords` TEXT NULL,
  `title` VARCHAR(2000) NULL,
  `description` VARCHAR(2000) NULL,
  `detail_source_code` TEXT NULL,
  `backup` VARCHAR(45) NULL,
  `create_time` DATETIME NULL,
  `update_time` DATETIME NULL,
  `opt` VARCHAR(45) NULL,
  PRIMARY KEY (`id`),
  INDEX `goodsId` (`goods_id` ASC))
ENGINE = InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8
COMMENT = '商品SEO配置';

alter table goods add column access_path varchar(200) null comment '访问路径';
alter table goods_first_category add column tag_path varchar(200) null comment '图标路径';
alter table goods_first_category add column access_path varchar(200) null comment '访问路径';
alter table goods_second_category add column access_path varchar(200) null comment '访问路径';
alter table goods_third_category add column access_path varchar(200) null comment '访问路径';
alter table goods_first_category add column theme_name varchar(200) null comment '主题页名称';
alter table goods_second_category add column theme_name varchar(200) null comment '主题页名称';
alter table goods_third_category add column theme_name varchar(200) null comment '主题页名称';
CREATE UNIQUE INDEX uk_theme_name ON goods_first_category(theme_name);
CREATE UNIQUE INDEX uk_theme_name ON goods_second_category(theme_name);
CREATE UNIQUE INDEX uk_theme_name ON goods_third_category(theme_name);
CREATE UNIQUE INDEX access_path ON goods_first_category(access_path); 
CREATE UNIQUE INDEX access_path ON goods_second_category(access_path); 
CREATE UNIQUE INDEX access_path ON goods_third_category(access_path); 

alter table goods_2 add column is_publish tinyint default 0 comment '0:未发布，1已发布';
