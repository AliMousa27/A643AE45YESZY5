package com.toothtrek.bookings.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.toothtrek.bookings.entity.Dentist;

public interface DentistRepository extends JpaRepository<Dentist, Long> {

}