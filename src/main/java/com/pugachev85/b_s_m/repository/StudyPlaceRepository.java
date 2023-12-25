package com.pugachev85.b_s_m.repository;

import com.pugachev85.b_s_m.domain.StudyPlace;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the StudyPlace entity.
 */
@SuppressWarnings("unused")
@Repository
public interface StudyPlaceRepository extends JpaRepository<StudyPlace, Long> {}
