package com.toothtrek.dentalRecord.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.toothtrek.dentalRecord.entity.Record;
import com.toothtrek.dentalRecord.entity.Timeslot;

public interface RecordRepository extends JpaRepository<Record, Long> {
    Record findByTimeslot(Timeslot timeslot);

    @Query("SELECT r FROM Record r WHERE r.timeslot.id = :timeslotId")
    Record findByTimeslotId(Long timeslotId);

    @Query("SELECT r FROM Record r WHERE r.patient.id = :patientId")
    List<Record> findByPatientId(String patientId);
}   