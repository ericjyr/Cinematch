package com.cm.cinematchapp.dto;

import lombok.Data;

/**
 * The `LoginData` class represents the data needed for user login, including a username and password.
 * It is typically used in HTTP requests to capture login credentials when a user attempts to log in.
 *
 * @author Eric Rebadona
 */
@Data
public class LoginDTO {
    private String username;
    private String password;
}
