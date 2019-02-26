/**
 * 商品库改造
 */
drop table if exists  `kj_goods`;
 
CREATE TABLE IF NOT EXISTS `zm_goods`.`kj_goods` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `goods_id` VARCHAR(45) NULL COMMENT '商品ID',
  `type` TINYINT(3) NOT NULL COMMENT '0:跨境,2:一般贸易',
  `goods_name` VARCHAR(300) NULL COMMENT '商品名称',
  `subtitle` VARCHAR(300) NULL COMMENT '商品副标题',
  `description` VARCHAR(450) NULL COMMENT '商品描述',
  `brand_id` VARCHAR(45) NOT NULL COMMENT '品牌ID',
  `origin` VARCHAR(20) NULL COMMENT '产地',
  `detail_path` VARCHAR(300) NULL COMMENT '商详路径',
  `access_path` VARCHAR(50) NULL COMMENT '静态路径',
  `first_category` VARCHAR(45) NOT NULL COMMENT '一级分类',
  `second_category` VARCHAR(45) NOT NULL COMMENT '二级分类',
  `third_category` VARCHAR(45) NOT NULL COMMENT '三级分类',
  `hscode` VARCHAR(45) NULL COMMENT '海关hscode',
  `display` TINYINT(3) NOT NULL DEFAULT 0 COMMENT '展示方式，0：统一展示（1个goods多个规格）；1：分开展示（1个规格一个页面）',
  `is_del` TINYINT(3) UNSIGNED NOT NULL DEFAULT 0 COMMENT '0未删除;1已删除',
  `create_time` DATETIME NULL,
  `update_time` DATETIME NULL,
  `remark` VARCHAR(45) NULL,
  `opt` VARCHAR(30) NULL COMMENT '操作人',
  PRIMARY KEY (`id`),
  UNIQUE INDEX `goods_id_uk` (`goods_id` ASC))
ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT = '商品表';

drop table if exists  `kj_specs_goods`;

CREATE TABLE IF NOT EXISTS `zm_goods`.`kj_specs_goods` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `specs_id` VARCHAR(45) NOT NULL,
  `encode` VARCHAR(45) NOT NULL COMMENT '条形码',
  `weight` INT NOT NULL DEFAULT 0 COMMENT '重量/克',
  `description` VARCHAR(100) NULL COMMENT '描述',
  `unit` VARCHAR(5) NULL COMMENT '单位',
  `info` VARCHAR(1000) NULL COMMENT '规格',
  `carton` VARCHAR(45) NULL COMMENT '箱规',
  `conversion` INT UNSIGNED NOT NULL DEFAULT 1 COMMENT '换算比例',
  `specs_goods_name` VARCHAR(100) NULL COMMENT '每个规格的商品名称',
  `create_time` DATETIME NULL,
  `update_time` DATETIME NULL,
  `opt` VARCHAR(45) NULL,
  `is_del` TINYINT(3) NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `specs_id_uk` (`specs_id` ASC))
ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT = '商品规格表';

drop table if exists  `kj_goods_specs_tradepattern`;

