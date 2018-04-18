alter table order_base change column center_id  mall_id INT NULL COMMENT '商城ID';

alter table order_base change column shop_id  grade_id INT NULL COMMENT 'gradeID';

alter table order_shopping_cart change column center_id  grade_id INT NULL COMMENT 'gradeID';

update order_base set grade_id = mall_id where grade_id is null;