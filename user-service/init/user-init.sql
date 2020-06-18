CREATE DATABASE IF NOT EXISTS user_db;

ALTER USER 'root' IDENTIFIED BY 'unsecured';
GRANT ALL PRIVILEGES ON urldb.* TO 'root'@'%';
GRANT ALL PRIVILEGES ON urldb.* TO 'root'@'localhost';

USE user_db;

/*
 MySQL VARCHAR is not case-sensitive.
 */
CREATE TABLE users (
id binary(16) PRIMARY KEY,
username VARCHAR(100) NOT NULL UNIQUE,
password VARCHAR(100) NOT NULL,
email VARCHAR(100)
);

INSERT INTO users VALUES(UUID_TO_BIN(UUID(), true), 'root', 'root', NULL);


COMMIT;