package com.pugachev85.b_s_m.repository;

import com.pugachev85.b_s_m.domain.EducationalProgram;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface EducationalProgramRepositoryWithBagRelationships {
    Optional<EducationalProgram> fetchBagRelationships(Optional<EducationalProgram> educationalProgram);

    List<EducationalProgram> fetchBagRelationships(List<EducationalProgram> educationalPrograms);

    Page<EducationalProgram> fetchBagRelationships(Page<EducationalProgram> educationalPrograms);
}
