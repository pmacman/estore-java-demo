use estore;

create table if not exists order_detail (
     id int auto_increment,
     order_id int,
     product_id int,
     quantity int,
     cost decimal(5,2),
     tax decimal(5,2),
     primary key (id),
     foreign key (product_id) references product(id),
     foreign key (order_id) references orders(id)
) default charset=utf8;

select * from order_detail;

insert into order_detail values (1, 1, 1, 1, 100, 10);