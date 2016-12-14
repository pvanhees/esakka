package io.esakka.domain.model.events;

import io.esakka.framework.DomainEvent;

import java.util.Date;

/**
 * Created by pieter on 14/12/2016.
 */
public class CustomerCreated extends DomainEvent {

    public final String aggregateId;
    public final String firstName;
    public final String lastName;
    public final String email;

    public CustomerCreated(final String aggregateId, final Date timestamp, final String firstName, final String lastName, final String email) {
        super(aggregateId, timestamp);
        this.aggregateId = aggregateId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }
}
