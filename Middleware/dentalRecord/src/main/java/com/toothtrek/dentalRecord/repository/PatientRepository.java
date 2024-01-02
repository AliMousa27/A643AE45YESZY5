package com.toothtrek.dentalRecord.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.toothtrek.dentalRecord.entity.Patient;

public interface PatientRepository extends JpaRepository<Patient, String> {

}