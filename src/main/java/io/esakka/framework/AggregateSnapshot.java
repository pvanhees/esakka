package io.esakka.framework;

import org.neo4j.ogm.annotation.NodeEntity;

/**
 * Created by pieter on 12/9/16.
 */
@NodeEntity
public class AggregateSnapshot {

    private String aggregateId;
    private Long sequenceNr;
}
