package com.toothtrek.dentalRecord.entity;

import lombok.Data;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity // Marks this class as an entity.
@Table(name = "record") // Name of the table in the database.
@Data // Automatically generates getters and setters.
public class Record {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "patient_id", referencedColumnName = "id", nullable = false)
    private Patient patient;

    @ManyToOne
    @JoinColumn(name = "timeslot_id", referencedColumnName = "id", nullable = false)
    private Timeslot timeslot;

    @Column(name = "notes")
    private String notes;

    /**
     * @param patientId   foreign key to patient table, given by Firebase.
     * @param dentistId   foreign key to dentist table.
     * @param dateAndTime date and time of the appointment.
     * @param notes       notes for the appointment.
     */
    public Record(Patient patient, Timeslot timeslot, String notes) {
        this.patient = patient;
        this.timeslot = timeslot;
        this.notes = notes;
    }

    // Empty constructor required for JPA (The ORM)
    public Record() {
    }
}
