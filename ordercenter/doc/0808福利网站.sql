alter table order_shopping_cart add column platform_type tinyint unsigned not null default 0 comment '平台类型 0：普通，1：福利网站';
ALTER TABLE order_shopping_cart DROP INDEX uk_shoppingcart;
CREATE UNIQUE INDEX uk_shoppingcart ON order_shopping_cart(`user_id`,`item_id`,`grade_id`,`platform_type`); 