create table t_user
(
    id        bigint primary key,
    name      varchar(32) not null,
    gender    smallint    not null default 0,
    create_at bigint      not null,
    update_at bigint      not null
);