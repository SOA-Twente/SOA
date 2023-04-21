CREATE DATABASE profiledatabase;
\c profiledatabase;

CREATE TABLE followings(
id SERIAL PRIMARY KEY,
user_id varchar NOT NULL,
following_id varchar NOT NULL
);

CREATE TABLE quacks(
id SERIAL PRIMARY KEY,
user_id varchar NOT NULL,
quack varchar(300) NOT NULL,
is_reply boolean,
reply_to_quack_id int,
is_retweet boolean,
retweet_of_quack_id int,
created_at TIMESTAMP DEFAULT NOW()
);

CREATE TABLE users(
id SERIAL PRIMARY KEY,
username varchar(255) NOT NULL,
email varchar(255) NOT NULL,
password varchar(255)
);