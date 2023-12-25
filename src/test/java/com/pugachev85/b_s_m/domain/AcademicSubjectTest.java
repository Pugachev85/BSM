package com.pugachev85.b_s_m.domain;

import static com.pugachev85.b_s_m.domain.AcademicSubjectTestSamples.*;
import static com.pugachev85.b_s_m.domain.EducationalProgramTestSamples.*;
import static com.pugachev85.b_s_m.domain.EmployeeTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.pugachev85.b_s_m.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class AcademicSubjectTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AcademicSubject.class);
        AcademicSubject academicSubject1 = getAcademicSubjectSample1();
        AcademicSubject academicSubject2 = new AcademicSubject();
        assertThat(academicSubject1).isNotEqualTo(academicSubject2);

        academicSubject2.setId(academicSubject1.getId());
        assertThat(academicSubject1).isEqualTo(academicSubject2);

        academicSubject2 = getAcademicSubjectSample2();
        assertThat(academicSubject1).isNotEqualTo(academicSubject2);
    }

    @Test
    void educationalProgramTest() throws Exception {
        AcademicSubject academicSubject = getAcademicSubjectRandomSampleGenerator();
        EducationalProgram educationalProgramBack = getEducationalProgramRandomSampleGenerator();

        academicSubject.addEducationalProgram(educationalProgramBack);
        assertThat(academicSubject.getEducationalPrograms()).containsOnly(educationalProgramBack);
        assertThat(educationalProgramBack.getAcademicSubjects()).containsOnly(academicSubject);

        academicSubject.removeEducationalProgram(educationalProgramBack);
        assertThat(academicSubject.getEducationalPrograms()).doesNotContain(educationalProgramBack);
        assertThat(educationalProgramBack.getAcademicSubjects()).doesNotContain(academicSubject);

        academicSubject.educationalPrograms(new HashSet<>(Set.of(educationalProgramBack)));
        assertThat(academicSubject.getEducationalPrograms()).containsOnly(educationalProgramBack);
        assertThat(educationalProgramBack.getAcademicSubjects()).containsOnly(academicSubject);

        academicSubject.setEducationalPrograms(new HashSet<>());
        assertThat(academicSubject.getEducationalPrograms()).doesNotContain(educationalProgramBack);
        assertThat(educationalProgramBack.getAcademicSubjects()).doesNotContain(academicSubject);
    }

    @Test
    void employeeTest() throws Exception {
        AcademicSubject academicSubject = getAcademicSubjectRandomSampleGenerator();
        Employee employeeBack = getEmployeeRandomSampleGenerator();

        academicSubject.addEmployee(employeeBack);
        assertThat(academicSubject.getEmployees()).containsOnly(employeeBack);
        assertThat(employeeBack.getAcademicSubjects()).containsOnly(academicSubject);

        academicSubject.removeEmployee(employeeBack);
        assertThat(academicSubject.getEmployees()).doesNotContain(employeeBack);
        assertThat(employeeBack.getAcademicSubjects()).doesNotContain(academicSubject);

        academicSubject.employees(new HashSet<>(Set.of(employeeBack)));
        assertThat(academicSubject.getEmployees()).containsOnly(employeeBack);
        assertThat(employeeBack.getAcademicSubjects()).containsOnly(academicSubject);

        academicSubject.setEmployees(new HashSet<>());
        assertThat(academicSubject.getEmployees()).doesNotContain(employeeBack);
        assertThat(employeeBack.getAcademicSubjects()).doesNotContain(academicSubject);
    }
}
