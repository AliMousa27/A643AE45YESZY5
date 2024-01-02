package com.toothtrek.bookings.entity;

import java.sql.Timestamp;

import org.hibernate.annotations.Type;

import com.google.gson.annotations.SerializedName;

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
@Table(name = "timeslot")
@Data
public class Timeslot {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "timeslot_id_seq")
    @SequenceGenerator(name = "timeslot_id_seq", sequenceName = "timeslot_id_seq", allocationSize = 1)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "office_id", referencedColumnName = "id", nullable = false)
    private Office office;

    @ManyToOne
    @JoinColumn(name = "dentist_id", referencedColumnName = "id", nullable = false)
    private Dentist dentist;

    @Column(name = "date_and_time")
    @SerializedName("date_and_time") // Frontend requires this name.
    private Timestamp dateAndTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "state", nullable = false)
    @Type(PostgreSQLEnumType.class)
    private State state = State.open;

    /**
     * Timeslot constructor
     * 
     * @param office      office of time slot
     * @param dentist     dentist of time slot
     * @param dateAndTime date and time of time slot
     */
    public Timeslot(Office office, Dentist dentist, Timestamp dateAndTime) {
        this.office = office;
        this.dentist = dentist;
        this.dateAndTime = dateAndTime;
    }

    public Timeslot() {
    }

    public static enum State {
        open,
        cancelled;
    }
}
