package com.escolatecnica.api.answer.repository;

import com.escolatecnica.api.answer.model.Answer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface AnswerRepository extends JpaRepository<Answer, UUID> {

    /**
     * Métedo cuja finalidade é checar se existe uma resposta do aluno associado a tal material.
     *
     * @param userId id do aluno
     * @param materialId id do material
     * @param organizationId id da organização
     * @return boleano
     */
    boolean existsByUser_IdAndMaterial_IdAndOrganizationId(UUID userId, UUID materialId, UUID organizationId);

    @Query("select a from answer a where a.material.id = ?1 and a.organizationId = ?2")
    List<Answer> findByMaterial_IdAndOrganizationId(UUID id, UUID organizationId);

}
