drop SCHEMA if exists  `zm_goods`;
CREATE SCHEMA `zm_goods` ;

use zm_goods;

drop table if exists  `base`;

CREATE TABLE `zm_goods`.`base` (
  `id` int UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `brand_id` varchar(100)  NOT NULL COMMENT '品牌ID',
  `goods_name` VARCHAR(100) NOT NULL COMMENT '商品名称',
  `brand` VARCHAR(100) NOT NULL COMMENT '商品品牌',
  `increment_tax` DECIMAL(5,2) NULL COMMENT '增值税',
  `tariff` DECIMAL(5,2) NULL COMMENT '关税',
  `unit` VARCHAR(30) NOT NULL COMMENT '单位',
  `hscode` VARCHAR(50) NULL COMMENT 'HSCODE',
  `encode` VARCHAR(50) NULL COMMENT '条形码',
  `first_category` varchar(100) NOT NULL COMMENT '一级分类id',
  `second_category` varchar(100) NOT NULL COMMENT '二级分类id',
  `third_category` varchar(100) NOT NULL COMMENT '三级分类id',
  `center_id` int NULL COMMENT '区域中心ID',
  `create_time` DATETIME NULL COMMENT '注册时间',
  `update_time` DATETIME NULL COMMENT '更新时间',
  `opt` VARCHAR(20) NULL COMMENT '操作人',
  PRIMARY KEY (`id`),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC),
  INDEX `idx_brand` (`brand`),
  INDEX `idx_first_category` (`first_category`),
  INDEX `idx_second_category` (`second_category`),
  INDEX `idx_third_category` (`third_category`),
  INDEX `idx_brand_id` (`brand_id`)) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 
COMMENT = '商品基本信息管理表';


drop table if exists  `goods`;

CREATE TABLE `zm_goods`.`goods` (
  `id` int UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `goods_id` VARCHAR(50) NOT NULL COMMENT '商品ID',
  `supplier_id` int UNSIGNED NULL COMMENT '商家ID',
  `supplier_name` VARCHAR(100) NULL COMMENT '商家名称',
  `base_id` int UNSIGNED NOT NULL COMMENT '商品基本信息ID',
  `specs_template_id` int UNSIGNED NOT NULL COMMENT '规格模板ID',
  `goods_name` VARCHAR(100) NOT NULL COMMENT '商品名称',
  `description` VARCHAR(450) NULL COMMENT '描述',
  `origin` VARCHAR(100) NULL COMMENT '原产国',
  `status` tinyint UNSIGNED NOT NULL DEFAULT 0 COMMENT '商品状态0：初始，1：可用；2：可分销',
  `type` tinyint UNSIGNED NOT NULL COMMENT '商品分类0：跨境；1：大贸;2：一般贸易',
  `is_popular` tinyint UNSIGNED NOT NULL DEFAULT 0 COMMENT '是否特推0：否，1是',
  `is_new` tinyint UNSIGNED NOT NULL DEFAULT 0 COMMENT '是否新品0：否，1是',
  `is_good` tinyint UNSIGNED NOT NULL DEFAULT 0 COMMENT '是否精选0：否，1是',
  `is_choice` tinyint UNSIGNED NOT NULL DEFAULT 0 COMMENT '是否渠道商品0：否，1是',
  `is_hot` tinyint UNSIGNED NOT NULL DEFAULT 0 COMMENT '是否..0：否，1是',
  `is_free_postfee` tinyint UNSIGNED NOT NULL DEFAULT 0 COMMENT '是否包邮0：否，1是',
  `detail_path` VARCHAR(2000) NULL COMMENT '详情地址',
  `index_status` tinyint UNSIGNED NOT NULL DEFAULT 0 COMMENT '是否创建lucene0：否，1是',
  `create_time` DATETIME NULL COMMENT '创建时间',
  `update_time` DATETIME NULL COMMENT '更新时间',
  `opt` VARCHAR(20) NULL COMMENT '操作人',
  PRIMARY KEY (`id`),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC),
  INDEX `idx_supplier_id` (`supplier_id`),
  INDEX `idx_base_id` (`base_id`),
  INDEX `idx_status` (`status`),
  INDEX `idx_index_status` (`index_status`),
  INDEX `idx_type` (`type`),
  INDEX `idx_is_popular` (`is_popular`),
  INDEX `idx_is_new` (`is_new`),
  INDEX `idx_is_hot` (`is_hot`),
  INDEX `idx_is_good` (`is_good`),
  INDEX `idx_is_choice` (`is_choice`)) ENGINE=InnoDB AUTO_INCREMENT=10000 DEFAULT CHARSET=utf8 
