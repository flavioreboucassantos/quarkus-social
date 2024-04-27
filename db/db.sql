CREATE DATABASE quarkus-social;

CREATE TABLE USERS (
	id bigserial not null primary key,
	name varchar(100) not null,
	age integer not null
);

CREATE TABLE POSTS (
    id bigserial not null primary key,
    id_user bigint not null references USERS(id),
	post_text varchar(150) not null,
	date_time timestamp not null
);

CREATE TABLE RELATIONSHIP_FOLLOWERS (
    id bigserial not null primary key,
    id_user bigint not null references USERS(id),
    id_follower bigint not null references USERS(id),
    UNIQUE (id_user, id_follower)
);