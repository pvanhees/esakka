package io.esakka.dto;

/**
 * Created by pieter on 14/12/2016.
 */
public class CustomerDTO {

    private String firstName;
    private String lastName;
    private String email;

    public CustomerDTO() {
    }

    public CustomerDTO(final String firstName, final String lastName, final String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }
}
