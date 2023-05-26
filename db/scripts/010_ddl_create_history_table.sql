create table history
(
    id serial primary key,
    start_at timestamp not null,
    end_at timestamp not null
);