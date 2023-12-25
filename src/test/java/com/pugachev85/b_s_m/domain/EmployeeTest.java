package com.pugachev85.b_s_m.domain;

import static com.pugachev85.b_s_m.domain.AcademicSubjectTestSamples.*;
import static com.pugachev85.b_s_m.domain.EmployeeTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.pugachev85.b_s_m.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class EmployeeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Employee.class);
        Employee employee1 = getEmployeeSample1();
        Employee employee2 = new Employee();
        assertThat(employee1).isNotEqualTo(employee2);

        employee2.setId(employee1.getId());
        assertThat(employee1).isEqualTo(employee2);

        employee2 = getEmployeeSample2();
        assertThat(employee1).isNotEqualTo(employee2);
    }

    @Test
    void academicSubjectTest() throws Exception {
        Employee employee = getEmployeeRandomSampleGenerator();
        AcademicSubject academicSubjectBack = getAcademicSubjectRandomSampleGenerator();

        employee.addAcademicSubject(academicSubjectBack);
        assertThat(employee.getAcademicSubjects()).containsOnly(academicSubjectBack);

        employee.removeAcademicSubject(academicSubjectBack);
        assertThat(employee.getAcademicSubjects()).doesNotContain(academicSubjectBack);

        employee.academicSubjects(new HashSet<>(Set.of(academicSubjectBack)));
        assertThat(employee.getAcademicSubjects()).containsOnly(academicSubjectBack);

        employee.setAcademicSubjects(new HashSet<>());
        assertThat(employee.getAcademicSubjects()).doesNotContain(academicSubjectBack);
    }
}
