package com.toothtrek.notifications.entity;

import java.sql.Timestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "notification")
@Data
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "notification_id_seq")
    @SequenceGenerator(name = "notification_id_seq", sequenceName = "notifcation_id_seq", allocationSize = 1)
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "message")
    private String message;

    @Column(name = "email")
    private String email;

    @Column(name = "time")
    private Timestamp time;

    @Column(name = "booking_id")
    private String bookingId;

    /**
     * Notification constructor
     * 
     * @param title   title of the notification
     * @param message message of the notification
     * @param email   email of the user
     * @param time    time of the notification
     * @param bookingId booking id of the notification
     */
    public Notification(String title, String message, String email, Timestamp time, String bookingId) {
        this.title = title;
        this.message = message;
        this.email = email;
        this.time = time;
        this.bookingId = bookingId;
    }

    public Notification() {
    }
}
