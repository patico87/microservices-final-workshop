CREATE TABLE users
(
    id       SERIAL PRIMARY KEY,
    username VARCHAR(100) NOT NULL UNIQUE,
    password TEXT         NOT NULL,
    role     VARCHAR(50)  NOT NULL
);