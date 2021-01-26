create table person
(
    id       serial      not null primary key,
    login    varchar(50) not null unique,
    password varchar(200)
);

create table room
(
    id           serial      not null primary key,
    name_room    varchar(50) not null unique,
    ownership_id integer     not null references person
);

create table message
(
    id           serial        not null primary key,
    message      varchar(1000) not null,
    ownership_id integer       not null references person,
    room_id      integer       not null references room
);
