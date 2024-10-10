CREATE SCHEMA IF NOT EXISTS postgres;

CREATE TABLE IF NOT EXISTS postgres.stucture
(
    id
    SERIAL
    PRIMARY
    KEY,
    name
    VARCHAR
(
    255
) NOT NULL,
    form VARCHAR
(
    255
),
    symbol VARCHAR
(
    255
)
    );

CREATE TABLE IF NOT EXISTS postgres.truck
(
    id
    SERIAL
    PRIMARY
    KEY,
    height
    INT
    NOT
    NULL,
    width
    INT
    NOT
    NULL
);

CREATE TABLE IF NOT EXISTS postgres.package
(
    id
    SERIAL
    PRIMARY
    KEY,
    height
    INT
    NOT
    NULL,
    widthTop
    INT
    NOT
    NULL,
    widthBottom
    INT
    NOT
    NULL,
    pack
    TEXT,
    truck_id
    INT,
    CONSTRAINT
    fk_truck
    FOREIGN
    KEY
(
    truck_id
)
    REFERENCES truck
(
    id
)
    );