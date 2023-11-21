package com.escolatecnica.api.discipline.repository;

import com.escolatecnica.api.discipline.model.Discipline;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface DisciplineRepository extends JpaRepository<Discipline, UUID> {

    Discipline readByIdAndOrganizationId(UUID id, UUID organizationId);

    List<Discipline> readAllByOrganizationId(UUID organizationId);

    List<Discipline> findByOrganizationIdAndCourse_Id(UUID organizationId, UUID id);

}

