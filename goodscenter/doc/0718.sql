alter table goods add is_publish tinyint(4) not null default 0;

alter table goods add index idx_is_publish (is_publish);

alter table goods_item add is_fx tinyint(4) not null default 0 comment "0:not_fx;1:fx";

alter table goods_item add index idx_is_fx (is_fx);