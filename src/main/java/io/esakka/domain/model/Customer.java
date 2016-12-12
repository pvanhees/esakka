package io.esakka.domain.model;

import io.esakka.domain.model.exceptions.IllegalFirstNameException;
import io.esakka.domain.service.CustomerActor;
import io.esakka.framework.Aggregate;
import io.esakka.framework.Behavior;
import io.esakka.framework.DomainCommand;
import io.esakka.framework.DomainEvent;
import javaslang.collection.List;
import javaslang.control.Try;
import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.NodeEntity;

import static javaslang.API.*;
import static javaslang.Predicates.instanceOf;

/**
 * Created by pieter on 12/8/16.
 */
@NodeEntity
public class Customer extends Aggregate {

    @GraphId
    private Long id;
    private final String firstname;
    private final String lastname;

    public Customer(final String aggregateId, final String firstname, final String lastname) {
        super(aggregateId);
        this.firstname = firstname;
        this.lastname = lastname;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "id='" + id + '\'' +
                ", firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                '}';
    }

    @Override
    public Behavior getBehavior() {
        return defaultBehavior;
    }

    private final Behavior defaultBehavior = new Behavior() {
        @Override
        public Try<List<DomainEvent>> handleCommand(final DomainCommand command) {
            return Match(command).of(
                    Case($(instanceOf(CustomerActor.ChangeFirstNameCommand.class)), Customer.this::changeFirstName)
            );
        }

        @Override
        public Aggregate handleEvent(final DomainEvent event) {
            return Match(event).of(
                    Case($(instanceOf(FirstNameChangedEvent.class)), Customer.this::changeFirstName)
            );
        }
    };

    private Try<List<DomainEvent>> changeFirstName(final CustomerActor.ChangeFirstNameCommand command) {
        final String newFirstName = command.getNewFirstName();
        if (newFirstName == null || newFirstName.isEmpty())
            return Try.failure(new IllegalFirstNameException());

        return Try.success(List.of(new FirstNameChangedEvent(getAggregateId(), newFirstName)));
    }

    private Customer changeFirstName(FirstNameChangedEvent event){
        return new Customer(getAggregateId(), event.getNewFirstName(), this.lastname);
    }
}
