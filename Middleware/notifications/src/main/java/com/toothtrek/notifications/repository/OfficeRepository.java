package com.toothtrek.notifications.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.toothtrek.notifications.entity.Office;

public interface OfficeRepository extends JpaRepository<Office, Long> {

}