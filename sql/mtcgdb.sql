CREATE DATABASE mtcgdb;

\c mtcgdb

CREATE TABLE IF NOT EXISTS t_user (
    username VARCHAR(255) PRIMARY KEY,
    password VARCHAR(255) NOT NULL,
    coins INT DEFAULT 20
);

CREATE TABLE IF NOT EXISTS t_userdata (
    username VARCHAR(255) PRIMARY KEY REFERENCES t_user(username),
    name VARCHAR(255) NOT NULL,
    bio VARCHAR(255) NOT NULL,
    image VARCHAR(255) NOT NULL
);