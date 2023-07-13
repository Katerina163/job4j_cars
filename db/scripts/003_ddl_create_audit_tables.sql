--liquibase formatted sql
--changeset Katerina163:1
create table revinfo (
                         rev serial primary key,
                         revtstmp timestamp,
                         user_id int not null references auto_user(id)
);
--rollback drop table revinfo;

--changeset Katerina163:2
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
--rollback drop table auto_post_aud;

--changeset Katerina163:3
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
--rollback drop table car_aud;

--changeset Katerina163:4
create table files_aud (
                           id int not null,
                           rev int not null references revinfo(rev),
                           revtype int,
                           name varchar,
                           path varchar,
                           post_id int,
                           primary key (id, rev)
);
--rollback drop table files_aud;

--changeset Katerina163:5
create table price_history_aud (
                                   id int not null,
                                   rev int not null references revinfo(rev),
                                   revtype int,
                                   created timestamp,
                                   price int,
                                   post_id int,
                                   primary key (id, rev)
);
--rollback drop table price_history_aud;