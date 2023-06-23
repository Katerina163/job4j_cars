create table auto_post (
    id             bigserial primary key,
    description    text not null,
    created        timestamp not null,
    sold           boolean not null,
    car_id         int references car(id) unique not null,
    user_id        int references auto_user(id) not null
);