COMMENT = '供应商商品基本信息表';


drop table if exists  `goods_file`;

CREATE TABLE `zm_goods`.`goods_file` (
  `id` int UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `goods_id` VARCHAR(50) NOT NULL COMMENT '商品ID',
  `path` VARCHAR(100) NULL COMMENT '文件路径',
  `suffix` CHAR(5) NULL COMMENT '后缀',
  `store_type` tinyint UNSIGNED NOT NULL COMMENT '存储类型',
  `type` tinyint UNSIGNED NOT NULL COMMENT '存储类型 0:图片',
  `create_time` DATETIME NULL COMMENT '创建时间',
  `update_time` DATETIME NULL COMMENT '更新时间',
  `opt` VARCHAR(20) NULL COMMENT '操作人',
  PRIMARY KEY (`id`),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC),
  UNIQUE INDEX `goodsIdpath_UNIQUE` (`goods_id`,`path` ASC),
  INDEX `idx_goods_id` (`goods_id`),
  INDEX `idx_suffix` (`suffix`),
  INDEX `idx_store_type` (store_type),
  INDEX `idx_type` (type)
  ) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 
COMMENT = '供应商商品文件表';


drop table if exists  `goods_first_category`;

CREATE TABLE `zm_goods`.`goods_first_category` (
  `id` int UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `first_id` varchar(100) NOT NULL COMMENT '一级分类ID',
  `name` VARCHAR(100) NULL COMMENT '一级分类名称',
  `create_time` DATETIME NULL COMMENT '创建时间',
  `update_time` DATETIME NULL COMMENT '更新时间',
  `opt` VARCHAR(20) NULL COMMENT '操作人',
  PRIMARY KEY (`id`),
  UNIQUE INDEX `first_id_UNIQUE` (`first_id` ASC),
  INDEX `idx_first_id` (first_id)) ENGINE=InnoDB AUTO_INCREMENT=1
  DEFAULT CHARSET=utf8 
COMMENT = '供应商商品一级分类表';


drop table if exists  `goods_second_category`;

CREATE TABLE `zm_goods`.`goods_second_category` (
  `id` int UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `first_id` varchar(100) NOT NULL COMMENT 'first_id',
  `second_id` varchar(100) NOT NULL COMMENT 'first_id',
  `name` VARCHAR(100) NULL COMMENT '二级分类名称',
  `create_time` DATETIME NULL COMMENT '创建时间',
  `update_time` DATETIME NULL COMMENT '更新时间',
  `opt` VARCHAR(20) NULL COMMENT '操作人',
  PRIMARY KEY (`id`),
  INDEX `idx_second_id` (`second_id`),
  UNIQUE INDEX `second_id_UNIQUE` (`second_id` ASC),
   INDEX `goods_second_categroy_first_id` (`first_id`)) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 
COMMENT = '供应商商品二级分类表';


drop table if exists  `goods_third_category`;

