create table price_history (
   id           bigserial primary key,
   price        bigint not null,
   created      timestamp without time zone default now(),
   post_id      bigint references auto_post (id) not null
);