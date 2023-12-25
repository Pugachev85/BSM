package com.pugachev85.b_s_m.repository;

import com.pugachev85.b_s_m.domain.PersonalGrade;
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
public class PersonalGradeRepositoryWithBagRelationshipsImpl implements PersonalGradeRepositoryWithBagRelationships {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<PersonalGrade> fetchBagRelationships(Optional<PersonalGrade> personalGrade) {
        return personalGrade.map(this::fetchStudents);
    }

    @Override
    public Page<PersonalGrade> fetchBagRelationships(Page<PersonalGrade> personalGrades) {
        return new PageImpl<>(
            fetchBagRelationships(personalGrades.getContent()),
            personalGrades.getPageable(),
            personalGrades.getTotalElements()
        );
    }

    @Override
    public List<PersonalGrade> fetchBagRelationships(List<PersonalGrade> personalGrades) {
        return Optional.of(personalGrades).map(this::fetchStudents).orElse(Collections.emptyList());
    }

    PersonalGrade fetchStudents(PersonalGrade result) {
        return entityManager
            .createQuery(
                "select personalGrade from PersonalGrade personalGrade left join fetch personalGrade.students where personalGrade.id = :id",
                PersonalGrade.class
            )
            .setParameter("id", result.getId())
            .getSingleResult();
    }

    List<PersonalGrade> fetchStudents(List<PersonalGrade> personalGrades) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, personalGrades.size()).forEach(index -> order.put(personalGrades.get(index).getId(), index));
        List<PersonalGrade> result = entityManager
            .createQuery(
                "select personalGrade from PersonalGrade personalGrade left join fetch personalGrade.students where personalGrade in :personalGrades",
                PersonalGrade.class
            )
            .setParameter("personalGrades", personalGrades)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
