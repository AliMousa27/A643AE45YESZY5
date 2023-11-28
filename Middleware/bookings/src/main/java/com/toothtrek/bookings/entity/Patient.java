package com.toothtrek.bookings.entity;

import java.sql.Timestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Data;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "patient")
@Data
public class Patient {
    @Id
    private String id;
    private String name;

    @Column(name = "date_of_birth")
    private Timestamp dateOfBirth;

    /**
     * Patient constructor
     * 
     * @param id            id of patient, given by Firebase.
     * @param name          name of patient
     * @param dateOfBirth date of birth of patient
     */
    public Patient(String id, String name, Timestamp dateOfBirth) {
        this.id = id;
        this.name = name;
        this.dateOfBirth = dateOfBirth;
    }

    public Patient() {
    }
}
