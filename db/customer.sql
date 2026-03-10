use estore;

create table if not exists customer (
    id int auto_increment,
    user_id varchar(100), /* Auth0 ID */
    first_name varchar(100) not null,
    last_name varchar(100) not null,
    street1 varchar(100),
    street2 varchar(100),
    city varchar(50),
    state_id int,
    zip varchar(15),
    country_iso2 char(2),
    phone varchar(25),
    email varchar(320),
    active bit,
    primary key (id),
    foreign key (state_id) references state(id),
    foreign key (country_iso2) references country(iso2)

) default charset=utf8;

select * from customer;

insert into customer values (1, 'identity-provider|123', 'John', 'Smith', '123 Elm St', 'Ste 100', 'Chicago', 17, '60611', 'US', '773-000-0000', 'jsmith@gmail.com', 1)