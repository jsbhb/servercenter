alter table goods add column is_del tinyint unsigned default 0 comment '是否删除，0否；1是';
alter table goods_item add column is_del tinyint unsigned default 0 comment '是否删除，0否；1是';
alter table goods add index is_del(is_del);
alter table goods_item add index is_del(is_del);