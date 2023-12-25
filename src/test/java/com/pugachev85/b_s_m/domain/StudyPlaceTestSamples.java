package com.pugachev85.b_s_m.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class StudyPlaceTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static StudyPlace getStudyPlaceSample1() {
        return new StudyPlace().id(1L).title("title1");
    }

    public static StudyPlace getStudyPlaceSample2() {
        return new StudyPlace().id(2L).title("title2");
    }

    public static StudyPlace getStudyPlaceRandomSampleGenerator() {
        return new StudyPlace().id(longCount.incrementAndGet()).title(UUID.randomUUID().toString());
    }
}
