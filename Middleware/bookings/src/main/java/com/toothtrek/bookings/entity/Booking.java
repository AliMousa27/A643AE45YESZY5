package com.toothtrek.bookings.entity;

import jakarta.persistence.Column;
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

    @Column(name = "patient_id")
    private String patientId;

    @Column(name = "timeslot_id")
    private long timeslotId;

    /**
     * Booking constructor
     * 
     * @param patientId  foreign key to patient table, given by Firebase.
     * @param timeslotId foreign key to timeslotId table
     */
    public Booking(String patientId, long timeslotId) {
        this.patientId = patientId;
        this.timeslotId = timeslotId;
    }

    public Booking() {
    }
}
