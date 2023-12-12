package com.cm.cinematchapp.dto;

import lombok.Data;

/**
 * The `RegistrationData` class represents the data needed for user registration. These require first and last name,
 * username, password and email to allow the process to go through.
 *
 * @author Mateus Souza
 */
@Data
public class RegistrationDTO {
    private String firstName;
    private String lastName;
    private String username;
    private String password;
    private String email;
}