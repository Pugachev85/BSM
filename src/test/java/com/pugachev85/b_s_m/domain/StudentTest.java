package com.pugachev85.b_s_m.domain;

import static com.pugachev85.b_s_m.domain.GroupTestSamples.*;
import static com.pugachev85.b_s_m.domain.OrderTestSamples.*;
import static com.pugachev85.b_s_m.domain.PersonalGradeTestSamples.*;
import static com.pugachev85.b_s_m.domain.StudentTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.pugachev85.b_s_m.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class StudentTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Student.class);
        Student student1 = getStudentSample1();
        Student student2 = new Student();
        assertThat(student1).isNotEqualTo(student2);

        student2.setId(student1.getId());
        assertThat(student1).isEqualTo(student2);

        student2 = getStudentSample2();
        assertThat(student1).isNotEqualTo(student2);
    }

    @Test
    void groupTest() throws Exception {
        Student student = getStudentRandomSampleGenerator();
        Group groupBack = getGroupRandomSampleGenerator();

        student.setGroup(groupBack);
        assertThat(student.getGroup()).isEqualTo(groupBack);

        student.group(null);
        assertThat(student.getGroup()).isNull();
    }

    @Test
    void orderTest() throws Exception {
        Student student = getStudentRandomSampleGenerator();
        Order orderBack = getOrderRandomSampleGenerator();

        student.addOrder(orderBack);
        assertThat(student.getOrders()).containsOnly(orderBack);

        student.removeOrder(orderBack);
        assertThat(student.getOrders()).doesNotContain(orderBack);

        student.orders(new HashSet<>(Set.of(orderBack)));
        assertThat(student.getOrders()).containsOnly(orderBack);

        student.setOrders(new HashSet<>());
        assertThat(student.getOrders()).doesNotContain(orderBack);
    }

    @Test
    void personalGradeTest() throws Exception {
        Student student = getStudentRandomSampleGenerator();
        PersonalGrade personalGradeBack = getPersonalGradeRandomSampleGenerator();

        student.addPersonalGrade(personalGradeBack);
        assertThat(student.getPersonalGrades()).containsOnly(personalGradeBack);
        assertThat(personalGradeBack.getStudents()).containsOnly(student);

        student.removePersonalGrade(personalGradeBack);
        assertThat(student.getPersonalGrades()).doesNotContain(personalGradeBack);
        assertThat(personalGradeBack.getStudents()).doesNotContain(student);

        student.personalGrades(new HashSet<>(Set.of(personalGradeBack)));
        assertThat(student.getPersonalGrades()).containsOnly(personalGradeBack);
        assertThat(personalGradeBack.getStudents()).containsOnly(student);

        student.setPersonalGrades(new HashSet<>());
        assertThat(student.getPersonalGrades()).doesNotContain(personalGradeBack);
        assertThat(personalGradeBack.getStudents()).doesNotContain(student);
    }
}
