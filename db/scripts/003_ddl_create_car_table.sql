create table car (
    id       bigserial primary key,
    name     varchar   not null,
    mark_id  bigint    not null references mark(id),
    owners   varchar,
    color    varchar(100) not null
);