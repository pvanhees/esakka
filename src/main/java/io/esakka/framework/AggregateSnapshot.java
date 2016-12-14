package io.esakka.framework;

import org.neo4j.ogm.annotation.NodeEntity;

/**
 * Created by pieter on 12/9/16.
 */
@NodeEntity
public class AggregateSnapshot {

    private final Long sequenceNr;
    private final Aggregate aggregate;

    public AggregateSnapshot(final Long sequenceNr, final Aggregate aggregate) {
        this.sequenceNr = sequenceNr;
        this.aggregate = aggregate;
    }

    public Aggregate getAggregate() {
        return aggregate;
    }

    public Long getSequenceNr() {
        return sequenceNr;
    }
}
