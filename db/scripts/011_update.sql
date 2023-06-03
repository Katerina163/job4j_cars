alter table auto_post add column car_id int references car(id);
alter table auto_post add sold boolean not null;