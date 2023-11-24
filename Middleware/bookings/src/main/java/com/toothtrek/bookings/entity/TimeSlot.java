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
@Table(name = "time_slot")
@Data
public class TimeSlot {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "time_slot_id_seq")
    @SequenceGenerator(name = "time_slot_id_seq", sequenceName = "time_slot_id_seq", allocationSize = 1)
    private Long id;
    private Long office_id;
    private Long dentist_id;
    private Timestamp date_and_time;

    /**
     * TimeSlot constructor
     * 
     * @param office_id     foreign key to office table
     * @param dentist_id    foreign key to dentist table
     * @param date_and_time date and time of time slot
     */
    public TimeSlot(Long office_id, Long dentist_id, Timestamp date_and_time) {
        this.office_id = office_id;
        this.dentist_id = dentist_id;
        this.date_and_time = date_and_time;
    }

    public TimeSlot() {
    }
}
