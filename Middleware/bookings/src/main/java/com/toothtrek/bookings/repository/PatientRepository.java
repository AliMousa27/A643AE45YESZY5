package com.toothtrek.bookings.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.toothtrek.bookings.entity.Patient;

public interface PatientRepository extends JpaRepository<Patient, Integer> {

}