use zm_user;

alter table grade add column `calc_rebate_flg` tinyint(3) unsigned NOT NULL DEFAULT 0 COMMENT '线下订单是否计算返佣 0:不计算';
alter table grade add column `grade_inviter` varchar(50) NULL COMMENT '分级邀请人';
alter table grade add column `profit_ratio` tinyint(3) unsigned NULL COMMENT '利润比例';