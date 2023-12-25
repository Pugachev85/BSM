package com.pugachev85.b_s_m.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class AcademicYearTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static AcademicYear getAcademicYearSample1() {
        return new AcademicYear().id(1L).title("title1");
    }

    public static AcademicYear getAcademicYearSample2() {
        return new AcademicYear().id(2L).title("title2");
    }

    public static AcademicYear getAcademicYearRandomSampleGenerator() {
        return new AcademicYear().id(longCount.incrementAndGet()).title(UUID.randomUUID().toString());
    }
}
