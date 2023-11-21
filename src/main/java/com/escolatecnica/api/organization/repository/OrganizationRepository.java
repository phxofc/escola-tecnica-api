package com.escolatecnica.api.organization.repository;

import com.escolatecnica.api.organization.model.Organization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface OrganizationRepository extends JpaRepository<Organization, UUID> {
    boolean existsByName(String name);

    List<Organization> findByIsRootFalse();

}
