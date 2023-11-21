package com.middleware.Logs;

import java.sql.Timestamp;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.Data;
import jakarta.persistence.Id;

@Entity
// mark as a data class
@Data
public class Logs {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String email;
    private String sql_statement;
    private Timestamp timestamp;
    private String message;
    private String topic;

    public Logs(String email, String sql_statement, java.sql.Timestamp timestamps,
            String message, String topic) {
        this.email = email;
        this.sql_statement = sql_statement;
        this.timestamp = timestamps;
        this.message = message;
        this.topic = topic;
    }
}
