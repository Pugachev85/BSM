package com.pugachev85.b_s_m.repository;

import com.pugachev85.b_s_m.domain.PersonalGrade;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface PersonalGradeRepositoryWithBagRelationships {
    Optional<PersonalGrade> fetchBagRelationships(Optional<PersonalGrade> personalGrade);

    List<PersonalGrade> fetchBagRelationships(List<PersonalGrade> personalGrades);

    Page<PersonalGrade> fetchBagRelationships(Page<PersonalGrade> personalGrades);
}
