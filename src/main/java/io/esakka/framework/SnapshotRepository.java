package io.esakka.framework;

import java.util.Optional;

/**
 * Created by pieter on 12/9/16.
 */
public interface SnapshotRepository {

    Optional<AggregateSnapshot> findLatestSnapshotByAggregateId(String aggregateId);
}
