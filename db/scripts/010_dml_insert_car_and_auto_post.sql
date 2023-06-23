insert into car(name, mark_id, color, owners) values ('500', (select mark.id from mark where mark.name = 'Fiat'), 'GREEN', 'Иван Иванов');
insert into car(name, mark_id, color, owners) values ('Huracán Evo', (select mark.id from mark where mark.name = 'Lamborghini'), 'RED', 'Александра Александровна, Петр Петрович');
insert into car(name, mark_id, color, owners) values ('RS 5', (select mark.id from mark where mark.name = 'Audi'), 'YELLOW', 'Андрей Андреевич, Василий Васильевич');

insert into auto_post(description, created, user_id, sold, car_id)
values ('Продаю небольшой автомобиль с яркой зеленой окраской за хорошую цену. Он имеет круглую форму и привлекательный дизайн. Это машина для городской езды, компактная и удобная в управлении.',
        now(), (select auto_user.id from auto_user where login = 'Ivanov'), false, (select car.id from car where car.name = '500'));
insert into auto_post(description, created, user_id, sold, car_id)
values ('Красный Lamborghini Huracán Evo - это спортивный автомобиль, который имеет высокую скорость и мощный двигатель. Он имеет яркий красный цвет, что позволяет ему выделяться на дороге. Внутри автомобиля находится удобное и комфортное сиденье для водителя, которое обеспечивает максимальное удобство во время езды. Красный Lamborghini Huracán Evo является уникальным эталоном стиля и комфорта в сфере автоиндустрии.',
        now(), (select auto_user.id from auto_user where login = 'Ivanov'), false, (select car.id from car where car.name = 'Huracán Evo'));
insert into auto_post(description, created, user_id, sold, car_id)
values ('Быстрый и спортивный автомобиль, оснащенный мощным 2,9-литровым битурбированным двигателем, который способен разгоняться до 100 км/ч всего за 3,9 секунды. Он имеет красивый и яркий желтый окрас, а также светодиодные передние и задние фары, которые придают ему уникальный стиль. Внутри RS 5 оборудован качественными материалами и современной технологией, включая дисплей MMI, систему звукового оформления Bang & Olufsen и множество других функций и опций.',
        now(), (select auto_user.id from auto_user where login = 'Petrov'), true, (select car.id from car where car.name = 'RS 5'));