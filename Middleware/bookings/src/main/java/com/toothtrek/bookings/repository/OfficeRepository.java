package com.toothtrek.bookings.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.toothtrek.bookings.entity.Office;

public interface OfficeRepository extends JpaRepository<Office, Long> {

}