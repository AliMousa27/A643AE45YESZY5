package com.toothtrek.notifications.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.toothtrek.notifications.entity.Timeslot;

public interface TimeslotRepository extends JpaRepository<Timeslot, Long> {

}