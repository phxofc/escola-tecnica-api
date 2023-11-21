package com.escolatecnica.api.documentation.repository;

import com.escolatecnica.api.documentation.model.Documentation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface DocumentationRepository extends JpaRepository<Documentation, UUID> {
}
