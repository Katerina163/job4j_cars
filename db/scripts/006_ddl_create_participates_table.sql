create table participates (
    id      bigserial primary key,
    user_id int not null,
    post_id int not null,
        unique (user_id, post_id),
    foreign key (user_id) references auto_user(id) on delete cascade,
    foreign key (post_id) references auto_post(id) on delete cascade
);