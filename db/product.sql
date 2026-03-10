use estore;

create table if not exists product (
    id int auto_increment,
    partner_id int,
    name varchar(100) not null,
    description varchar(2000) not null,
    price decimal(5,2) not null,
    quantity int not null,
    status varchar(25),
    primary key (id),
    foreign key (partner_id) references partner(id)
) default charset=utf8;

select * from product;

insert into product values (1, 1, 'product1', 'description', 100, 1, 'ACTIVE');
