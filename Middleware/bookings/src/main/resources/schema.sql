CREATE TABLE patient (
    id varchar(255) NOT NULL,
    Name varchar(255) NOT NULL,
    date_of_birth date,
    PRIMARY KEY (id)
);
CREATE TABLE office (
    id SERIAL NOT NULL,
    name varchar(255) NOT NULL,
    address varchar(255) NOT NULL,
    longitude float NOT NULL,
    latitude float NOT NULL,
    PRIMARY KEY (id)
);
CREATE TABLE dentist (
    id SERIAL NOT NULL,
    name varchar(255) NOT NULL,
    date_of_birth date,
    PRIMARY KEY (id)
);
CREATE TABLE timeslot (
    id SERIAL NOT NULL,
    date_and_time timestamp NOT NULL,
    office_id bigint NOT NULL,
    dentist_id bigint NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_office FOREIGN KEY(office_id) REFERENCES office(id),
    CONSTRAINT fk_dentist FOREIGN KEY(dentist_id) REFERENCES dentist(id)
);
CREATE TABLE booking (
    id SERIAL NOT NULL,
    patient_id varchar(255) NOT NULL,
    timeslot_id bigint NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_patient FOREIGN KEY(patient_id) REFERENCES patient(id),
    CONSTRAINT fk_time_slot FOREIGN KEY(timeslot_id) REFERENCES timeslot(id)
);