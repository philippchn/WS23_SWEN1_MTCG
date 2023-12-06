CREATE DATABASE userdb;

\c userdb

CREATE TABLE IF NOT EXISTS usertable (
    username VARCHAR(255) PRIMARY KEY,
    password VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS userdatatable (
    username VARCHAR(255) PRIMARY KEY REFERENCES usertable(username),
    name VARCHAR(255) NOT NULL,
    bio VARCHAR(255) NOT NULL,
    image VARCHAR(255) NOT NULL
);