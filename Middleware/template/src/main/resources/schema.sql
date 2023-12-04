CREATE TABLE template (
    id SERIAL NOT NULL,
    message varchar(255) NOT NULL,
    number INTEGER NOT NULL,
    PRIMARY KEY (id)
);

-- This file is not run nor used by the application.  It is only here as a convenience to create the database schema manually.