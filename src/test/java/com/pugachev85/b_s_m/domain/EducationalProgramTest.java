package com.pugachev85.b_s_m.domain;

import static com.pugachev85.b_s_m.domain.AcademicSubjectTestSamples.*;
import static com.pugachev85.b_s_m.domain.AcademicYearTestSamples.*;
import static com.pugachev85.b_s_m.domain.EducationalProgramTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.pugachev85.b_s_m.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class EducationalProgramTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(EducationalProgram.class);
        EducationalProgram educationalProgram1 = getEducationalProgramSample1();
        EducationalProgram educationalProgram2 = new EducationalProgram();
        assertThat(educationalProgram1).isNotEqualTo(educationalProgram2);

        educationalProgram2.setId(educationalProgram1.getId());
        assertThat(educationalProgram1).isEqualTo(educationalProgram2);

        educationalProgram2 = getEducationalProgramSample2();
        assertThat(educationalProgram1).isNotEqualTo(educationalProgram2);
    }

    @Test
    void academicYearTest() throws Exception {
        EducationalProgram educationalProgram = getEducationalProgramRandomSampleGenerator();
        AcademicYear academicYearBack = getAcademicYearRandomSampleGenerator();

        educationalProgram.setAcademicYear(academicYearBack);
        assertThat(educationalProgram.getAcademicYear()).isEqualTo(academicYearBack);

        educationalProgram.academicYear(null);
        assertThat(educationalProgram.getAcademicYear()).isNull();
    }

    @Test
    void academicSubjectTest() throws Exception {
        EducationalProgram educationalProgram = getEducationalProgramRandomSampleGenerator();
        AcademicSubject academicSubjectBack = getAcademicSubjectRandomSampleGenerator();

        educationalProgram.addAcademicSubject(academicSubjectBack);
        assertThat(educationalProgram.getAcademicSubjects()).containsOnly(academicSubjectBack);

        educationalProgram.removeAcademicSubject(academicSubjectBack);
        assertThat(educationalProgram.getAcademicSubjects()).doesNotContain(academicSubjectBack);

        educationalProgram.academicSubjects(new HashSet<>(Set.of(academicSubjectBack)));
        assertThat(educationalProgram.getAcademicSubjects()).containsOnly(academicSubjectBack);

        educationalProgram.setAcademicSubjects(new HashSet<>());
        assertThat(educationalProgram.getAcademicSubjects()).doesNotContain(academicSubjectBack);
    }
}
