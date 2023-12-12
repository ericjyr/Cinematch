package com.cm.cinematchapp.exceptions;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serial;

/**
 * Exception class to represent a resource not found error. This exception is typically thrown when
 * a requested resource, such as a user or entity, is not found in the system.
 *
 * @author Eric Rebadona
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException{

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Creates a new instance of ResourceNotFoundException with a default message.
     */
    public ResourceNotFoundException() {
        this("Username not found");
    }

    /**
     * Creates a new instance of ResourceNotFoundException with a custom error message.
     *
     * @param message The custom error message.
     */
    public ResourceNotFoundException(String message) {
        this(message, null);
    }

    /**
     * Creates a new instance of ResourceNotFoundException with a custom error message and a cause.
     *
     * @param message The custom error message.
     * @param cause   The cause of the exception.
     */
    public ResourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

}
