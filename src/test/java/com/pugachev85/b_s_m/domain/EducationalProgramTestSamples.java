package com.pugachev85.b_s_m.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class EducationalProgramTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static EducationalProgram getEducationalProgramSample1() {
        return new EducationalProgram().id(1L).title("title1");
    }

    public static EducationalProgram getEducationalProgramSample2() {
        return new EducationalProgram().id(2L).title("title2");
    }

    public static EducationalProgram getEducationalProgramRandomSampleGenerator() {
        return new EducationalProgram().id(longCount.incrementAndGet()).title(UUID.randomUUID().toString());
    }
}
