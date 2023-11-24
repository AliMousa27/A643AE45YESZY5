package com.toothtrek.bookings.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.Data;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Entity
@Table(name = "booking")
@Data
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "booking_id_seq")
    @SequenceGenerator(name = "booking_id_seq", sequenceName = "booking_id_seq", allocationSize = 1)
    private Long id;
    private String patient_id;
    private long time_slot_id;

    /**
     * Booking constructor
     * 
     * @param patient_id   foreign key to patient table, given by Firebase.
     * @param time_slot_id foreign key to time_slot table
     */
    public Booking(String patient_id, long time_slot_id) {
        this.patient_id = patient_id;
        this.time_slot_id = time_slot_id;
    }

    public Booking() {
    }
}