CREATE TABLE `zm_goods`.`goods_third_category` (
  `id` int UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `second_id` varchar(100) NOT NULL COMMENT 'second_id',
  `third_id` varchar(100) NOT NULL COMMENT 'second_id',
  `name` VARCHAR(100) NULL COMMENT '三级分类名称',
  `create_time` DATETIME NULL COMMENT '创建时间',
  `update_time` DATETIME NULL COMMENT '更新时间',
  `opt` VARCHAR(20) NULL COMMENT '操作人',
  PRIMARY KEY (`id`),
  INDEX `idx_third_id` (`third_id`),
  UNIQUE INDEX `third_id_UNIQUE` (`third_id` ASC),
  INDEX `goods_third_category_second_id` (`second_id`)) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 
COMMENT = '供应商商品三级分类表';


drop table if exists  `goods_category_brand`;

CREATE TABLE `zm_goods`.`goods_category_brand` (
  `id` int UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `brand_id` varchar(100)  NOT NULL COMMENT '品牌ID',
  `brand` VARCHAR(100) NULL COMMENT '品牌名称',
  `attr` VARCHAR(100) NULL COMMENT '备用',
  `create_time` DATETIME NULL COMMENT '创建时间',
  `update_time` DATETIME NULL COMMENT '更新时间',
  `opt` VARCHAR(20) NULL COMMENT '操作人',
  PRIMARY KEY (`id`),
  UNIQUE INDEX `brand_id_UNIQUE` (`brand_id` ASC),
  INDEX `idx_brand_id` (`brand_id`),
  INDEX `goods_category_brand_brand` (`brand`)
  ) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 
COMMENT = '商品品牌表';


drop table if exists  `goods_item`;

CREATE TABLE `zm_goods`.`goods_item` (
  `id` int UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `goods_id` VARCHAR(50) NOT NULL COMMENT '商品ID',
  `item_id` VARCHAR(50) NOT NULL COMMENT 'itemID',
  `item_code` varchar(50) NOT NULL COMMENT '商家自有编码',
  `sku` VARCHAR(100) NULL COMMENT 'sku信息',
  `weight` int UNSIGNED NOT NULL COMMENT '商品重量（克）',
  `excise_tax` DECIMAL(5,2) NULL COMMENT '消费税',
  `is_promotion` tinyint UNSIGNED NOT NULL DEFAULT 0 COMMENT '是否促销0：否；1：是',
  `status` tinyint UNSIGNED NOT NULL DEFAULT 0 COMMENT '商品状态0：停售，1：在售',
  `discount` decimal(10,2) NULL COMMENT '促销折扣',
  `info` VARCHAR(1000) NULL COMMENT '规格信息',
  `create_time` DATETIME NULL COMMENT '创建时间',
  `update_time` DATETIME NULL COMMENT '更新时间',
  `opt` VARCHAR(20) NULL COMMENT '操作人',
  PRIMARY KEY (`id`),
  INDEX `idx_goods_id` (`goods_id`),
  INDEX `idx_is_promotion` (`is_promotion`),
  UNIQUE INDEX `item_id_UNIQUE` (`item_id` ASC),
  INDEX `idx_sku` (`sku`)
  ) ENGINE=InnoDB AUTO_INCREMENT=200000 DEFAULT CHARSET=utf8 
COMMENT = '商品item表';


drop table if exists  `goods_price_contrast`;

CREATE TABLE `zm_goods`.`goods_price_contrast` (
  `id` int UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `item_id` VARCHAR(50) NOT NULL COMMENT 'itemID',
  `sh_price` decimal(10,2) NOT NULL COMMENT '上海价格',
  `bj_price` decimal(10,2) NOT NULL COMMENT '北京价格',
  `price` decimal(10,2) NOT NULL COMMENT '自己价格',
  `reserve` decimal(10,2) NULL COMMENT '备用',
  `contrast_time` varchar(15) NOT NULL COMMENT '比较时间',
  `create_time` DATETIME NULL COMMENT '创建时间',
  `update_time` DATETIME NULL COMMENT '更新时间',
  `opt` VARCHAR(20) NULL COMMENT '操作人',
  PRIMARY KEY (`id`),
  INDEX `idx_item_id` (`item_id`),
  UNIQUE INDEX `uq_contrast_time` (`contrast_time`,`item_id` ASC)
  ) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 
