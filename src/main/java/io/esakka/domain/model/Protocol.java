package io.esakka.domain.model;

import io.esakka.framework.DomainCommand;

/**
 * Created by pieter on 13/12/2016.
 */
public class Protocol {
    public static class ChangeFirstName implements DomainCommand {
        public final String newFirstName;


        public ChangeFirstName(String newFirstName) {
            this.newFirstName = newFirstName;
        }
    }

    public static class CreateCustomer implements DomainCommand {
        public final String aggregateId;
        public final String firstName;
        public final String lastName;
        public final String email;

        public CreateCustomer(final String aggregateId, final String firstName, final String lastName, final String email) {
            this.aggregateId = aggregateId;
            this.firstName = firstName;
            this.lastName = lastName;
            this.email = email;
        }
    }

    public static class ValidateCustomer implements DomainCommand {

    }
}
