--liquibase formatted sql
--changeset Katerina163:1
create table if not exists auto_user (
    id        bigserial primary key,
    login     varchar   unique not null,
    password  varchar   not null
);
--rollback drop table auto_user;

--changeset Katerina163:2
create table if not exists mark (
    id   serial primary key,
    name varchar(100) unique not null
);
--rollback drop table mark;

--changeset Katerina163:3
create table if not exists car (
    id       bigserial primary key,
    name     varchar   not null,
    mark_id  bigint    not null references mark(id),
    owners   varchar,
    color    varchar(100) not null
);
--rollback drop table car;

--changeset Katerina163:4
create table if not exists auto_post (
    id             bigserial primary key,
    version        int default 1 not null,
    description    text not null,
    created        timestamp not null,
    sold           boolean not null,
    car_id         bigint references car(id) unique not null,
    user_id        bigint references auto_user(id) not null
);
--rollback drop table auto_post;

--changeset Katerina163:5
create table if not exists price_history (
   id           bigserial primary key,
   price        bigint not null,
   created      timestamp without time zone default now(),
   post_id      bigint references auto_post (id) not null
);
--rollback drop table price_history;

--changeset Katerina163:6
create table if not exists participates (
    id      bigserial primary key,
    user_id bigint not null,
    post_id bigint not null,
        unique (user_id, post_id),
    foreign key (user_id) references auto_user(id) on delete cascade,
    foreign key (post_id) references auto_post(id) on delete cascade
);
--rollback drop table participates;

--changeset Katerina163:7
create table if not exists files (
    id      bigserial primary key,
    name    varchar not null,
    path    varchar not null unique,
    post_id bigint references auto_post(id) not null
);
--rollback drop table files;