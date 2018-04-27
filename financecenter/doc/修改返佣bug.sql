alter table rebate_detail add column status TINYINT UNSIGNED default 0 comment '0：待到账，1已到账';
alter table rebate_detail add column update_time DATETIME NULL COMMENT '更新时间';