alter table supplier_interface add column account_id varchar(50) default null comment 'accountId';
alter table supplier_interface add column url varchar(200) default null comment 'url';
alter table supplier_interface add column member_id varchar(50) default null comment 'memberId';