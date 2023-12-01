CREATE DATABASE userdb;

\c userdb

CREATE TABLE IF NOT EXISTS usertable (
    username VARCHAR(255) PRIMARY KEY,
    password VARCHAR(255) NOT NULL
);