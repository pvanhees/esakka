package io.esakka.framework;

/**
 * Created by pieter on 12/9/16.
 */
public abstract class Aggregate {

    private final String aggregateId;

    public Aggregate(final String aggregateId) {
        this.aggregateId = aggregateId;
    }

    public String getAggregateId() {
        return aggregateId;
    }

    public abstract Behavior getBehavior();


    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final Aggregate aggregate = (Aggregate) o;

        return aggregateId.equals(aggregate.aggregateId);
    }

    @Override
    public int hashCode() {
        return aggregateId.hashCode();
    }
}
