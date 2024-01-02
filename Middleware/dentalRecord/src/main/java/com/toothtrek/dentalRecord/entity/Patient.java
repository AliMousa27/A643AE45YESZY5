package com.toothtrek.dentalRecord.entity;

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

    private boolean notified;

    private String email;

    /**
     * Patient constructor
     * 
     * @param id    id of patient, given by Firebase.
     * @param name  name of patient
     * @param email email of patient
     */
    public Patient(String id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.notified = true;
    }

    public Patient() {
    }
}
