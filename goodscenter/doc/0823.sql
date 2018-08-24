alter table goods_item add is_tmpdown tinyint(4) not null default 0 comment "是否临时下架，0否；1是";

alter table goods_item add index idx_is_tmpdown (is_tmpdown);