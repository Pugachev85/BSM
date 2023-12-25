package com.pugachev85.b_s_m.repository;

import com.pugachev85.b_s_m.domain.PersonalGrade;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the PersonalGrade entity.
 *
 * When extending this class, extend PersonalGradeRepositoryWithBagRelationships too.
 * For more information refer to https://github.com/jhipster/generator-jhipster/issues/17990.
 */
@Repository
public interface PersonalGradeRepository extends PersonalGradeRepositoryWithBagRelationships, JpaRepository<PersonalGrade, Long> {
    default Optional<PersonalGrade> findOneWithEagerRelationships(Long id) {
        return this.fetchBagRelationships(this.findOneWithToOneRelationships(id));
    }

    default List<PersonalGrade> findAllWithEagerRelationships() {
        return this.fetchBagRelationships(this.findAllWithToOneRelationships());
    }

    default Page<PersonalGrade> findAllWithEagerRelationships(Pageable pageable) {
        return this.fetchBagRelationships(this.findAllWithToOneRelationships(pageable));
    }

    @Query(
        value = "select personalGrade from PersonalGrade personalGrade left join fetch personalGrade.academicSubject",
        countQuery = "select count(personalGrade) from PersonalGrade personalGrade"
    )
    Page<PersonalGrade> findAllWithToOneRelationships(Pageable pageable);

    @Query("select personalGrade from PersonalGrade personalGrade left join fetch personalGrade.academicSubject")
    List<PersonalGrade> findAllWithToOneRelationships();

    @Query(
        "select personalGrade from PersonalGrade personalGrade left join fetch personalGrade.academicSubject where personalGrade.id =:id"
    )
    Optional<PersonalGrade> findOneWithToOneRelationships(@Param("id") Long id);
}
