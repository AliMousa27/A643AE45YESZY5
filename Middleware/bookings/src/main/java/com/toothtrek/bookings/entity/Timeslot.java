package com.toothtrek.bookings.entity;

import java.sql.Timestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.Data;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Entity
@Table(name = "timeslot")
@Data
public class Timeslot {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "timeslot_id_seq")
    @SequenceGenerator(name = "timeslot_id_seq", sequenceName = "timeslot_id_seq", allocationSize = 1)
    private Long id;

    @Column(name = "office_id")
    private Long officeId;

    @Column(name = "dentist_id")
    private Long dentistId;

    @Column(name = "date_and_time")
    private Timestamp dateAndTime;

    /**
     * Timeslot constructor
     * 
     * @param officeId    foreign key to office table
     * @param dentistId   foreign key to dentist table
     * @param dateAndTime date and time of time slot
     */
    public Timeslot(Long officeId, Long dentistId, Timestamp dateAndTime) {
        this.officeId = officeId;
        this.dentistId = dentistId;
        this.dateAndTime = dateAndTime;
    }

    public Timeslot() {
    }
}
