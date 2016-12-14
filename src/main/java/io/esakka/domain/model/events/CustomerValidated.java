package io.esakka.domain.model.events;

import io.esakka.framework.DomainEvent;

import java.util.Date;

/**
 * Created by pieter on 14/12/2016.
 */
public class CustomerValidated extends DomainEvent {
    public CustomerValidated(final String aggregateId, final Date timestamp) {
        super(aggregateId, timestamp);
    }
}
