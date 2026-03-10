use estore;

create table if not exists orders (
     id int auto_increment,
     customer_id int,
     shipping_method varchar(25),
     shipping_cost decimal(5,2),
     status varchar(25),
     primary key (id),
     foreign key (customer_id) references customer(id)
) default charset=utf8;

select * from orders;

insert into orders values (1, 1, 'STANDARD', 5, 'PENDING');