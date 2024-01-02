package com.toothtrek.Logs.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.toothtrek.Logs.entity.Log;

public interface LogRepository extends JpaRepository<Log, Long> {
}