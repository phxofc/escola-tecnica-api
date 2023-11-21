package com.escolatecnica.api.material.repository;

import com.escolatecnica.api.material.model.Material;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface MaterialRepository extends JpaRepository<Material, UUID> {
    List<Material> findByOrganizationIdAndRegisteredBy(UUID organizationId, UUID registeredBy);
    Material readByIdAndOrganizationId(UUID id, UUID organizationId);
    List<Material> findByOrganizationIdAndCourse_IdAndClazz_IdAndDiscipline_Id(UUID organizationId, UUID courseId, UUID clazzId, UUID disciplineId);
}
