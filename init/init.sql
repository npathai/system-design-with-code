CREATE DATABASE IF NOT EXISTS short_url_generator;

ALTER USER 'root' IDENTIFIED BY 'unsecured';
GRANT ALL PRIVILEGES ON urldb.* TO 'root'@'%';
GRANT ALL PRIVILEGES ON urldb.* TO 'root'@'localhost';

USE short_url_generator;

/*
 MySQL VARCHAR is not case-sensitive, so it cased integrity violation because it considered AAAAa and AAAAA as same
 values. That's why  SET utf8 COLLATE utf8_bin has been added which makes it case insensitive.
 */
CREATE TABLE redirection(
    id VARCHAR(10) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
    long_url VARCHAR(500) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    expiry_at TIMESTAMP NOT NULL,
    PRIMARY KEY(id)
)