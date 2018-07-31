alter table grade add column appKey varchar(30) default null comment '对接用户appkey';
alter table grade add column appSecret varchar(100) default null comment '对接用户appSecret';
alter table grade add column type tinyint unsigned default 1 comment '客户类型 1：普通客户，2：对接客户';