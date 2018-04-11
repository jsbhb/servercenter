drop table if exists  `grade_type`;

CREATE TABLE `zm_user`.`grade_type` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `parent_id` INT UNSIGNED NOT NULL DEFAULT 0 COMMENT '父级ID',
  `name` varchar(100) NULL COMMENT '类型名称',
  `description` varchar(100) NULL COMMENT '描述',
  `create_time` DATETIME NULL COMMENT '注册时间', 
  `update_time` DATETIME NULL COMMENT '更新时间',
  `opt` VARCHAR(50) NULL COMMENT '操作人',
  PRIMARY KEY (`id`),
  INDEX `idx_parentId` (`parent_id`)
  ) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 
COMMENT = '等级类型表';


alter table grade add column copy_mall TINYINT UNSIGNED default 0 comment '是否需要复制商城，0否，1是';

alter table user change column center_id  mall_id INT NULL COMMENT '商城ID';

alter table user change column shop_id  grade_id INT NULL COMMENT 'gradeID';

alter table user_vip change column center_id  mall_id INT NULL COMMENT '商城ID';

alter table vip_price change column center_id  mall_id INT NULL COMMENT '商城ID';

alter table grade change column grade_type  grade_type TINYINT UNSIGNED NOT NULL COMMENT '客户类型';

alter table grade modify column grade_level TINYINT UNSIGNED NULL COMMENT '等级';

delimiter $$
DROP FUNCTION IF EXISTS `zm_user`.`getGradeChildLst` $$
CREATE FUNCTION `getGradeChildLst`(rootId INT)
     RETURNS varchar(2000)
     BEGIN
       DECLARE sTemp VARCHAR(2000);
       DECLARE sTempChd VARCHAR(2000);
  
       set sTemp = '$';
       SET sTempChd =cast(rootId as CHAR);
    
       WHILE sTempChd is not null DO
        SET sTemp = concat(sTemp,',',sTempChd);
         SELECT group_concat(id) INTO sTempChd FROM grade where FIND_IN_SET(parent_id,sTempChd)>0;
       END WHILE;
       RETURN sTemp;
     END $$

insert into grade_type(parent_id,name,create_time) values (0,'海外购',now());
insert into grade_type(parent_id,name,create_time) values (1,'区域中心',now());
insert into grade_type(parent_id,name,create_time) values (2,'门店',now());
insert into grade_type(parent_id,name,create_time) values (3,'推手',now());