alter table grade add column status tinyint unsigned not null default 0 comment '状态：0：初始；1审核未通过；2审核通过；11手机号异常';
alter table grade_data add column remark varchar(200) default null comment '备注';
CREATE UNIQUE INDEX phone_uq ON grade(phone); 
alter table grade_data modify pic1_path varchar(200) default null comment '身份证照片地址';
alter table grade_data modify contacts varchar(20) default null comment '门店联系人';
alter table grade_data modify contacts_phone varchar(20) default null comment '联系人电话';
alter table grade_data modify operator_idnum varchar(50) default null comment '经营者证件号';
alter table grade_data modify address varchar(200) default null comment '门店地址';
alter table grade_data modify pic2_path varchar(200) default null comment '营业执照';
alter table grade_data modify pic3_path varchar(200) default null comment '门店照片';
alter table grade_data modify pic4_path varchar(200) default null comment '供销货架照片';
alter table grade_data modify store_operator varchar(20) default null comment '门店经营者';
alter table grade_data modify store_name varchar(100) default null comment '门店名称';

set sql_safe_updates = 0;
update grade set status = 2;