package com.pugachev85.b_s_m.domain;

import static com.pugachev85.b_s_m.domain.AcademicSubjectTestSamples.*;
import static com.pugachev85.b_s_m.domain.PersonalGradeTestSamples.*;
import static com.pugachev85.b_s_m.domain.StudentTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.pugachev85.b_s_m.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class PersonalGradeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PersonalGrade.class);
        PersonalGrade personalGrade1 = getPersonalGradeSample1();
        PersonalGrade personalGrade2 = new PersonalGrade();
        assertThat(personalGrade1).isNotEqualTo(personalGrade2);

        personalGrade2.setId(personalGrade1.getId());
        assertThat(personalGrade1).isEqualTo(personalGrade2);

        personalGrade2 = getPersonalGradeSample2();
        assertThat(personalGrade1).isNotEqualTo(personalGrade2);
    }

    @Test
    void academicSubjectTest() throws Exception {
        PersonalGrade personalGrade = getPersonalGradeRandomSampleGenerator();
        AcademicSubject academicSubjectBack = getAcademicSubjectRandomSampleGenerator();

        personalGrade.setAcademicSubject(academicSubjectBack);
        assertThat(personalGrade.getAcademicSubject()).isEqualTo(academicSubjectBack);

        personalGrade.academicSubject(null);
        assertThat(personalGrade.getAcademicSubject()).isNull();
    }

    @Test
    void studentTest() throws Exception {
        PersonalGrade personalGrade = getPersonalGradeRandomSampleGenerator();
        Student studentBack = getStudentRandomSampleGenerator();

        personalGrade.addStudent(studentBack);
        assertThat(personalGrade.getStudents()).containsOnly(studentBack);

        personalGrade.removeStudent(studentBack);
        assertThat(personalGrade.getStudents()).doesNotContain(studentBack);

        personalGrade.students(new HashSet<>(Set.of(studentBack)));
        assertThat(personalGrade.getStudents()).containsOnly(studentBack);

        personalGrade.setStudents(new HashSet<>());
        assertThat(personalGrade.getStudents()).doesNotContain(studentBack);
    }
}
