drop SCHEMA if exists  `zm_auth`;
CREATE SCHEMA `zm_auth` ;

use zm_auth;

drop table if exists  `role`;

CREATE TABLE `role` (
  `ROLE_ID` int(11) NOT NULL AUTO_INCREMENT,
  `ROLE_NAME` varchar(85) NOT NULL COMMENT '角色名称',
  `ROLE_TYPE` varchar(45) NOT NULL COMMENT '角色类型',
  `STATUS` int(11) NOT NULL DEFAULT '1',
  `CREATION_DATE` datetime NOT NULL COMMENT '创建时间',
  `CREATED_BY` int(11) NOT NULL COMMENT '创建人',
  `LAST_UPDATE_DATE` datetime NOT NULL COMMENT '最后更新时间',
  `LAST_UPDATED_BY` int(11) NOT NULL COMMENT '最后更新人',
  PRIMARY KEY (`ROLE_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COMMENT='角色表';


CREATE TABLE `user` (
  `USER_ID` int(11) NOT NULL AUTO_INCREMENT,
  `user_name` varchar(100) DEFAULT NULL,
  `password` varchar(100) DEFAULT NULL,
  `phone` varchar(100) DEFAULT NULL,
  `email` varchar(100) DEFAULT NULL,
  `STATUS` int(11) NOT NULL DEFAULT '1' COMMENT '用户状态',
  `CREATION_DATE` datetime NOT NULL COMMENT '创建时间',
  `CREATED_BY` int(11) NOT NULL COMMENT '创建人',
  `LAST_UPDATE_DATE` datetime NOT NULL COMMENT '最后修改时间',
  `LAST_UPDATED_BY` int(11) NOT NULL COMMENT '最后修改人',
  `openid` varchar(100) DEFAULT NULL,
  `platform` int(11) DEFAULT NULL,
  `usercenterid` int(11) NOT NULL, 
  PRIMARY KEY (`USER_ID`),
  UNIQUE KEY `USER_NAME_UNIQUE` (`user_name`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8 COMMENT='用户表';



drop table if exists  `user_role`;

CREATE TABLE `user_role` (
  `USER_ROLE_ID` int(11) NOT NULL AUTO_INCREMENT,
  `USER_ID` int(11) NOT NULL COMMENT '用户ID',
  `ROLE_ID` int(11) NOT NULL COMMENT '角色ID',
  PRIMARY KEY (`USER_ROLE_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COMMENT='用户角色表';


INSERT INTO `zm_auth`.`user` (`user_id`, `user_name`, `STATUS`, `CREATION_DATE`, `CREATED_BY`, `LAST_UPDATE_DATE`, `LAST_UPDATED_BY`, `usercenterid`, `platform`) VALUES ('21','admin', '1', now(), '1', now(), '1', '8001', '1');



