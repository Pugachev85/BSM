package com.pugachev85.b_s_m.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class EmployeeTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Employee getEmployeeSample1() {
        return new Employee().id(1L).firstName("firstName1").secondName("secondName1").lastName("lastName1").jobTitle("jobTitle1");
    }

    public static Employee getEmployeeSample2() {
        return new Employee().id(2L).firstName("firstName2").secondName("secondName2").lastName("lastName2").jobTitle("jobTitle2");
    }

    public static Employee getEmployeeRandomSampleGenerator() {
        return new Employee()
            .id(longCount.incrementAndGet())
            .firstName(UUID.randomUUID().toString())
            .secondName(UUID.randomUUID().toString())
            .lastName(UUID.randomUUID().toString())
            .jobTitle(UUID.randomUUID().toString());
    }
}
