alter table activity_goods_record add column is_del tinyint not null default 0 comment '是否删除';
drop index goods_role_id_user_id_UNIQUE on activity_goods_record; 