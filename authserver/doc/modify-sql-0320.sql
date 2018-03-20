alter table zm_auth.user drop index uq_userName;
alter table zm_auth.user add unique uq_userName (user_name);

insert into platform_user(user_id,status,platform) select user_id ,status,platform from user;