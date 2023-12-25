package com.pugachev85.b_s_m.repository;

import com.pugachev85.b_s_m.domain.EducationalProgram;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the EducationalProgram entity.
 *
 * When extending this class, extend EducationalProgramRepositoryWithBagRelationships too.
 * For more information refer to https://github.com/jhipster/generator-jhipster/issues/17990.
 */
@Repository
public interface EducationalProgramRepository
    extends EducationalProgramRepositoryWithBagRelationships, JpaRepository<EducationalProgram, Long> {
    default Optional<EducationalProgram> findOneWithEagerRelationships(Long id) {
        return this.fetchBagRelationships(this.findOneWithToOneRelationships(id));
    }

    default List<EducationalProgram> findAllWithEagerRelationships() {
        return this.fetchBagRelationships(this.findAllWithToOneRelationships());
    }

    default Page<EducationalProgram> findAllWithEagerRelationships(Pageable pageable) {
        return this.fetchBagRelationships(this.findAllWithToOneRelationships(pageable));
    }

    @Query(
        value = "select educationalProgram from EducationalProgram educationalProgram left join fetch educationalProgram.academicYear",
        countQuery = "select count(educationalProgram) from EducationalProgram educationalProgram"
    )
    Page<EducationalProgram> findAllWithToOneRelationships(Pageable pageable);

    @Query("select educationalProgram from EducationalProgram educationalProgram left join fetch educationalProgram.academicYear")
    List<EducationalProgram> findAllWithToOneRelationships();

    @Query(
        "select educationalProgram from EducationalProgram educationalProgram left join fetch educationalProgram.academicYear where educationalProgram.id =:id"
    )
    Optional<EducationalProgram> findOneWithToOneRelationships(@Param("id") Long id);
}
