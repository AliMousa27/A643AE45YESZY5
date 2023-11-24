package com.toothtrek.bookings.entity;

import java.sql.Timestamp;

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
    private Timestamp date_of_birth;

    /**
     * Patient constructor
     * 
     * @param id            id of patient, given by Firebase.
     * @param name          name of patient
     * @param date_of_birth date of birth of patient
     */
    public Patient(String id, String name, Timestamp date_of_birth) {
        this.id = id;
        this.name = name;
        this.date_of_birth = date_of_birth;
    }

    public Patient() {
    }
}
