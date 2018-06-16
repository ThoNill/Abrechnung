package tests;

import java.util.concurrent.atomic.AtomicLong;

import ddd.Entity;

public class TestEntity implements Entity<Long> {
    static private AtomicLong generatorDerIDs = new AtomicLong();
    private long id;

    public TestEntity() {
        super();
        this.id = generatorDerIDs.incrementAndGet();
    }

    @Override
    public Long getId() {
        return id;
    }

}
