create table t_user
(
    id        bigint primary key,
    name      varchar(32) not null,
    gender    smallint    not null default 0,
    create_at bigint      not null,
    update_at bigint      not null,
    contact_qq varchar(32) not null default '',
    contact_address varchar(64) not null default '',
    contact_zip_code varchar(16) not null default '',
    contact_email varchar(64) not null default '',
    contact_mobile varchar(16) not null default ''
);