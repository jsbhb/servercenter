alter table order_detail add rebate_fee DECIMAL(10,2) default null comment '返佣支付金额';
alter table order_base add purchasing_order tinyint unsigned default 0 comment '采购订单0：否，1是';