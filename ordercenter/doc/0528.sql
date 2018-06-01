alter table order_base add is_manual tinyint unsigned not null default 0 comment '是否手工单：0否，1是';
alter table order_base add index is_manual(is_manual);

alter table zm_order.order_base modify column order_id varchar(100);
 
alter table zm_order.order_goods modify column order_id varchar(100);
 
alter table zm_order.order_detail modify column order_id varchar(100);
 
alter table zm_order.order_back_record modify column order_id varchar(100);
 
alter table zm_order.order_status_record modify column order_id varchar(100);

alter table zm_order.third_order_info modify column order_id varchar(100);