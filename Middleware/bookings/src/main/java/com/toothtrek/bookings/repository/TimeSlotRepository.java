package com.toothtrek.bookings.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.toothtrek.bookings.entity.TimeSlot;

public interface TimeSlotRepository extends JpaRepository<TimeSlot, Integer> {

}