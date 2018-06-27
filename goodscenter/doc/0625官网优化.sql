alter table goods_first_category add column sort smallint unsigned default 0 comment '排序';
alter table goods_second_category add column sort smallint unsigned default 0 comment '排序';
alter table goods_third_category add column sort smallint unsigned default 0 comment '排序';

alter table goods_first_category add column status tinyint unsigned default 1 comment '是否显示，1显示，0否';
alter table goods_second_category add column status tinyint unsigned default 1 comment '是否显示，1显示，0否';
alter table goods_third_category add column status tinyint unsigned default 1 comment '是否显示，1显示，0否';

alter table goods_first_category add index sort(sort);
alter table goods_second_category add index sort(sort);
alter table goods_third_category add index sort(sort);
alter table goods_first_category add index status(status);
alter table goods_second_category add index status(status);
alter table goods_third_category add index status(status);