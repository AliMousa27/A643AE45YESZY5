package com.toothtrek.notifications.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


import com.toothtrek.notifications.entity.Notification;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Integer> {

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM notification WHERE booking_id = ?1", nativeQuery = true)
    void deleteByBookingId(String bookingId);

    List<Notification> findByBookingId(String bookingId);
}
