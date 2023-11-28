package com.toothtrek.bookings.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.toothtrek.bookings.entity.Booking;
import com.toothtrek.bookings.entity.Patient;

public interface BookingRepository extends JpaRepository<Booking, Integer> {
    List<Booking> findByTimeslotId(Integer timeslotId);
    List<Patient> findByPatientId(String patientId);
}