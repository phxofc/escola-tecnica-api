package com.escolatecnica.api.score.repository;

import com.escolatecnica.api.score.model.Score;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ScoreRepository extends JpaRepository<Score, UUID> {

    boolean existsByMaterialIdAndUserIdAndOrganizationId(UUID materialId, UUID userId, UUID organizationId);

    Optional<Score> findByMaterialIdAndUserIdAndOrganizationId(UUID materialId, UUID userId, UUID organizationId);

}
