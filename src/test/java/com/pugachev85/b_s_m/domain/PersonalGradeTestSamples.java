package com.pugachev85.b_s_m.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class PersonalGradeTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static PersonalGrade getPersonalGradeSample1() {
        return new PersonalGrade().id(1L).grade(1);
    }

    public static PersonalGrade getPersonalGradeSample2() {
        return new PersonalGrade().id(2L).grade(2);
    }

    public static PersonalGrade getPersonalGradeRandomSampleGenerator() {
        return new PersonalGrade().id(longCount.incrementAndGet()).grade(intCount.incrementAndGet());
    }
}
