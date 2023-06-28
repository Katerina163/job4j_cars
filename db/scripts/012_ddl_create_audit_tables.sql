create table revinfo (
                         rev serial primary key,
                         revtstmp timestamp,
                         user_id int not null references auto_user(id)
);

create table auto_post_aud (
                               id int not null,
                               rev int not null references revinfo(rev),
                               revtype int,
                               created timestamp,
                               description text,
                               sold boolean,
                               car_id int,
                               primary key (id, rev)
);
create table car_aud (
                         id int not null,
                         rev int not null references revinfo(rev),
                         revtype int,
                         color varchar(100),
                         name varchar(100),
                         owners varchar,
                         mark_id int,
                         primary key (id, rev)
);

create table files_aud (
                           id int not null,
                           rev int not null references revinfo(rev),
                           revtype int,
                           name varchar,
                           path varchar,
                           post_id int,
                           primary key (id, rev)
);

create table price_history_aud (
                                   id int not null,
                                   rev int not null references revinfo(rev),
                                   revtype int,
                                   created timestamp,
                                   price int,
                                   post_id int,
                                   primary key (id, rev)
);