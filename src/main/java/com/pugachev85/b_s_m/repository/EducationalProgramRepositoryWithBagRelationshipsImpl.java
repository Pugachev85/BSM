package com.pugachev85.b_s_m.repository;

import com.pugachev85.b_s_m.domain.EducationalProgram;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

/**
 * Utility repository to load bag relationships based on https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
public class EducationalProgramRepositoryWithBagRelationshipsImpl implements EducationalProgramRepositoryWithBagRelationships {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<EducationalProgram> fetchBagRelationships(Optional<EducationalProgram> educationalProgram) {
        return educationalProgram.map(this::fetchAcademicSubjects);
    }

    @Override
    public Page<EducationalProgram> fetchBagRelationships(Page<EducationalProgram> educationalPrograms) {
        return new PageImpl<>(
            fetchBagRelationships(educationalPrograms.getContent()),
            educationalPrograms.getPageable(),
            educationalPrograms.getTotalElements()
        );
    }

    @Override
    public List<EducationalProgram> fetchBagRelationships(List<EducationalProgram> educationalPrograms) {
        return Optional.of(educationalPrograms).map(this::fetchAcademicSubjects).orElse(Collections.emptyList());
    }

    EducationalProgram fetchAcademicSubjects(EducationalProgram result) {
        return entityManager
            .createQuery(
                "select educationalProgram from EducationalProgram educationalProgram left join fetch educationalProgram.academicSubjects where educationalProgram.id = :id",
                EducationalProgram.class
            )
            .setParameter("id", result.getId())
            .getSingleResult();
    }

    List<EducationalProgram> fetchAcademicSubjects(List<EducationalProgram> educationalPrograms) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, educationalPrograms.size()).forEach(index -> order.put(educationalPrograms.get(index).getId(), index));
        List<EducationalProgram> result = entityManager
            .createQuery(
                "select educationalProgram from EducationalProgram educationalProgram left join fetch educationalProgram.academicSubjects where educationalProgram in :educationalPrograms",
                EducationalProgram.class
            )
            .setParameter("educationalPrograms", educationalPrograms)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