COMMENT = '商品价格对比表';


drop table if exists  `goods_price`;

CREATE TABLE `zm_goods`.`goods_price` (
  `id` int UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `item_id` VARCHAR(50) NOT NULL COMMENT 'itemID',
  `min` int UNSIGNED NULL DEFAULT 0 COMMENT '最小数量',
  `max` int UNSIGNED NULL COMMENT '最大数量',
  `proxy_price` decimal(10,2) NULL DEFAULT 0.0 COMMENT '代理价格',
  `fxprice` decimal(10,2) NULL DEFAULT 0.0 COMMENT '分销价格',
  `vip_price` decimal(10,2) DEFAULT 0.0 NULL COMMENT '会员价格',
  `retail_price` decimal(10,2) DEFAULT 0.0 NULL COMMENT '零售价格',
  `delivery_place` varchar(20) NULL COMMENT '发货地',
  `create_time` DATETIME NULL COMMENT '创建时间',
  `update_time` DATETIME NULL COMMENT '更新时间',
  `opt` VARCHAR(20) NULL COMMENT '操作人',
  PRIMARY KEY (`id`),
  INDEX `idx_item_id` (`item_id`)) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 
COMMENT = '商品价格表';


drop table if exists  `goods_stock`;

CREATE TABLE `zm_goods`.`goods_stock` (
  `id` int UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `item_id` VARCHAR(50) NOT NULL COMMENT 'itemID',
  `qpqty` int  NULL COMMENT '良品数量', 
  `fxqty` int  NULL COMMENT '分销数量', 
  `defqty` int  NULL COMMENT '次品数量',
  `frozenqty` int  NULL COMMENT '冻结数量',
  `lockedqty` int  NULL COMMENT '锁定数量',
  `create_time` DATETIME NULL COMMENT '创建时间',
  `update_time` DATETIME NULL COMMENT '更新时间',
  `opt` VARCHAR(20) NULL COMMENT '操作人',
  PRIMARY KEY (`id`),
  UNIQUE INDEX `uq_item_id` (`item_id` ASC),
  INDEX `idx_item_id` (`item_id`)) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 
COMMENT = '商品库存表';


drop table if exists  `layout`;

CREATE TABLE `zm_goods`.`layout` (
  `id` int UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `page` varchar(50) NOT NULL COMMENT '页面名称',
  `code` varchar(50) NOT NULL COMMENT '模块ID',
  `type` tinyint UNSIGNED NOT NULL COMMENT '类型：0：普通模块；1：活动模块；',
  `page_type` tinyint UNSIGNED NOT NULL COMMENT '类型：0：PC；1：手机；',
  `is_show` tinyint UNSIGNED NULL DEFAULT 0 COMMENT '是否显示 0：否；1：是', 
  `config` varchar(100) NULL COMMENT '模块配置',
  `description` VARCHAR(450) NULL COMMENT '描述',
  `create_time` DATETIME NULL COMMENT '创建时间',
  `update_time` DATETIME NULL COMMENT '更新时间',
  `opt` VARCHAR(20) NULL COMMENT '操作人',
  PRIMARY KEY (`id`),
  INDEX `idx_code` (`code`),
  INDEX `idx_page_type` (`page_type`),
  INDEX `idx_page` (`page`)) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 
COMMENT = '布局表';


drop table if exists  `popularize_dict`;

