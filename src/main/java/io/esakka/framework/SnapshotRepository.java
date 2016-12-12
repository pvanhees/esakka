package io.esakka.framework;

/**
 * Created by pieter on 12/9/16.
 */
public interface SnapshotRepository<A extends Aggregate> {

    A findByAggregateId(String aggregateId);
}
