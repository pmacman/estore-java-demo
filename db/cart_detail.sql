use estore;

create table if not exists cart_detail (
    id int auto_increment,
    cart_id int,
    product_id int,
    quantity int,
    primary key (id),
    foreign key (cart_id) references cart(id),
    foreign key (product_id) references product(id)
) default charset=utf8;

select * from cart_detail;

insert into cart_detail values (1, 1, 1, 1);
