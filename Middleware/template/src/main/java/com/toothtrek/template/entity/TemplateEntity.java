package com.toothtrek.template.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.Data;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Entity
@Table(name = "template") // Name of the table in the database
@Data
public class TemplateEntity {
    // Primary key for the table (the attribute after this annotation)
    @Id
    // Sequence generator for the id.
    // Requires a sequence to be created in the database.
    // Example: "CREATE SEQUENCE template_id_seq;" in postgres.
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "template_id_seq")
    @SequenceGenerator(name = "template_id_seq", sequenceName = "template_id_seq", allocationSize = 1)
    private Long id;

    private String message;
    private int number;

    /**
     * Constructor for the TemplateEntity
     * 
     * @param message A String message
     * @param number  An int number
     */
    public TemplateEntity(String message, int number) {
        this.message = message;
        this.number = number;
    }

    // Empty constructor required for JPA (The ORM)
    public TemplateEntity() {
    }
}
