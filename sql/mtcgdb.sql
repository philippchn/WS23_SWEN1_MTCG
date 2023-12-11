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
    name VARCHAR(255),
    damage FLOAT,
    monsterType BOOLEAN NOT NULL,
    elementType VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS t_package (
    packageId SERIAL PRIMARY KEY,
    cardId_1 VARCHAR(255) REFERENCES t_card(cardId),
    cardId_2 VARCHAR(255) REFERENCES t_card(cardId),
    cardId_3 VARCHAR(255) REFERENCES t_card(cardId),
    cardId_4 VARCHAR(255) REFERENCES t_card(cardId),
    cardId_5 VARCHAR(255) REFERENCES t_card(cardId)
);

CREATE TABLE IF NOT EXISTS t_userToPackage (
    username VARCHAR(255) REFERENCES t_user(username),
    packageId INT REFERENCES t_package(packageId)
);