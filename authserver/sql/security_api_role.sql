CREATE DATABASE  IF NOT EXISTS `security_api`;
USE `security_api`;


DROP TABLE IF EXISTS `role`;

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

--
-- Dumping data for table `role`
--

LOCK TABLES `role` WRITE;
INSERT INTO `role` VALUES (1,'超级管理员','ADMIN',1,'2016-10-11 00:00:00',1,'2016-10-11 00:00:00',1),(2,'普通用户','USER',1,'2016-10-11 00:00:00',1,'2016-10-11 00:00:00',1);
UNLOCK TABLES;
