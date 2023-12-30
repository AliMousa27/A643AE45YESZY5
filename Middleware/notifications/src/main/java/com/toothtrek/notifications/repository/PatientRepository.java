package com.toothtrek.notifications.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.toothtrek.notifications.entity.Patient;

public interface PatientRepository extends JpaRepository<Patient, String> {

}