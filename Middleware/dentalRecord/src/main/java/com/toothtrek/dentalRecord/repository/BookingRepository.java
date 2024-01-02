package com.toothtrek.dentalRecord.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.toothtrek.dentalRecord.entity.Booking;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    /**
     * Find a completed booking by timeslot id (When booking.state = 'completed').
     * There can only be one completed booking for a timeslot.
     * 
     * @param timeslotId The timeslot id
     * @return The booking (or null if not found)
     */
    @Query("SELECT b FROM Booking b WHERE b.timeslot.id = :timeslotId AND b.state = 'completed'")
    Booking findCompletedBookingByTimeslotId(Long timeslotId);
}
