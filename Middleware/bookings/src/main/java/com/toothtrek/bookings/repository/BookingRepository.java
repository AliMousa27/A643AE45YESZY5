package com.toothtrek.bookings.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.toothtrek.bookings.entity.Booking;

public interface BookingRepository extends JpaRepository<Booking, Integer> {

}