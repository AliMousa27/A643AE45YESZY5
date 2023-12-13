package com.toothtrek.dentalRecord.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.toothtrek.dentalRecord.entity.Office;

public interface OfficeRepository extends JpaRepository<Office, Long> {

}