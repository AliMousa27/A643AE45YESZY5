CREATE TABLE notification (
    id SERIAL NOT NULL,
    title varchar(255) NOT NULL,
    message varchar(255) NOT NULL,
    time timestamp,
    email varchar(255) NOT NULL,
    booking_id varchar(255) NOT NULL,
    PRIMARY KEY (id)
);

-- This file is not run nor used by the application.  It is only here as a convenience to create the database schema manually.