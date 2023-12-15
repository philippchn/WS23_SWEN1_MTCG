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

CREATE TABLE IF NOT EXISTS t_card(
    cardId VARCHAR(255) PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    damage FLOAT NOT NULL,
    monsterType BOOLEAN NOT NULL,
    elementType VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS t_package (
    packageId SERIAL PRIMARY KEY,
    cardId_1 VARCHAR(255) REFERENCES t_card(cardId) NOT NULL,
    cardId_2 VARCHAR(255) REFERENCES t_card(cardId) NOT NULL,
    cardId_3 VARCHAR(255) REFERENCES t_card(cardId) NOT NULL,
    cardId_4 VARCHAR(255) REFERENCES t_card(cardId) NOT NULL,
    cardId_5 VARCHAR(255) REFERENCES t_card(cardId) NOT NULL,
    available BOOLEAN DEFAULT true
);

CREATE TABLE IF NOT EXISTS t_userToPackage (
    username VARCHAR(255) REFERENCES t_user(username) NOT NULL,
    packageId INT REFERENCES t_package(packageId) NOT NULL
);