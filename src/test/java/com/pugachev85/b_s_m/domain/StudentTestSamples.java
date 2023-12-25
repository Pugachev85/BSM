package com.pugachev85.b_s_m.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class StudentTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Student getStudentSample1() {
        return new Student().id(1L).alphabetBookNumber(1L).firstName("firstName1").secondName("secondName1").lastName("lastName1");
    }

    public static Student getStudentSample2() {
        return new Student().id(2L).alphabetBookNumber(2L).firstName("firstName2").secondName("secondName2").lastName("lastName2");
    }

    public static Student getStudentRandomSampleGenerator() {
        return new Student()
            .id(longCount.incrementAndGet())
            .alphabetBookNumber(longCount.incrementAndGet())
            .firstName(UUID.randomUUID().toString())
            .secondName(UUID.randomUUID().toString())
            .lastName(UUID.randomUUID().toString());
    }
}
