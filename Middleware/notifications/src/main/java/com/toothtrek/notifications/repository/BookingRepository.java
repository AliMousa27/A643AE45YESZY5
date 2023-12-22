package com.toothtrek.notifications.repository;

import java.util.List;
import java.sql.Timestamp;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.toothtrek.notifications.entity.Booking;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    @Query("SELECT b FROM Booking b WHERE b.timeslot.dateAndTime BETWEEN :start AND :end")
    List<Booking> findBookingsByTimePeriod(@Param("start") Timestamp start, @Param("end") Timestamp end);

}