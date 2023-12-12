package com.cm.cinematchapp.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serial;


/**
 * Exception class to represent a duplicate object conflict error. This exception is typically thrown when
 * attempting to create or add an object that already exists in the system and would cause a conflict.
 *
 * @author Eric Rebadona
 */
@ResponseStatus(HttpStatus.CONFLICT)
public class DuplicateObjectException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Creates a new instance of DuplicateObjectException with a default message.
     */
    public DuplicateObjectException() {
        this("This object already exists");
    }

    /**
     * Creates a new instance of DuplicateObjectException with a custom error message.
     *
     * @param message The custom error message.
     */
    public DuplicateObjectException(String message) {
        this(message, null);
    }

    /**
     * Creates a new instance of DuplicateObjectException with a custom error message and a cause.
     *
     * @param message The custom error message.
     * @param cause   The cause of the exception.
     */
    public DuplicateObjectException(String message, Throwable cause) {
        super(message, cause);
    }
}
