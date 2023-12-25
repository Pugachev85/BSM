package com.pugachev85.b_s_m.repository;

import com.pugachev85.b_s_m.domain.AcademicSubject;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the AcademicSubject entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AcademicSubjectRepository extends JpaRepository<AcademicSubject, Long> {}
