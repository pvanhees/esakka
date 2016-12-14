package io.esakka.framework;

import akka.actor.AbstractLoggingActor;

/**
 * Created by pieter on 12/9/16.
 */
public abstract class Aggregate extends AbstractLoggingActor {

    public Behavior behavior;

    public Aggregate() {
        this.behavior = getBehavior();
    }

    public abstract String getAggregateId();
    public abstract Behavior getBehavior();

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final Aggregate aggregate = (Aggregate) o;

        return getAggregateId() != null ? getAggregateId().equals(aggregate.getAggregateId()) : aggregate.getAggregateId() == null;
    }

    @Override
    public int hashCode() {
        return getAggregateId() != null ? getAggregateId().hashCode() : 0;
    }
}