CREATE TABLE `zm_goods`.`popularize_dict` (
  `id` int UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `layout_id` int UNSIGNED NOT NULL COMMENT '布局ID',
  `name` VARCHAR(50) NULL COMMENT '名称',
  `href` VARCHAR(300) NULL COMMENT '跳转地址',
  `type` tinyint UNSIGNED NOT NULL COMMENT '类型：0：新品；1：特推；2：渠道；3精选；4：普通分类',
  `enname` VARCHAR(50) NULL COMMENT '英文名称',
  `first_category` varchar(100) NULL COMMENT '一级分类id',
  `picPath1` VARCHAR(300) NULL COMMENT '图片地址',
  `picPath2` VARCHAR(300) NULL COMMENT '图片地址',
  `picPath3` VARCHAR(300) NULL COMMENT '图片地址',
  `description` VARCHAR(450) NULL COMMENT '描述',
  `create_time` DATETIME NULL COMMENT '创建时间',
  `update_time` DATETIME NULL COMMENT '更新时间',
  `opt` VARCHAR(20) NULL COMMENT '操作人',
  PRIMARY KEY (`id`),
  UNIQUE INDEX `uq_layout_id` (`layout_id`)
  ) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 
COMMENT = '推广字典';


drop table if exists  `popularize_data`;
CREATE TABLE `zm_goods`.`popularize_data` (
  `id` int UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `dictId` int UNSIGNED NOT NULL COMMENT '字典ID',
  `href` VARCHAR(300) NULL COMMENT '跳转地址',
  `picPath` VARCHAR(300) NULL COMMENT '图片地址',
  `title` VARCHAR(50) NULL COMMENT '名称',
  `specs` VARCHAR(50) NULL COMMENT '规格',
  `origin` VARCHAR(100) NULL COMMENT '原产国',
  `description` VARCHAR(450) NULL COMMENT '描述',
  `price` DECIMAL(10,2) NULL COMMENT '商品价格',
  `goods_id` VARCHAR(50) NOT NULL COMMENT '商品ID',
  `create_time` DATETIME NULL COMMENT '创建时间',
  `update_time` DATETIME NULL COMMENT '更新时间',
  `opt` VARCHAR(20) NULL COMMENT '操作人',
  PRIMARY KEY (`id`),
  INDEX `idx_dictId` (`dictId`)) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 
COMMENT = '推广数据';


drop table if exists  `activity`;
CREATE TABLE `zm_goods`.`activity` (
  `id` int UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `layout_id` int UNSIGNED NOT NULL COMMENT '布局ID',
  `name` VARCHAR(50) NULL COMMENT '活动名称',
  `href` VARCHAR(300) NULL COMMENT '跳转地址',
  `type` tinyint UNSIGNED NOT NULL COMMENT '类型：0：限时促销；1：满减；2：满打折',
  `type_status` tinyint UNSIGNED NULL DEFAULT 0 COMMENT '类型：0：特定区域；1：全场；',
  `condition_price` DECIMAL(10,2) NULL COMMENT '满多少条件',
  `discount` decimal(10,2) NULL COMMENT '促销折扣',
  `status` tinyint UNSIGNED NULL DEFAULT 0 COMMENT '状态0:未开始；1：正在进行；2：结束',
  `picPath` VARCHAR(200) NULL COMMENT '图片地址',
  `attr` VARCHAR(50) NULL COMMENT '备用字段',
  `description` VARCHAR(450) NULL COMMENT '描述',
  `start_time` DATETIME NULL COMMENT '开始时间',
  `end_time` DATETIME NULL COMMENT '结束时间',
  `opt` VARCHAR(20) NULL COMMENT '操作人',
  PRIMARY KEY (`id`),
  UNIQUE INDEX `uq_layout_id` (`layout_id`),
  INDEX `idx_type_status` (`type_status`),
  INDEX `idx_status` (`status`),
  INDEX `idx_type` (`type`)) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 
COMMENT = '活动列表';

