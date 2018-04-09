alter table order_base add tag_fun tinyint unsigned default 0 comment '标签功能：0正常订单，1预售卡单';
alter table order_base add index tag_fun(tag_fun);