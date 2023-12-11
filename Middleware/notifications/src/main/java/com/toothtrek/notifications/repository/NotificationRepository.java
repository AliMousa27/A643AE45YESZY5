package com.toothtrek.notifications.repository;

import java.util.List;
import java.sql.Timestamp;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


import com.toothtrek.notifications.entity.Notification;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Integer> {

    @Modifying
    @Query(value = "DELETE FROM notification WHERE booking_id = ?1", nativeQuery = true)
    void deleteByBookingId(String bookingId);

    List<Notification> findByBookingId(String bookingId);

    @Modifying
    @Query(value = "SELECT * FROM notification WHERE time BETWEEN ?1 AND ?2", nativeQuery = true)
    List<Notification> findByTime(Timestamp start, Timestamp end);
}
