use zm_goods;

alter table goods_price add column `internal_price` DECIMAL(10,2) NULL COMMENT '内供价';
alter table goods_price add column `department_profit` INT(11) NOT NULL DEFAULT 0 COMMENT '部门利润';
alter table goods_price add column `instant_ratio` INT(11) NOT NULL DEFAULT 0 COMMENT '瞬加比例';