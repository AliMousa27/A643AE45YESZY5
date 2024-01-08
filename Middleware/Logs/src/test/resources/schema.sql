CREATE TABLE log (
    id SERIAL NOT NULL,
    topic VARCHAR(255) NOT NULL,
    payload TEXT NOT NULL,
    date_and_time timestamp NOT NULL,
    PRIMARY KEY (id)
);
