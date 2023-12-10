CREATE DATABASE userdb;

\c userdb

CREATE TABLE IF NOT EXISTS usertable (
    username VARCHAR(255) PRIMARY KEY,
    password VARCHAR(255) NOT NULL,
    coins INT DEFAULT 20
);

ALTER TABLE usertable ADD COLUMN coins INT DEFAULT 20;


CREATE TABLE IF NOT EXISTS userdatatable (
    username VARCHAR(255) PRIMARY KEY REFERENCES usertable(username),
    name VARCHAR(255) NOT NULL,
    bio VARCHAR(255) NOT NULL,
    image VARCHAR(255) NOT NULL
);