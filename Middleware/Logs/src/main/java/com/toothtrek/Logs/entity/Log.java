package com.toothtrek.Logs.entity;

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
@Table(name = "log")
@Data
public class Log {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "log_id_seq")
    @SequenceGenerator(name = "log_id_seq", sequenceName = "log_id_seq", allocationSize = 1)
    private Long id;

    private String topic;

    private String payload;

    @Column(name = "date_and_time")
    private Timestamp dateAndTime;

    /**
     * Constructor for the Log entity
     * 
     * @param topic       The topic of the message
     * @param payload     The payload of the message as a String
     */
    public Log(String topic, String payload) {
        this.topic = topic;
        this.payload = payload;
        this.dateAndTime = new Timestamp(System.currentTimeMillis());
    }

    // Empty constructor
    public Log() {
    }
}
