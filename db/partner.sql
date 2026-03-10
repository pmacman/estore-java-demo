use estore;

create table if not exists partner (
    id int auto_increment,
    company_name varchar(100) not null,
    company_description varchar(2000),
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

select * from partner;

insert into partner values (1, 'Company 1', 'Company 1 description', '123 Elm St', 'Ste 101', 'Chicago', 17, '60611', 'US', "555-555-5555", 'company1@email.com', 1);
