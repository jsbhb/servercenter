drop table if exists  `zm_supplier`.`custom_config`;

CREATE TABLE `zm_supplier`.`custom_config` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `custom_id` INT UNSIGNED NOT NULL,
  `target_object` VARCHAR(50) NOT NULL COMMENT '目标类',
  `company_code` VARCHAR(100)  NULL COMMENT '备案编号',
  `company_name` VARCHAR(100)  NULL COMMENT '备案名称',
  `eCommerce_code` VARCHAR(100)  NULL COMMENT '电商企业编码',
  `eCommerce_name` VARCHAR(100)  NULL COMMENT '电商企业名称',
  `public_key` VARCHAR(500)  NULL COMMENT 'RSA 公钥（其他海关加密方式可能不一样，也用这个字段）',
  `private_key` VARCHAR(500)  NULL COMMENT 'RSA 私钥（其他海关加密方式可能不一样，也用这个字段）',
  `aes_key` VARCHAR(500)  NULL COMMENT 'aes秘钥',
  `customs_aes_key` VARCHAR(500)  NULL COMMENT '海关aes秘钥',
  `customs_public_key` VARCHAR(500)  NULL COMMENT '海关公钥，用来验签',
  `dxpid` VARCHAR(50)  NULL COMMENT '加签传输ID',
  `url` VARCHAR(100) NOT NULL COMMENT 'url',
  `zsurl` VARCHAR(100) NOT NULL COMMENT '总署推送加签报文url',
  `attr` VARCHAR(100) NULL COMMENT '备用',
  `create_time` DATETIME NULL,
  `update_time` DATETIME NULL,
  `opt` VARCHAR(20) NULL,
  PRIMARY KEY (`id`),
  INDEX `idx_custom_id` (`custom_id` ASC),
  UNIQUE INDEX `custom_id_UNIQUE` (`custom_id` ASC)
  ) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 
COMMENT = '海关对接配置信息';


drop table if exists  `zm_supplier`.`supplier_response`;

CREATE TABLE `zm_supplier`.`supplier_response` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `order_id` VARCHAR(50) NULL,
  `type` tinyint NOT NULL COMMENT '0:推送仓库,1:海关申报，2：加签，3：推送总署',
  `content` text NULL COMMENT '返回报文',
  `create_time` DATETIME NULL,
  `update_time` DATETIME NULL,
  `opt` VARCHAR(20) NULL,
  PRIMARY KEY (`id`),
  INDEX `idx_order_id` (`order_id` ASC)
  ) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 
COMMENT = '对接返回报文';

drop table if exists  `custom_order_return`;

CREATE TABLE `zm_supplier`.`custom_order_return` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `order_id` VARCHAR(100) NOT NULL COMMENT '订单号',
  `json_str` text NULL COMMENT '回执报文',
  `remark` VARCHAR(200) NULL COMMENT '备注',
  `create_time` DATETIME NULL COMMENT '注册时间', 
  `update_time` DATETIME NULL COMMENT '更新时间',
  `opt` VARCHAR(20) NULL COMMENT '操作人',
  PRIMARY KEY (`id`),
  INDEX `idx_orderId` (`order_id` ASC)) 
  ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 
COMMENT = '订单回执';
