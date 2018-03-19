alter table order_goods add goods_id varchar(50) not null comment '商品ID';
alter table order_base add rebate tinyint unsigned default 0 comment '是否已经计算返佣，0未计算，1已计算';