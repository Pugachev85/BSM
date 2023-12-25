package com.pugachev85.b_s_m.repository;

import com.pugachev85.b_s_m.domain.Student;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Student entity.
 *
 * When extending this class, extend StudentRepositoryWithBagRelationships too.
 * For more information refer to https://github.com/jhipster/generator-jhipster/issues/17990.
 */
@Repository
public interface StudentRepository extends StudentRepositoryWithBagRelationships, JpaRepository<Student, Long> {
    default Optional<Student> findOneWithEagerRelationships(Long id) {
        return this.fetchBagRelationships(this.findOneWithToOneRelationships(id));
    }

    default List<Student> findAllWithEagerRelationships() {
        return this.fetchBagRelationships(this.findAllWithToOneRelationships());
    }

    default Page<Student> findAllWithEagerRelationships(Pageable pageable) {
        return this.fetchBagRelationships(this.findAllWithToOneRelationships(pageable));
    }

    @Query(
        value = "select student from Student student left join fetch student.group",
        countQuery = "select count(student) from Student student"
    )
    Page<Student> findAllWithToOneRelationships(Pageable pageable);

    @Query("select student from Student student left join fetch student.group")
    List<Student> findAllWithToOneRelationships();

    @Query("select student from Student student left join fetch student.group where student.id =:id")
    Optional<Student> findOneWithToOneRelationships(@Param("id") Long id);
}
