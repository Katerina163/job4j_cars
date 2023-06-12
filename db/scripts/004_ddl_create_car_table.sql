create table car (
    id       serial primary key,
    name     varchar not null,
    mark_id  int not null references mark(id),
    color_id int not null references color(id)
);