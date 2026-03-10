use estore;

create table if not exists cart (
    id int auto_increment,
    customer_id int,
    shipping_method varchar(25),
    primary key (id),
    foreign key (customer_id) references customer(id)
) default charset=utf8;

select * from cart;

insert into cart values (1, 1, '');
