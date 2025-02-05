package com.in28minutes.rest.webservices.restfulwebservices.util;

import java.util.concurrent.atomic.AtomicInteger;

public class TodoIdGenerator {
    private static final AtomicInteger counter = new AtomicInteger(0);

    public static synchronized String getNextSequence() {
        int nextVal = counter.incrementAndGet();  // Increment the counter
        return String.format("AA%03d", nextVal); // Format as AA001, AA002, etc.
    }
}
