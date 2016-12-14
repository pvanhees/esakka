package io.esakka.domain.model;

import io.esakka.domain.model.events.CustomerCreated;
import io.esakka.domain.model.events.CustomerValidated;
import io.esakka.domain.model.events.FirstNameChanged;
import io.esakka.domain.model.exceptions.IllegalFirstNameException;
import io.esakka.framework.*;
import javaslang.collection.List;
import javaslang.control.Try;
import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.NodeEntity;

import java.util.Date;
import java.util.function.Function;

import static javaslang.API.*;
import static javaslang.Predicates.instanceOf;

/**
 * Created by pieter on 12/8/16.
 */
@NodeEntity
public abstract class Customer extends Aggregate {

    @GraphId
    private Long id;

    private final String aggregateId;
    public static Behavior initialBehavior = new BehaviorBuilder()
            .setCommandHandler(domainCommand -> Match(domainCommand).of(
                    Case($(instanceOf(Protocol.CreateCustomer.class)),
                            command -> Try.success(List.of(
                                    new CustomerCreated(
                                            command.aggregateId,
                                            new Date(),
                                            command.firstName,
                                            command.lastName,
                                            command.email))
                            ))
            ))
            .setEventHandler(domainEvent -> Match(domainEvent).of(
                    Case($(instanceOf(CustomerCreated.class)),
                            event -> new Customer.UnvalidatedCustomer(
                                    event.aggregateId,
                                    event.firstName,
                                    event.lastName,
                                    event.email))
            ))
            .build();

    public Customer(final String aggregateId) {
        this.aggregateId = aggregateId;
    }

    @Override
    public String getAggregateId() {
        return aggregateId;
    }

    @NodeEntity
    public static class UnvalidatedCustomer extends Customer {
        public final String firstname;
        public final String lastname;
        public final String email;

        public UnvalidatedCustomer(final String aggregateId, final String firstname, final String lastname, final String email) {
            super(aggregateId);
            this.firstname = firstname;
            this.lastname = lastname;
            this.email = email;
        }

        @Override
        public Behavior getBehavior() {
            return new BehaviorBuilder()
                    .setCommandHandler(domainCommand -> Match(domainCommand).of(
                            Case($(instanceOf(Protocol.ValidateCustomer.class)),
                                    command -> Try.success(List.of(new CustomerValidated(getAggregateId(), new Date()))))
                    ))
                    .setEventHandler(domainEvent -> Match(domainEvent).of(
                            Case($(instanceOf(CustomerValidated.class)), event -> new ActiveCustomer(
                                    getAggregateId(),
                                    firstname,
                                    lastname,
                                    email))

                    ))
                    .build();
        }


        @Override
        public String toString() {
            return "Customer{" +
                    "id='" + getAggregateId() + '\'' +
                    ", firstname='" + firstname + '\'' +
                    ", lastname='" + lastname + '\'' +
                    '}';
        }
    }

    public static class ActiveCustomer extends Customer {
        public final String firstname;
        public final String lastname;
        public final String email;


        public ActiveCustomer(final String aggregateId, final String firstname, final String lastname, final String email) {
            super(aggregateId);
            this.firstname = firstname;
            this.lastname = lastname;
            this.email = email;
        }

        @Override
        public Behavior getBehavior() {
            return new BehaviorBuilder()
                .setCommandHandler(command -> Match(command).of(
                        Case($(instanceOf(Protocol.ChangeFirstName.class)), ActiveCustomer.this::firstNameChanged)
                ))
                .setEventHandler(event -> Match(event).of(
                        Case($(instanceOf(FirstNameChanged.class)), ActiveCustomer.this::firstNameChanged)
                ))
                .build();
        }

        private Try<List<DomainEvent>> firstNameChanged(final Protocol.ChangeFirstName command) {
            final String newFirstName = command.newFirstName;
            if (newFirstName == null || newFirstName.isEmpty())
                return Try.failure(new IllegalFirstNameException());

            return Try.success(List.of(new FirstNameChanged(getAggregateId(), newFirstName)));
        };

        private Customer firstNameChanged(FirstNameChanged event) {
            return new ActiveCustomer(getAggregateId(), event.getNewFirstName(), this.lastname, this.email);
        }
    }

}
