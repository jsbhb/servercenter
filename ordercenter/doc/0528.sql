alter table order_base add is_manual tinyint unsigned not null default 0 comment '是否手工单：0否，1是';
alter table order_base add index is_manual(is_manual);