use zm_user;

alter table grade add column `shop_extension_flg` tinyint(3) unsigned NOT NULL DEFAULT 0 COMMENT '微店推广功能 0:无';