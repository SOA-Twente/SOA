CREATE DATABASE db_follow;
\c db_follow;

CREATE TABLE followings(
id SERIAL PRIMARY KEY,
user_id varchar NOT NULL,
following_id varchar NOT NULL
);