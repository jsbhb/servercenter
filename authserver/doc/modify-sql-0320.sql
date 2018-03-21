alter table zm_auth.user drop index uq_userName;
alter table zm_auth.user add unique uq_userName (user_name);

drop table if exists  `platform_user`;

CREATE TABLE `platform_user` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `USER_ID` int(11) NOT NULL COMMENT '用户ID',
  `STATUS` int(11) NOT NULL DEFAULT '1' COMMENT '用户状态',
  `platform` int(11) DEFAULT NULL,
  `remark` varchar(100) DEFAULT NULL,
  `CREATION_DATE` datetime NULL COMMENT '创建时间',
  `LAST_UPDATE_DATE` datetime NULL COMMENT '最后修改时间',
  `LAST_UPDATED_BY` varchar(20) NULL COMMENT '最后修改人',
  PRIMARY KEY (`ID`),
  UNIQUE KEY `USER_ID_UNIQUE` (`USER_ID`,`platform`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='平台用户表';

insert into platform_user(user_id,status,platform) select user_id ,status,platform from user;