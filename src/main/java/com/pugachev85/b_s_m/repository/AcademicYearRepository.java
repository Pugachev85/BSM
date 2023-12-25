package com.pugachev85.b_s_m.repository;

import com.pugachev85.b_s_m.domain.AcademicYear;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the AcademicYear entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AcademicYearRepository extends JpaRepository<AcademicYear, Long> {}
