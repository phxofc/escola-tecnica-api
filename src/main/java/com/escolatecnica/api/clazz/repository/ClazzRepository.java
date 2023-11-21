package com.escolatecnica.api.clazz.repository;

import com.escolatecnica.api.clazz.model.Clazz;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ClazzRepository extends JpaRepository<Clazz, UUID> {

    /**
     * Método que retorna um lista de turmas não consolidadas de um curso.
     * @param organizationId
     * @param id
     * @return uma lista de classes
     */
    List<Clazz> findByOrganizationIdAndIsConsolidatedFalseAndCourse_Id(UUID organizationId, UUID id);

    /**
     * Lista de turmas por organização.
     * @param organizationId
     * @return uma lista de classes
     */
    List<Clazz> findByOrganizationId(UUID organizationId);

    /**
     * Retorna uma turma dado id e o id da organização.
     * @param organizationId
     * @param id
     * @return uma instância da classe
     */
    Clazz findByOrganizationIdAndId(UUID organizationId, UUID id);


    /**
     * Retorna um boleano que indica a existência da turma na organização.
     * @param organizationId id da organização onde a turma está cadastrada.
     * @param code código da turma.
     * @param id id do curso
     * @return boleano
     */
    boolean existsByOrganizationIdAndCodeAndCourse_Id(UUID organizationId, String code, UUID id);

}
