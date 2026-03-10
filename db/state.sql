use estore;

create table if not exists state (
     id int auto_increment,
     abbreviation char(2) not null,
     name varchar(50) not null,
     country_iso2 char(2) not null,
     primary key (id),
     foreign key (country_iso2) references country(iso2)
) default charset=utf8;

select * from state;

insert into state values (1, 'AK', 'Alaska', 'US');
insert into state values (2, 'AL', 'Alabama', 'US');
insert into state values (3, 'AR', 'Arkansas', 'US');
insert into state values (4, 'AS', 'American Samoa', 'US');
insert into state values (5, 'AZ', 'Arizona', 'US');
insert into state values (6, 'CA', 'California', 'US');
insert into state values (7, 'CO', 'Colorado', 'US');
insert into state values (8, 'CT', 'Connecticut', 'US');
insert into state values (9, 'DC', 'District of Columbia', 'US');
insert into state values (10, 'DE', 'Delaware', 'US');
insert into state values (11, 'FL', 'Florida', 'US');
insert into state values (12, 'GA', 'Georgia', 'US');
insert into state values (13, 'GU', 'Guam', 'US');
insert into state values (14, 'HI', 'Hawaii', 'US');
insert into state values (15, 'IA', 'Iowa', 'US');
insert into state values (16, 'ID', 'Idaho', 'US');
insert into state values (17, 'IL', 'Illinois', 'US');
insert into state values (18, 'IN', 'Indiana', 'US');
insert into state values (19, 'KS', 'Kansas', 'US');
insert into state values (20, 'KY', 'Kentucky', 'US');
insert into state values (21, 'LA', 'Louisiana', 'US');
insert into state values (22, 'MA', 'Massachusetts', 'US');
insert into state values (23, 'MD', 'Maryland', 'US');
insert into state values (24, 'ME', 'Maine', 'US');
insert into state values (25, 'MI', 'Michigan', 'US');
insert into state values (26, 'MN', 'Minnesota', 'US');
insert into state values (27, 'MO', 'Missouri', 'US');
insert into state values (28, 'MP', 'Northern Mariana Islands', 'US');
insert into state values (29, 'MS', 'Mississippi', 'US');
insert into state values (30, 'MT', 'Montana', 'US');
insert into state values (31, 'NC', 'North Carolina', 'US');
insert into state values (32, 'ND', 'North Dakota', 'US');
insert into state values (33, 'NE', 'Nebraska', 'US');
insert into state values (34, 'NH', 'New Hampshire', 'US');
insert into state values (35, 'NJ', 'New Jersey', 'US');
insert into state values (36, 'NM', 'New Mexico', 'US');
insert into state values (37, 'NV', 'Nevada', 'US');
insert into state values (38, 'NY', 'New York', 'US');
insert into state values (39, 'OH', 'Ohio', 'US');
insert into state values (40, 'OK', 'Oklahoma', 'US');
insert into state values (41, 'OR', 'Oregon', 'US');
insert into state values (42, 'PA', 'Pennsylvania', 'US');
insert into state values (43, 'PR', 'Puerto Rico', 'US');
insert into state values (44, 'RI', 'Rhode Island', 'US');
insert into state values (45, 'SC', 'South Carolina', 'US');
insert into state values (46, 'SD', 'South Dakota', 'US');
insert into state values (47, 'TN', 'Tennessee', 'US');
insert into state values (48, 'TX', 'Texas', 'US');
insert into state values (49, 'UM', 'United States Minor Outlying Islands', 'US');
insert into state values (50, 'UT', 'Utah', 'US');
insert into state values (51, 'VA', 'Virginia', 'US');
insert into state values (52, 'VI', 'Virgin Islands', 'US');
insert into state values (53, 'VT', 'Vermont', 'US');
insert into state values (54, 'WA', 'Washington', 'US');
insert into state values (55, 'WI', 'Wisconsin', 'US');
insert into state values (56, 'WV', 'West Virginia', 'US');
insert into state values (57, 'WY', 'Wyoming', 'US');

insert into state values (58, 'AB', 'Alberta', 'CA');
insert into state values (59, 'BC', 'British Columbia', 'CA');
insert into state values (60, 'MB', 'Manitoba', 'CA');
insert into state values (61, 'NB', 'New Brunswick', 'CA');
insert into state values (62, 'NL', 'Newfoundland and Labrador', 'CA');
insert into state values (63, 'NS', 'Nova Scotia', 'CA');
insert into state values (64, 'NT', 'Northwest Territories', 'CA');
insert into state values (65, 'NU', 'Nunavut', 'CA');
insert into state values (66, 'ON', 'Ontario', 'CA');
insert into state values (67, 'PE', 'Prince Edward Island', 'CA');
insert into state values (68, 'QC', 'Quebec', 'CA');
insert into state values (69, 'SK', 'Saskatchewan', 'CA');
insert into state values (70, 'YT', 'Yukon', 'CA');