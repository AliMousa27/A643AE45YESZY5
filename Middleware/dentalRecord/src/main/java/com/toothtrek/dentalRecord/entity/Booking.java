package com.toothtrek.dentalRecord.entity;

import org.hibernate.annotations.Type;

import io.hypersistence.utils.hibernate.type.basic.PostgreSQLEnumType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.Data;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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

    @ManyToOne
    @JoinColumn(name = "patient_id", referencedColumnName = "id", nullable = false)
    private Patient patient;

    @ManyToOne
    @JoinColumn(name = "timeslot_id", referencedColumnName = "id", nullable = false)
    private Timeslot timeslot;

    @Enumerated(EnumType.STRING)
    @Column(name = "state", nullable = false)
    @Type(PostgreSQLEnumType.class)
    private State state = State.booked;

    /**
     * Booking constructor
     * 
     * @param patientId  foreign key to patient table, given by Firebase.
     * @param timeslotId foreign key to timeslotId table
     */
    public Booking(Patient patient, Timeslot timeslot) {
        this.patient = patient;
        this.timeslot = timeslot;
    }

    public Booking() {
    }

    public static enum State {
        booked,
        confirmed,
        rejected,
        cancelled,
        completed;
    }
}