drop table if exists  `activity_data`;
CREATE TABLE `zm_goods`.`activity_data` (
  `id` int UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `activity_id` int UNSIGNED NOT NULL COMMENT '活动ID',
  `href` VARCHAR(300) NULL COMMENT '跳转地址',
  `picPath` VARCHAR(300) NULL COMMENT '图片地址',
  `title` VARCHAR(50) NULL COMMENT '名称',
  `goods_id` VARCHAR(50) NOT NULL COMMENT '商品ID',
  `goods_name` VARCHAR(100) NULL COMMENT '商品名称',
  `attr` VARCHAR(50) NULL COMMENT '备用字段',
  `create_time` DATETIME NULL COMMENT '创建时间',
  `update_time` DATETIME NULL COMMENT '更新时间',
  `opt` VARCHAR(20) NULL COMMENT '操作人',
  PRIMARY KEY (`id`),
  INDEX `idx_goods_id` (`goods_id`),
  INDEX `idx_activity_id` (`activity_id`)) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 
COMMENT = '活动数据';


drop table if exists  `third_goods`;
CREATE TABLE `zm_goods`.`third_goods` (
  `id` int UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `supplier_id` int UNSIGNED NOT NULL COMMENT '供应商ID',
  `supplier_name` VARCHAR(300) NULL COMMENT '供应商名称',
  `sku` VARCHAR(100) NULL COMMENT 'sku',
  `item_code` VARCHAR(100) NULL COMMENT 'itemcode',
  `goods_name` VARCHAR(100) NULL COMMENT '商品名称',
  `brand` VARCHAR(100) NULL COMMENT '品牌',
  `weight` int UNSIGNED NOT NULL COMMENT '商品重量（克）',
  `origin` VARCHAR(100) NULL COMMENT '原产国',
  `stock` int  NULL COMMENT '库存数量', 
  `min` int UNSIGNED NULL DEFAULT 0 COMMENT '最小数量',
  `max` int UNSIGNED NULL COMMENT '最大数量',
  `status` tinyint UNSIGNED NULL DEFAULT 0 COMMENT '类型：0：未处理；1：已处理；',
  `create_time` DATETIME NULL COMMENT '创建时间',
  `update_time` DATETIME NULL COMMENT '更新时间',
  `opt` VARCHAR(20) NULL COMMENT '操作人',
  PRIMARY KEY (`id`),
  UNIQUE INDEX `uq_sku` (`supplier_id`,`sku`),
  UNIQUE INDEX `uq_item_code` (`supplier_id`,`item_code`),
  INDEX `idx_supplier_id` (`supplier_id`),
  INDEX `idx_status` (`status`)) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 
COMMENT = '第三方商品';


drop table if exists  `goods_api`;

CREATE TABLE `zm_goods`.`goods_api` (
  `id` int UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `api_url` VARCHAR(100) NOT NULL COMMENT 'api地址',
  `api_name` VARCHAR(50) NOT NULL COMMENT 'api名称',
  `create_time` DATETIME NULL COMMENT '创建时间',
  `update_time` DATETIME NULL COMMENT '更新时间',
  `opt` VARCHAR(20) NULL COMMENT '操作人',
  PRIMARY KEY (`id`)) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 
COMMENT = '服务api表';


drop table if exists  `goods_search_parameter`;

CREATE TABLE `zm_goods`.`goods_search_parameter` (
  `id` int UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `key` VARCHAR(100) NOT NULL COMMENT '字段名',
  `value` VARCHAR(500) NOT NULL COMMENT '值',
  `html_tag` VARCHAR(50) NOT NULL COMMENT 'HTML标签',
  `type` Int NOT NULL COMMENT '1只读；2读写；3读写删',
  `create_time` DATETIME NULL COMMENT '创建时间',
  `update_time` DATETIME NULL COMMENT '更新时间',
  `opt` VARCHAR(20) NULL COMMENT '操作人',
  PRIMARY KEY (`id`)) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 
COMMENT = '检索字段表';

drop table if exists  `specs_template`;
CREATE TABLE `specs_template` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `name` varchar(100) NOT NULL COMMENT '模板名称',
  `status` int(10) NOT NULL COMMENT '模板名称',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `opt` varchar(20) DEFAULT NULL COMMENT '操作人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='规格模板表';

