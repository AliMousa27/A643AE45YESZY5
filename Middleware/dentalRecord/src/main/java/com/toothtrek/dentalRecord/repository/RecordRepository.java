package com.toothtrek.dentalRecord.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.toothtrek.dentalRecord.entity.Record;
import com.toothtrek.dentalRecord.entity.Timeslot;

public interface RecordRepository extends JpaRepository<Record, Long> {
    Record findByTimeslot(Timeslot timeslot);
}