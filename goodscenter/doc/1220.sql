use zm_goods;
alter table goods_second_category add column tag_path varchar(200) null comment '图标路径';
alter table goods_third_category add column tag_path varchar(200) null comment '图标路径';


alter table goods_item add column make_price DECIMAL(10,2) NOT NULL DEFAULT 0.0 comment '商品划线价';