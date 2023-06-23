create table price_history (
   id           bigserial primary key,
   before       bigint not null,
   after        bigint not null,
   created      timestamp without time zone default now(),
   post_id      int references auto_post (id) not null
);