CREATE TABLE IF NOT EXISTS `zm_goods`.`kj_goods_specs_tradepattern` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `specs_tp_id` VARCHAR(45) NOT NULL,
  `goods_id` VARCHAR(45) NOT NULL,
  `specs_id` VARCHAR(45) NOT NULL,
  `status` TINYINT(3) NOT NULL DEFAULT 0 COMMENT '0:初始,1：下架，2：上架，3：售罄',
  `is_free_post` TINYINT(3) NOT NULL DEFAULT 0 COMMENT '是否包邮;0:否,1:是',
  `is_free_tax` TINYINT(3) NOT NULL DEFAULT 0 COMMENT '是否包税,0:否,1是',
  `tag_ratio` TINYINT(3) UNSIGNED NOT NULL DEFAULT 0 COMMENT '权重',
  `increment_tax` DECIMAL(10,2) NOT NULL DEFAULT 0 COMMENT '增值税',
  `tariff` DECIMAL(10,2) NOT NULL DEFAULT 0 COMMENT '关税',
  `excise_tax` DECIMAL(10,2) NOT NULL DEFAULT 0 COMMENT '消费税',
  `is_promotion` TINYINT(3) UNSIGNED NOT NULL DEFAULT 0 COMMENT '是否促销0:否;1是',
  `discount` DECIMAL(5) NOT NULL DEFAULT 0 COMMENT '促销折扣',
  `is_distribution` TINYINT(3) UNSIGNED NOT NULL DEFAULT 0 COMMENT '是否分销0:否;1:是',
  `is_welfare` TINYINT(3) UNSIGNED NOT NULL DEFAULT 0 COMMENT '是否在福利商城显示0:否;1:是',
  `is_combinedgoods` TINYINT(3) UNSIGNED NOT NULL DEFAULT 0 COMMENT '是否组合商品0:否;1:是',
  `combined_specs_tp_id` VARCHAR(100) DEFAULT NULL COMMENT '组合商品包含的specs_tp_id',
  `display` TINYINT(3) UNSIGNED NOT NULL DEFAULT 0 COMMENT '显示(针对上架商品):0:不显示;1:前端显示;2:后台显示;3:前后台都显示',
  `distribution_price` decimal(10,2) DEFAULT '0.00' COMMENT '分销价格',
  `vip_price` decimal(10,2) DEFAULT '0.00' COMMENT '会员价格',
  `retail_price` decimal(10,2) DEFAULT '0.00' COMMENT '零售价格',
  `line_price` decimal(10,2) DEFAULT '0.00',
  `department_profit` int(11) NOT NULL DEFAULT '0' COMMENT '部门利润',
  `instant_ratio` int(11) NOT NULL DEFAULT '0' COMMENT '顺加比例',
  `sale_num` INT NOT NULL DEFAULT 0 COMMENT '销量',
  `item_id` VARCHAR(45) DEFAULT NULL COMMENT '跨境商品上架需绑定对应ItemId',
  `is_fresh` TINYINT(3) UNSIGNED NOT NULL DEFAULT 0 COMMENT '是否是新品:0否，1：是',
  `upshelf_time` DATETIME NULL COMMENT '上架时间',
  `downshelf_time` DATETIME NULL COMMENT '下架时间',
  `create_time` DATETIME NULL,
  `update_time` DATETIME NULL,
  `opt` VARCHAR(30) NULL,
  `remark` VARCHAR(45) NULL,
  `is_del` TINYINT(3) NOT NULL DEFAULT 0 COMMENT '是否删除0否;1是',
  PRIMARY KEY (`id`),
  UNIQUE INDEX `specs_tp_id_uk` (`specs_tp_id` ASC),
  UNIQUE INDEX `specsId_goodsId_uk` (`goods_id`,`specs_id` ASC),
  INDEX `specs_id_idx` (`specs_id` ASC),
  INDEX `status_idx` (`status` ASC),
  INDEX `del_idx` (`is_del` ASC))
ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT = '规格表和商品表绑定后生成的唯一规格商品';

drop table if exists  `kj_goods_item`;

CREATE TABLE IF NOT EXISTS `zm_goods`.`kj_goods_item` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `specs_tp_id` VARCHAR(45) NOT NULL,
  `item_id` VARCHAR(45) NOT NULL,
  `supplier_id` INT NOT NULL,
  `supplier_name` VARCHAR(45) NOT NULL,
  `status` TINYINT(3) NOT NULL DEFAULT 0 COMMENT '0:仓库中,1：待审核，2：在售中，11：审核失败',
  `reason` VARCHAR(100) DEFAULT NULL COMMENT '审核失败原因',
  `unit` VARCHAR(5) NULL COMMENT '海关备案单位',
  `sku` VARCHAR(45) NULL COMMENT '海关备案号',
  `item_code` VARCHAR(45) COMMENT '商家编码',
  `shelf_life` VARCHAR(30) NULL COMMENT '效期',
  `create_time` DATETIME NULL,
  `update_time` DATETIME NULL,
  `opt` VARCHAR(30) NULL,
  `remark` VARCHAR(45) NULL,
  `is_del` TINYINT(3) NOT NULL DEFAULT 0 COMMENT '是否删除0否;1是',
  PRIMARY KEY (`id`),
  UNIQUE INDEX `item_id_uk` (`item_id` ASC),
  INDEX `specs_tp_id_idx` (`specs_tp_id` ASC),
  INDEX `status_idx` (`status` ASC),
  INDEX `del_idx` (`is_del` ASC))
ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT = 'kj_goods_specs_tradepattern增加供应商等其他信息后的item表';