drop table if exists  `goods_specs`;
CREATE TABLE `goods_specs` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `template_id` int(10) unsigned NOT NULL COMMENT '模板编号',
  `name` varchar(100) NOT NULL COMMENT '供销内部商品ID',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `opt` varchar(20) DEFAULT NULL COMMENT '操作人',
  PRIMARY KEY (`id`),
  INDEX `idx_template_id` (`template_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='商品规格表';

drop table if exists  `goods_specs_value`;
CREATE TABLE `goods_specs_value` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `specs_id` int(10) unsigned NOT NULL COMMENT '商品ID',
  `value` varchar(100) NOT NULL COMMENT '供销内部商品ID',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `opt` varchar(20) DEFAULT NULL COMMENT '操作人',
  PRIMARY KEY (`id`),
  INDEX `idx_specs_id` (`specs_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='商品规格值表';

drop table if exists  `goods_specs_relation`;
CREATE TABLE `goods_specs_relation` (
  `specs_template_id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `goods_item_id` int(10) unsigned NOT NULL COMMENT '商品ID',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  INDEX `idx_specs_template_id` (`specs_template_id`),
  INDEX `idx_goods_item_id` (`goods_item_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='商品规格模板关联表';


drop table if exists  `goods_2b`;
CREATE TABLE `goods_2b` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `goods_id` varchar(50) NOT NULL COMMENT '商品ID',
  `goods_name` varchar(100) NOT NULL COMMENT '商品名称',
  `description` varchar(450) DEFAULT NULL COMMENT '描述',
  `status` tinyint(3) unsigned NOT NULL DEFAULT '0' COMMENT '商品状态0：下架，1：上架',
  `is_popular` tinyint(3) unsigned NOT NULL DEFAULT '0' COMMENT '是否特推0：否，1是',
  `is_hot` tinyint(3) unsigned NOT NULL DEFAULT '0' COMMENT '是否新品0：否，1是',
  `is_new` tinyint(3) unsigned NOT NULL DEFAULT '0' COMMENT '是否新品0：否，1是',
  `is_good` tinyint(3) unsigned NOT NULL DEFAULT '0' COMMENT '是否精选0：否，1是',
  `is_choice` tinyint(3) unsigned NOT NULL DEFAULT '0' COMMENT '是否渠道商品0：否，1是',
  `is_free_postfee` tinyint(3) unsigned NOT NULL DEFAULT '0' COMMENT '是否包邮0：否，1是',
  `detail_path` varchar(100) DEFAULT NULL COMMENT '详情地址',
  `index_status` tinyint(3) unsigned NOT NULL DEFAULT '0' COMMENT '是否创建lucene0：否，1是',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `opt` varchar(20) DEFAULT NULL COMMENT '操作人',
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  UNIQUE KEY `goodsId_UNIQUE` (`goods_id`),
  KEY `idx_status` (`status`),
  KEY `idx_is_popular` (`is_popular`),
  KEY `idx_is_hot` (`is_hot`),
  KEY `idx_is_new` (`is_new`),
  KEY `idx_is_good` (`is_good`),
  KEY `idx_is_choice` (`is_choice`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='2B商品表';


drop table if exists  `goods_file_2b`;
CREATE TABLE `goods_file_2b` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `goods_id` varchar(100) NOT NULL COMMENT '商品ID',
  `path` varchar(100) DEFAULT NULL COMMENT '文件路径',
  `suffix` char(5) DEFAULT NULL COMMENT '后缀',
  `store_type` tinyint(3) unsigned DEFAULT NULL COMMENT '存储类型',
  `type` tinyint(3) unsigned DEFAULT NULL COMMENT '存储类型',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `opt` varchar(20) DEFAULT NULL COMMENT '操作人',
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  UNIQUE KEY `goodsIdpath_UNIQUE` (`goods_id`,`path`),
  KEY `idx_goods_id` (`goods_id`),
  KEY `idx_suffix` (`suffix`),
  KEY `idx_store_type` (`store_type`),
  KEY `idx_type` (`type`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='2B商品文件表';


drop table if exists  `goods_first_category_2b`;
CREATE TABLE `goods_first_category_2b` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `first_id` varchar(100) NOT NULL COMMENT '一级分类ID',
  `name` varchar(100) DEFAULT NULL COMMENT '一级分类名称',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `opt` varchar(20) DEFAULT NULL COMMENT '操作人',
  PRIMARY KEY (`id`),
  UNIQUE KEY `first_id_UNIQUE` (`first_id`),
  KEY `idx_first_id` (`first_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='2B一级分类表';


drop table if exists  `goods_item_2b`;
CREATE TABLE `goods_item_2b` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `goods_id` varchar(100) NOT NULL COMMENT '商品ID',
  `item_id` varchar(50) NOT NULL COMMENT '供销内部商品ID',
  `item_code` varchar(50) NOT NULL COMMENT '商家自有编码',
  `excise_tax` decimal(5,2) DEFAULT NULL COMMENT '消费税',
  `is_promotion` tinyint(3) unsigned NOT NULL DEFAULT '0' COMMENT '是否促销0：否；1：是',
  `status` tinyint(3) unsigned DEFAULT 0 COMMENT '商品状态0：停售，1：在售',
  `discount` decimal(10,2) DEFAULT NULL COMMENT '促销折扣',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `opt` varchar(20) DEFAULT NULL COMMENT '操作人',
  PRIMARY KEY (`id`),
  UNIQUE KEY `item_id_UNIQUE` (`item_id`),
  KEY `idx_goods_id` (`goods_id`),
  KEY `idx_item_id` (`item_id`),
  KEY `idx_is_promotion` (`is_promotion`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='2B商品规格表';


drop table if exists  `goods_price_2b`;
CREATE TABLE `goods_price_2b` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `min` int(10) unsigned DEFAULT '0' COMMENT '最小数量',
  `max` int(10) unsigned DEFAULT NULL COMMENT '最大数量',
  `item_id` varchar(50) NOT NULL COMMENT '商品ID',
  `retail_price` decimal(10,2) DEFAULT '0.00' COMMENT '价格',
  `vip_price` decimal(10,2) DEFAULT NULL COMMENT '会员价格',
  `delivery_place` varchar(20) DEFAULT NULL COMMENT '发货地',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `opt` varchar(20) DEFAULT NULL COMMENT '操作人',
  PRIMARY KEY (`id`),
  KEY `idx_item_id` (`item_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='2B商品价格表';


drop table if exists  `goods_second_category_2b`;
CREATE TABLE `goods_second_category_2b` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `first_id` varchar(100) NOT NULL COMMENT '一级分类ID',
  `second_id` varchar(100) NOT NULL COMMENT 'first_id',
  `name` varchar(100) DEFAULT NULL COMMENT '二级分类名称',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `opt` varchar(20) DEFAULT NULL COMMENT '操作人',
  PRIMARY KEY (`id`),
  UNIQUE KEY `second_id_UNIQUE` (`second_id`),
  KEY `idx_second_id` (`second_id`),
  KEY `goods_second_category_first_id` (`first_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='2B二级分类表';


drop table if exists  `goods_third_category_2b`;
CREATE TABLE `goods_third_category_2b` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `second_id` varchar(100) NOT NULL COMMENT 'second_id',
  `third_id` varchar(100) NOT NULL COMMENT 'second_id',
  `name` varchar(100) DEFAULT NULL COMMENT '三级分类名称',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `opt` varchar(20) DEFAULT NULL COMMENT '操作人',
  PRIMARY KEY (`id`),
  UNIQUE KEY `third_id_UNIQUE` (`third_id`),
  KEY `idx_third_id` (`third_id`),
  KEY `goods_third_category_second_id` (`second_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='2B三级分类表';
