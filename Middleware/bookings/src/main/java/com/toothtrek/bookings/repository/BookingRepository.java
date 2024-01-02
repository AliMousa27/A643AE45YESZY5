package com.toothtrek.bookings.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.toothtrek.bookings.entity.Booking;
import com.toothtrek.bookings.entity.Patient;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByTimeslotId(Long timeslotId);

    List<Booking> findByPatient(Patient patient);
}