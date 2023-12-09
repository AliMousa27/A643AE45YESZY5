package com.toothtrek.template.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.toothtrek.template.entity.RecordEntity;

// A repository acts as an interface that provides methods for performing CRUD operations on entities.
// The JpaRepository<x, y> requires the entity class and the type of the primary key.
// @see TemplateEntity
public interface TemplateEntityRepository extends JpaRepository<RecordEntity, Integer> {

}