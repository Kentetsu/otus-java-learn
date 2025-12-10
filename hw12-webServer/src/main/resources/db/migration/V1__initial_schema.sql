create sequence client_seq start with 1 increment by 1;

create table address (
    id bigserial not null primary key,
    street varchar(255)
);

create table client (
    id bigint not null primary key,
    name varchar(50),
    password VARCHAR(255) DEFAULT '11111',
    is_admin BOOLEAN,
    address_id bigint,
    foreign key (address_id) references address(id)
);

create table phones (
    id bigserial not null primary key,
    number varchar(20),
    client_id bigint,
    foreign key (client_id) references client(id)
);
