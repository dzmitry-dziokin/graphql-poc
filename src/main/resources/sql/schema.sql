CREATE TABLE authors
(
    id            INTEGER     NOT NULL AUTO_INCREMENT,
    first_name    VARCHAR(64) NOT NULL,
    last_name     VARCHAR(64) NOT NULL,
    date_of_birth TIMESTAMP   NOT NULL DEFAULT now(),
    PRIMARY KEY (id)
);
