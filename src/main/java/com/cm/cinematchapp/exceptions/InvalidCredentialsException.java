package com.cm.cinematchapp.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serial;

/**
 * Exception class to represent an invalid credentials' error. This exception is typically thrown when
 * the credentials are invalid for an user
 *
 * @author Eric Rebadona
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class InvalidCredentialsException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;


    /**
     * Creates a new instance of InvalidCredentialsException with a default message.
     */
    public InvalidCredentialsException() {
        this("Invalid credentials");
    }

    /**
     * Creates a new instance of InvalidCredentialsException with a custom error message.
     *
     * @param message The custom error message.
     */
    public InvalidCredentialsException(String message) {
        this(message, null);
    }

    /**
     * Creates a new instance of InvalidCredentialsException with a custom error message and a cause.
     *
     * @param message The custom error message.
     * @param cause   The cause of the exception.
     */
    public InvalidCredentialsException(String message, Throwable cause) {
        super(message, cause);
    }
}