drop table if exists  `kj_property_name`;

CREATE TABLE IF NOT EXISTS `zm_goods`.`kj_property_name` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `type` TINYINT(3) NOT NULL COMMENT '属性类型:0:基本属性;1:系列属性(规格)',
  `name` VARCHAR(45) NULL COMMENT '属性名称',
  `create_time` DATETIME NULL,
  `update_time` DATETIME NULL,
  `opt` VARCHAR(30) NULL,
  PRIMARY KEY (`id`))
ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT = '属性名';

drop table if exists  `kj_property_value`;

CREATE TABLE IF NOT EXISTS `zm_goods`.`kj_property_value` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name_id` VARCHAR(45) NOT NULL COMMENT '属性名ID',
  `val` VARCHAR(45) NULL COMMENT '属性值',
  `pic_path` VARCHAR(200) NULL COMMENT '属性图片',
  `sort` tinyint(5) DEFAULT 0 COMMENT '顺序',
  `count_num` int(11) DEFAULT 0 COMMENT '关联商品数统计',
  `create_time` DATETIME NULL,
  `update_time` DATETIME NULL,
  `opt` VARCHAR(30) NULL,
  PRIMARY KEY (`id`))
ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT = '属性值';

drop table if exists  `kj_guide_property_name`;

CREATE TABLE IF NOT EXISTS `zm_goods`.`kj_guide_property_name` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NULL COMMENT '属性名称',
  `create_time` DATETIME NULL,
  `update_time` DATETIME NULL,
  `opt` VARCHAR(30) NULL,
  PRIMARY KEY (`id`))
ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT = '导购属性名';

drop table if exists  `kj_guide_property_value`;

CREATE TABLE IF NOT EXISTS `zm_goods`.`kj_guide_property_value` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `guide_name_id` VARCHAR(45) NOT NULL COMMENT '属性名ID',
  `val` VARCHAR(45) NULL COMMENT '属性值',
  `pic_path` VARCHAR(200) NULL COMMENT '属性图片',
  `sort` tinyint(5) DEFAULT 0 COMMENT '顺序',
  `count_num` int(11) DEFAULT 0 COMMENT '关联商品数统计',
  `create_time` DATETIME NULL,
  `update_time` DATETIME NULL,
  `opt` VARCHAR(30) NULL,
  PRIMARY KEY (`id`))
ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT = '导购属性值';

drop table if exists  `kj_category_property_bind`;

CREATE TABLE IF NOT EXISTS `zm_goods`.`kj_category_property_bind` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `property_id` INT NOT NULL COMMENT '属性名ID',
  `sort` tinyint(5) DEFAULT 0 COMMENT '顺序',
  `category_id` VARCHAR(45) NOT NULL COMMENT '分类ID',
  `category_type` TINYINT(3) NOT NULL COMMENT '分类类型:1:一级分类;2:二级分类;3:三级分类;',
  `create_time` DATETIME NULL,
  `update_time` DATETIME NULL,
  `opt` VARCHAR(30) NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `categoryId_categoryType_propertyId_uk` (`category_id`,`category_type`,`property_id`))
ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT = '类目属性绑定表';

drop table if exists  `kj_category_guide_property_bind`;

CREATE TABLE IF NOT EXISTS `zm_goods`.`kj_category_guide_property_bind` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `property_id` INT NOT NULL COMMENT '属性名ID',
  `sort` tinyint(5) DEFAULT 0 COMMENT '顺序',
  `category_id` VARCHAR(45) NOT NULL COMMENT '分类ID',
  `category_type` TINYINT(3) NOT NULL COMMENT '分类类型:1:一级分类;2:二级分类;3:三级分类;',
  `create_time` DATETIME NULL,
  `update_time` DATETIME NULL,
  `opt` VARCHAR(30) NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `categoryId_categoryType_propertyId_uk` (`category_id`,`category_type`,`property_id`))
ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT = '类目导购绑定表';

drop table if exists  `kj_goods_property_bind`;

CREATE TABLE IF NOT EXISTS `zm_goods`.`kj_goods_property_bind` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `property_id` INT NOT NULL COMMENT '属性名ID',
  `property_val_id` INT NOT NULL COMMENT '属性值ID',
  `specs_tp_id` VARCHAR(45) NOT NULL COMMENT 'specs_tp_id',
  `create_time` DATETIME NULL,
  `update_time` DATETIME NULL,
  `opt` VARCHAR(30) NULL,
  PRIMARY KEY (`id`))
ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT = '商品属性绑定表';

drop table if exists  `kj_goods_guide_property_bind`;

CREATE TABLE IF NOT EXISTS `zm_goods`.`kj_goods_guide_property_bind` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `property_id` INT NOT NULL COMMENT '导购属性名ID',
  `property_val_id` INT NOT NULL COMMENT '导购属性值ID',
  `specs_tp_id` VARCHAR(45) NOT NULL COMMENT 'specs_tp_id',
  `create_time` DATETIME NULL,
  `update_time` DATETIME NULL,
  `opt` VARCHAR(30) NULL,
  PRIMARY KEY (`id`))
ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT = '商品导购绑定表';

drop table if exists  `kj_goods_price`;

CREATE TABLE IF NOT EXISTS `zm_goods`.`kj_goods_price` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `item_id` varchar(50) NOT NULL COMMENT 'itemID',
  `min` int(10) unsigned DEFAULT '0' COMMENT '最小数量',
  `max` int(10) unsigned DEFAULT NULL COMMENT '最大数量',
  `cost_price` decimal(10,2) DEFAULT '0.00' COMMENT '成本价格',
  `internal_price` decimal(10,2) DEFAULT NULL COMMENT '内供价',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `opt` varchar(20) DEFAULT NULL COMMENT '操作人',
  PRIMARY KEY (`id`),
  KEY `item_id_idx` (`item_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='商品价格表';

