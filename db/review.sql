use estore;

create table if not exists review (
                                      id int auto_increment,
                                      customer_id int,
                                      product_id int,
                                      review varchar(2000),
                                      primary key (id),
                                      foreign key (customer_id) references customer(id),
                                      foreign key (product_id) references product(id)
) default charset=utf8;

select * from review;

insert into review values (1, 1, 1,  "This product really works!");