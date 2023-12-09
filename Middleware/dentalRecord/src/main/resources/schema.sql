CREATE TABLE record (
    id SERIAL NOT NULL,
    dentist_name VARCHAR(255) NOT NULL,
    patient_id INTEGER NOT NULL,
    date_and_time TIMESTAMP NOT NULL,
    notes VARCHAR(255),
    PRIMARY KEY (id),
    CONSTRAINT fk_dentist FOREIGN KEY(dentist_name) REFERENCES dentist(id)
    CONSTRAINT fk_patient FOREIGN KEY(patient_id) REFERENCES patient(id)
);

-- This file is not run nor used by the application.  It is only here as a convenience to create the database schema manually.