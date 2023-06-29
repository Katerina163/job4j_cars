create table auto_post (
    id             bigserial primary key,
    version        int default 1 not null,
    description    text not null,
    created        timestamp not null,
    sold           boolean not null,
    car_id         bigint references car(id) unique not null,
    user_id        bigint references auto_user(id) not null
);