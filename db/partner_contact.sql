use estore;

create table if not exists partner_contact (
    user_id varchar(100), /* Auth0 ID */
    partner_id int,
    primary key (user_id),
    foreign key (partner_id) references partner(id)
) default charset=utf8;

select * from partner_contact;

insert into partner_contact values ('identity-provider|123', 1);
