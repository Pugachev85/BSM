package com.pugachev85.b_s_m.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class AcademicSubjectTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static AcademicSubject getAcademicSubjectSample1() {
        return new AcademicSubject().id(1L).title("title1").hours(1);
    }

    public static AcademicSubject getAcademicSubjectSample2() {
        return new AcademicSubject().id(2L).title("title2").hours(2);
    }

    public static AcademicSubject getAcademicSubjectRandomSampleGenerator() {
        return new AcademicSubject().id(longCount.incrementAndGet()).title(UUID.randomUUID().toString()).hours(intCount.incrementAndGet());
    }
}
