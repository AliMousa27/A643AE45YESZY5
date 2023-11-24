package com.toothtrek.bookings.entity;

import java.sql.Timestamp;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.Data;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Entity
@Table(name = "dentist")
@Data
public class Dentist {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "dentist_id_seq")
    @SequenceGenerator(name = "dentist_id_seq", sequenceName = "dentist_id_seq", allocationSize = 1)
    private Long id;
    private String name;
    private Timestamp date_of_birth;

    /**
     * Dentist constructor
     * 
     * @param name          name of dentist
     * @param date_of_birth date of birth of dentist
     */
    public Dentist(String name, Timestamp date_of_birth) {
        this.name = name;
        this.date_of_birth = date_of_birth;
    }

    public Dentist() {
    }
}
