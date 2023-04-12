CREATE DATABASE db_profile;
\c db_profile;

CREATE TABLE userData(
id int NOT NULL,
username varchar(255) NOT NULL,
description varchar(300),
followers int,
tags varchar(255),
PRIMARY KEY (id)
);
