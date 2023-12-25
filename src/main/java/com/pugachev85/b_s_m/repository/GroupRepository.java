package com.pugachev85.b_s_m.repository;

import com.pugachev85.b_s_m.domain.Group;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Group entity.
 */
@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {
    default Optional<Group> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Group> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Group> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select jhiGroup from Group jhiGroup left join fetch jhiGroup.studyPlace left join fetch jhiGroup.educationalProgram",
        countQuery = "select count(jhiGroup) from Group jhiGroup"
    )
    Page<Group> findAllWithToOneRelationships(Pageable pageable);

    @Query("select jhiGroup from Group jhiGroup left join fetch jhiGroup.studyPlace left join fetch jhiGroup.educationalProgram")
    List<Group> findAllWithToOneRelationships();

    @Query(
        "select jhiGroup from Group jhiGroup left join fetch jhiGroup.studyPlace left join fetch jhiGroup.educationalProgram where jhiGroup.id =:id"
    )
    Optional<Group> findOneWithToOneRelationships(@Param("id") Long id);
}
