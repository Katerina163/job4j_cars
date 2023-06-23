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

insert into price_history(before, after, post_id) values (450000, 450000, (select auto_post.id from auto_post where car_id = (select car.id from car where name = '500')));
insert into price_history(before, after, post_id) values (450000, 546000, (select auto_post.id from auto_post where car_id = (select car.id from car where name = '500')));
insert into price_history(before, after, post_id) values (16500000, 16500000, (select auto_post.id from auto_post where car_id = (select car.id from car where name = 'Huracán Evo')));
insert into price_history(before, after, post_id) values (4200000, 4200000, (select auto_post.id from auto_post where car_id = (select car.id from car where name = 'RS 5')));
insert into price_history(before, after, post_id) values (4200000, 5120000, (select auto_post.id from auto_post where car_id = (select car.id from car where name = 'RS 5')));
insert into price_history(before, after, post_id) values (5120000, 4620000, (select auto_post.id from auto_post where car_id = (select car.id from car where name = 'RS 5')));