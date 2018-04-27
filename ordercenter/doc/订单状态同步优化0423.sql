alter table third_order_info add column order_status tinyint unsigned default 0 comment '总订单的状态';
alter table third_order_info add column item_id varchar(50) default null comment '商品明细Id';
alter table third_order_info add column item_name varchar(200) default null comment '商品名称';
alter table third_order_info add column item_code varchar(100) default null comment '商家商品编码';
alter table third_order_info add index idx_orderStatus (order_status);