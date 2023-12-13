package com.toothtrek.dentalRecord.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.toothtrek.dentalRecord.entity.Timeslot;

public interface TimeslotRepository extends JpaRepository<Timeslot, Long> {

}