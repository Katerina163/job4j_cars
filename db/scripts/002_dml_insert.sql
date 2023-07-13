--liquibase formatted sql
--changeset Katerina163:1
insert into auto_user(login, password) values ('admin', 'admin');
insert into auto_user(login, password) values ('Ivanov', 'root');
insert into auto_user(login, password) values ('Petrov', 'root');
insert into auto_user(login, password) values ('Sidorov', 'root');
--rollback truncate table auto_user;

--changeset Katerina163:2
insert into mark(name) values ('Mustang');
insert into mark(name) values ('Volkswagen');
insert into mark(name) values ('Fiat');
insert into mark(name) values ('Audi');
insert into mark(name) values ('Mercedes-Benz');
insert into mark(name) values ('Lamborghini');
insert into mark(name) values ('BMW');
--rollback truncate table mark;

--changeset Katerina163:3
insert into car(name, mark_id, color, owners) values ('500', (select mark.id from mark where mark.name = 'Fiat'), 'GREEN', 'Иван Иванов');
insert into car(name, mark_id, color, owners) values ('Huracán Evo', (select mark.id from mark where mark.name = 'Lamborghini'), 'RED', 'Александра Александровна, Петр Петрович');
insert into car(name, mark_id, color, owners) values ('RS 5', (select mark.id from mark where mark.name = 'Audi'), 'YELLOW', 'Андрей Андреевич, Василий Васильевич');
--rollback truncate table car;

--changeset Katerina163:4
insert into auto_post(description, created, user_id, sold, car_id, "version")
values ('Продаю небольшой автомобиль с яркой зеленой окраской за хорошую цену. Он имеет круглую форму и привлекательный дизайн. Это машина для городской езды, компактная и удобная в управлении.',
        now(), (select auto_user.id from auto_user where login = 'Ivanov'), false, (select car.id from car where car.name = '500'), 0);
insert into auto_post(description, created, user_id, sold, car_id, "version")
values ('Красный Lamborghini Huracán Evo - это спортивный автомобиль, который имеет высокую скорость и мощный двигатель. Он имеет яркий красный цвет, что позволяет ему выделяться на дороге. Внутри автомобиля находится удобное и комфортное сиденье для водителя, которое обеспечивает максимальное удобство во время езды. Красный Lamborghini Huracán Evo является уникальным эталоном стиля и комфорта в сфере автоиндустрии.',
        now(), (select auto_user.id from auto_user where login = 'Ivanov'), false, (select car.id from car where car.name = 'Huracán Evo'), 0);
insert into auto_post(description, created, user_id, sold, car_id, "version")
values ('Быстрый и спортивный автомобиль, оснащенный мощным 2,9-литровым битурбированным двигателем, который способен разгоняться до 100 км/ч всего за 3,9 секунды. Он имеет красивый и яркий желтый окрас, а также светодиодные передние и задние фары, которые придают ему уникальный стиль. Внутри RS 5 оборудован качественными материалами и современной технологией, включая дисплей MMI, систему звукового оформления Bang & Olufsen и множество других функций и опций.',
        now(), (select auto_user.id from auto_user where login = 'Petrov'), true, (select car.id from car where car.name = 'RS 5'), 0);
--rollback truncate table auto_post;

--changeset Katerina163:5
insert into participates(user_id, post_id)
values ((select auto_user.id from auto_user where login = 'Petrov'), (select auto_post.id from auto_post where car_id = (select car.id from car where name = '500')));
insert into participates(user_id, post_id)
values ((select auto_user.id from auto_user where login = 'Ivanov'), (select auto_post.id from auto_post where car_id = (select car.id from car where name = 'RS 5')));
insert into participates(user_id, post_id)
values ((select auto_user.id from auto_user where login = 'Petrov'), (select auto_post.id from auto_post where car_id = (select car.id from car where name = 'Huracán Evo')));
insert into participates(user_id, post_id)
values ((select auto_user.id from auto_user where login = 'Sidorov'), (select auto_post.id from auto_post where car_id = (select car.id from car where name = '500')));
insert into participates(user_id, post_id)
values ((select auto_user.id from auto_user where login = 'Sidorov'), (select auto_post.id from auto_post where car_id = (select car.id from car where name = 'Huracán Evo')));
insert into participates(user_id, post_id)
values ((select auto_user.id from auto_user where login = 'Sidorov'), (select auto_post.id from auto_post where car_id = (select car.id from car where name = 'RS 5')));
--rollback truncate table participates;

--changeset Katerina163:6
insert into price_history(price, post_id) values (450000, (select auto_post.id from auto_post where car_id = (select car.id from car where name = '500')));
insert into price_history(price, post_id) values (546000, (select auto_post.id from auto_post where car_id = (select car.id from car where name = '500')));
insert into price_history(price, post_id) values (16500000, (select auto_post.id from auto_post where car_id = (select car.id from car where name = 'Huracán Evo')));
insert into price_history(price, post_id) values (4200000, (select auto_post.id from auto_post where car_id = (select car.id from car where name = 'RS 5')));
insert into price_history(price, post_id) values (5120000, (select auto_post.id from auto_post where car_id = (select car.id from car where name = 'RS 5')));
insert into price_history(price, post_id) values (4620000, (select auto_post.id from auto_post where car_id = (select car.id from car where name = 'RS 5')));
--rollback truncate table price_history;