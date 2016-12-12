package io.esakka.framework;

import org.springframework.data.annotation.Id;

import java.util.Date;

/**
 * Created by pieter on 12/8/16.
 */
public class DomainEvent {

    @Id
    private String id;
    private Long sequenceNr;
    private final String aggregateId;
    private final Date timestamp;

    public DomainEvent(final String aggregateId, final Date timestamp) {
        this.sequenceNr = sequenceNr;
        this.aggregateId = aggregateId;
        this.timestamp = timestamp;
    }

    void setSequenceNr(final Long sequenceNr) {
        this.sequenceNr = sequenceNr;
    }
}
