package com.toothtrek.template.entity;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.Data;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity // Marks this class as an entity.
@Table(name = "record") // Name of the table in the database.
@Data // Automatically generates getters and setters.
public class RecordEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "dentist_id", nullable = false)
    private Integer dentistId;

    @Column(name = "patient_id", nullable = false)
    private Integer patientId;

    @Column(name = "date_and_time", nullable = false)
    private LocalDateTime dateAndTime;

    @Column(name = "notes")
    private String notes;

    @ManyToOne
    @JoinColumn(name = "dentist_id", insertable = false, updatable = false)
    private Dentist dentist;

    @ManyToOne
    @JoinColumn(name = "patient_id", insertable = false, updatable = false)
    private Patient patient;

    /**
     * Constructor for the TemplateEntity
     * 
     * @param patientId   foreign key to patient table, given by Firebase.
     * @param dentistId   foreign key to dentist table, given by Firebase.
     * @param dateAndTime date and time of the appointment.
     * @param notes       notes for the appointment.
     *
     */
    public RecordEntity(Integer patientId, Integer dentistId, Timestamp dateAndTime, String notes) {
        this.patientId = patientId;
        this.dentistId = dentistId;
        this.dateAndTime = dateAndTime;
        this.notes = notes;
    }

    // Empty constructor required for JPA (The ORM)
    public RecordEntity() {
    }
}
