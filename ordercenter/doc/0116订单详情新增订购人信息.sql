alter table order_detail add column customer_idnum varchar(45) default null comment '订购人身份证';
alter table order_detail add column customer_name varchar(25) default null comment '订购人姓名';
alter table order_detail add column customer_phone varchar(20) default null comment '订购人手机号';