
USE short_url_generator;

/*
 MySQL VARCHAR is not case-sensitive, so it cased integrity violation because it considered AAAAa and AAAAA as same
 values. That's why  SET utf8 COLLATE utf8_bin has been added which makes it case insensitive.
 */
CREATE TABLE redirection(
    id VARCHAR(10) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
    long_url VARCHAR(500) NOT NULL,
    created_at TIMESTAMP(3) NOT NULL,
    expiry_at TIMESTAMP(3) NOT NULL,
    uid VARCHAR(50),
    PRIMARY KEY(id)
);