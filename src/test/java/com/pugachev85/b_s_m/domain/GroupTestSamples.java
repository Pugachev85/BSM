package com.pugachev85.b_s_m.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class GroupTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Group getGroupSample1() {
        return new Group().id(1L).title("title1");
    }

    public static Group getGroupSample2() {
        return new Group().id(2L).title("title2");
    }

    public static Group getGroupRandomSampleGenerator() {
        return new Group().id(longCount.incrementAndGet()).title(UUID.randomUUID().toString());
    }
}
