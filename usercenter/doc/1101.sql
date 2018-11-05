alter table zm_order.order_shopping_cart modify column item_quantity int(10) NOT NULL;


alter table zm_user.grade_config add column invite_logo varchar(200) DEFAULT NULL COMMENT '邀请开店透明底logo';
alter table zm_user.grade_config add column qrcode_logo varchar(200) DEFAULT NULL COMMENT '二维码中心白底logo';