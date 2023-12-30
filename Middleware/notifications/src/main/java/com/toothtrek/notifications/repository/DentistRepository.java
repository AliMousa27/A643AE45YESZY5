package com.toothtrek.notifications.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.toothtrek.notifications.entity.Dentist;

public interface DentistRepository extends JpaRepository<Dentist, Long> {

}