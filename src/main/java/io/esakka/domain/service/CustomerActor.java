package io.esakka.domain.service;

import akka.actor.Props;
import io.esakka.domain.model.Customer;
import io.esakka.domain.model.FirstNameChangedEvent;
import io.esakka.framework.DomainCommand;
import io.esakka.framework.DomainEvent;
import io.esakka.framework.EventSourced;
import javaslang.API;

import static javaslang.API.*;
import static javaslang.Predicates.instanceOf;


public class CustomerActor extends EventSourced<Customer> {

    public static class ChangeFirstNameCommand implements DomainCommand {
        private final String newFirstName;


        public ChangeFirstNameCommand(String newFirstName) {
            this.newFirstName = newFirstName;
        }

        public String getNewFirstName() {
            return newFirstName;
        }
    }

    private Customer customer;

    public CustomerActor(final MongoEventRepository eventRepository, final Neo4jSnapshotRepository snapshotRepository, final String aggregateId) {
        super(aggregateId, eventRepository, snapshotRepository);
    }


    @Override
    protected Customer handleEvent(final Customer customer, final DomainEvent domainEvent) {
        return Match(domainEvent).of(
                API.Case(API.$(instanceOf(FirstNameChangedEvent.class)), event -> customer.set(event.getNewFirstName()))
        );
    }

    public static Props props(final String id, final MongoEventRepository eventRepository, final Neo4jSnapshotRepository snapshotRepository) {
        return Props.create(CustomerActor.class, () -> new CustomerActor(eventRepository, snapshotRepository, id));
    }
}
