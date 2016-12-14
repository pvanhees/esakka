package io.esakka.domain.model.events;

import io.esakka.framework.DomainEvent;

import java.util.Date;

/**
 * Created by pieter on 12/8/16.
 */
public class FirstNameChanged extends DomainEvent {

    private final String newFirstName;

    public FirstNameChanged(final String aggregateId, final String newFirstName) {
        super(aggregateId, new Date());
        this.newFirstName = newFirstName;
    }

    public String getNewFirstName() {
        return newFirstName;
    }
}
