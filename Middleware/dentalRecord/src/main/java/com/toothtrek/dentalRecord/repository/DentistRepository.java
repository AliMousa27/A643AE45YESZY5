package com.toothtrek.dentalRecord.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.toothtrek.dentalRecord.entity.Dentist;

public interface DentistRepository extends JpaRepository<Dentist, Long> {

}