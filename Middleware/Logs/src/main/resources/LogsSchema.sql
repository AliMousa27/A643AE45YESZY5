CREATE TABLE logs (
    id bigint NOT NULL,
    patient TEXT,
    dentist TEXT,
    sql_statement TEXT,
    timestamp TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    message TEXT NOT NULL,
    topic TEXT,
    PRIMARY KEY (id)
);
