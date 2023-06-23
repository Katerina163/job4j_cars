create table auto_user (
    id        bigserial primary key,
    login     varchar   unique not null,
    password  varchar   not null
);