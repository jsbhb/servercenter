alter table user_wechat add login_type tinyint unsigned default 2 comment '登录类型，默认2：微信公众号。5：微信小程序';
drop index awechat_UNIQUE on user_wechat;
CREATE UNIQUE INDEX awechat_UNIQUE ON user_wechat (login_type,wechat);
CREATE INDEX idx_login ON user_wechat(login_type);
set sql_safe_updates = 0;
update user_wechat set user_type = 5;