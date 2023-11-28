package com.toothtrek.bookings.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.toothtrek.bookings.entity.Timeslot;

public interface TimeslotRepository extends JpaRepository<Timeslot, Integer> {

}