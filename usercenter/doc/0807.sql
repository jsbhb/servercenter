use zm_user;

alter table grade add column welfare_type TINYINT UNSIGNED NOT NULL DEFAULT 0 comment '福利类型';
alter table grade add column welfare_rebate decimal(12,6) default 0 comment '福利比例';

drop table if exists  `welfare_inviter`;

CREATE TABLE `zm_user`.`welfare_inviter` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `grade_id` INT NULL COMMENT '分级ID',
  `name` VARCHAR(50) NULL COMMENT '名称',
  `phone` VARCHAR(15) NULL COMMENT '手机',
  `invitation_code` VARCHAR(100) NULL COMMENT '邀请码',
  `status` TINYINT UNSIGNED NULL DEFAULT 0 COMMENT '邀请码状态0未生成，1未发送，2已发送，3绑定，4作废,5发送失败',
  `user_center_id` int(10) unsigned DEFAULT NULL COMMENT '邀请码绑定的用户ID',
  `remark` VARCHAR(100)  DEFAULT NULL COMMENT '备注',
  `create_time` DATETIME NULL COMMENT '注册时间', 
  `update_time` DATETIME NULL COMMENT '更新时间',
  `opt` VARCHAR(50) NULL COMMENT '操作人',
  PRIMARY KEY (`id`),
  INDEX `idx_grade_id` (`grade_id`),
  INDEX `idx_phone` (`phone`),
  INDEX `idx_invitation_code` (`invitation_code`),
  INDEX `idx_status` (`status`),
  INDEX `idx_user_center_id` (`user_center_id`),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC),
  UNIQUE INDEX `grade_id_phone_UNIQUE` (`grade_id`,`phone` ASC),
  UNIQUE INDEX `grade_id_invitation_code_UNIQUE` (`grade_id`,`invitation_code` ASC)) 
  ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 
COMMENT = '福利商城邀请人表';
