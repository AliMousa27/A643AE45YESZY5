package com.toothtrek.template.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.toothtrek.template.entity.TemplateEntity;

// A repository acts as an interface that provides methods for performing CRUD operations on entities.
// The JpaRepository<x, y> requires the entity class and the type of the primary key.
public interface TemplateEntityRepository extends JpaRepository<TemplateEntity, Integer> {

}