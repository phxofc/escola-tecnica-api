package com.escolatecnica.api.discipline.repository;

import com.escolatecnica.api.discipline.dto.DTODisciplineScore;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@Service
public class DisciplineCustomRepositoryImp implements DisciplineCustomRepository {

    private final EntityManager entityManager;

    @Override
    public List<DTODisciplineScore> listDisciplinesAndScores(UUID courseId, UUID clazzId, UUID studentId, UUID organizationId) {
        String jpql = """
                SELECT
                    new com.escolatecnica.api.discipline.dto.DTODisciplineScore(d.name, SUM(s.value))
                FROM discipline d
                         JOIN material m ON d.id = m.discipline.id
                         JOIN score s ON m.id = s.material.id
                WHERE d.course.id = :courseId
                  AND m.clazz.id = :clazzId
                  AND s.user.id = :studentId
                  AND d.organizationId = :organizationId
                GROUP BY d.name
                """;

        TypedQuery<DTODisciplineScore> query = entityManager.createQuery(jpql, DTODisciplineScore.class);
        query.setParameter("courseId", courseId);
        query.setParameter("clazzId", clazzId);
        query.setParameter("studentId", studentId);
        query.setParameter("organizationId", organizationId);

        return query.getResultList();
    }
}