drop table if exists  `kj_goods_tag_bind`;

CREATE TABLE IF NOT EXISTS `zm_goods`.`kj_goods_tag_bind` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `specs_tp_id` varchar(100) NOT NULL COMMENT '商品编号',
  `tag_id` int(10) unsigned NOT NULL COMMENT '标签编号',
  `create_time` datetime DEFAULT NULL COMMENT '注册时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `opt` varchar(20) DEFAULT NULL COMMENT '操作人',
  PRIMARY KEY (`id`),
  UNIQUE KEY `specsTpId_tagId_uk` (`specs_tp_id`,`tag_id`),
  KEY `tag_id_idx` (`tag_id`),
  KEY `specs_tp_id_idx` (`specs_tp_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='商品标签绑定表';

drop table if exists  `kj_goods_rebate`;

CREATE TABLE IF NOT EXISTS `zm_goods`.`kj_goods_rebate` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `specs_tp_id` varchar(50) NOT NULL COMMENT '商品ID',
  `grade_type` int(10) unsigned NOT NULL COMMENT '客户类型',
  `proportion` decimal(12,6) DEFAULT NULL,
  `remark` varchar(200) DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT NULL COMMENT '注册时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `opt` varchar(20) DEFAULT NULL COMMENT '操作人',
  PRIMARY KEY (`id`),
  UNIQUE KEY `specsTpId_gradeType_uk` (`specs_tp_id`,`grade_type`),
  KEY `specs_tp_id_idx` (`specs_tp_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='商品返佣表';

/**
 *品牌表
 */ 
alter table goods_category_brand add column brand_logo varchar(200) default null comment '品牌logo';
alter table goods_category_brand add column brand_synopsis varchar(500) default null comment '品牌简介';
alter table goods_category_brand add column brand_name_cn varchar(20) default null comment '品牌中文名';
alter table goods_category_brand add column brand_name_en varchar(20) default null comment '品牌英文名';
alter table goods_category_brand add column country varchar(20) default null comment '国家';

/**
 *一级分类表
 */ 
alter table goods_first_category add column second_page_path varchar(300) default null comment '二级页面路径';

alter table activity_goods_role change item_id specs_tp_id varchar(45);
alter table big_sale_record change item_id specs_tp_id varchar(45);

