package com.pugachev85.b_s_m.domain;

import static com.pugachev85.b_s_m.domain.OrderTestSamples.*;
import static com.pugachev85.b_s_m.domain.StudentTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.pugachev85.b_s_m.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class OrderTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Order.class);
        Order order1 = getOrderSample1();
        Order order2 = new Order();
        assertThat(order1).isNotEqualTo(order2);

        order2.setId(order1.getId());
        assertThat(order1).isEqualTo(order2);

        order2 = getOrderSample2();
        assertThat(order1).isNotEqualTo(order2);
    }

    @Test
    void studentTest() throws Exception {
        Order order = getOrderRandomSampleGenerator();
        Student studentBack = getStudentRandomSampleGenerator();

        order.addStudent(studentBack);
        assertThat(order.getStudents()).containsOnly(studentBack);
        assertThat(studentBack.getOrders()).containsOnly(order);

        order.removeStudent(studentBack);
        assertThat(order.getStudents()).doesNotContain(studentBack);
        assertThat(studentBack.getOrders()).doesNotContain(order);

        order.students(new HashSet<>(Set.of(studentBack)));
        assertThat(order.getStudents()).containsOnly(studentBack);
        assertThat(studentBack.getOrders()).containsOnly(order);

        order.setStudents(new HashSet<>());
        assertThat(order.getStudents()).doesNotContain(studentBack);
        assertThat(studentBack.getOrders()).doesNotContain(order);
    }
}
