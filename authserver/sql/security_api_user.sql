CREATE DATABASE  IF NOT EXISTS `security_api`;
USE `security_api`;
--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `USER_ID` int(11) NOT NULL AUTO_INCREMENT,
  `USER_NAME` varchar(45) NOT NULL COMMENT '用户名',
  `PASSWORD` varchar(200) NOT NULL,
  `PHONE` varchar(45) NOT NULL,
  `EMAIL` varchar(45) NOT NULL,
  `STATUS` int(11) NOT NULL DEFAULT '1' COMMENT '用户状态',
  `CREATION_DATE` datetime NOT NULL COMMENT '创建时间',
  `CREATED_BY` int(11) NOT NULL COMMENT '创建人',
  `LAST_UPDATE_DATE` datetime NOT NULL COMMENT '最后修改时间',
  `LAST_UPDATED_BY` int(11) NOT NULL COMMENT '最后修改人',
  PRIMARY KEY (`USER_ID`),
  UNIQUE KEY `USER_NAME_UNIQUE` (`USER_NAME`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COMMENT='用户表';

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
INSERT INTO `user` VALUES (1,'ZHANGSAN','123','123','123',1,'2016-10-11 00:00:00',1,'2016-10-11 00:00:00',1),(2,'WANGWU','789','789','789',1,'2016-10-11 00:00:00',1,'2016-10-11 00:00:00',1);
UNLOCK TABLES;
