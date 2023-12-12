package com.cm.cinematchapp.advice;

import com.cm.cinematchapp.exceptions.DuplicateObjectException;
import com.cm.cinematchapp.exceptions.InvalidCredentialsException;
import com.cm.cinematchapp.exceptions.ResourceNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Global exception handler for REST controllers in the application. This class provides exception handling
 * for various types of exceptions, such as binding errors, resource not found, and duplicate object conflicts.
 *
 * @author Eric Rebadona
 */
@RestControllerAdvice
public class ExceptionHandlerAdvice {

    /**
     * Exception handler for handling `BindException`. It returns a bad request response along with an error message
     * extracted from the first error in the `BindException`.
     *
     * @param ex The `BindException` to be handled.
     * @return A `ResponseEntity` with a bad request status and an error message.
     */
    @ExceptionHandler(value= {BindException.class})
    ResponseEntity<String> bindExceptionHandler(BindException ex) {
        return ResponseEntity.badRequest()
                .body(ex.getAllErrors().get(0).getDefaultMessage());
    }

    /**
     * Exception handler for handling `ResourceNotFoundException`. It returns a not found response along with
     * the exception message.
     *
     * @param e The `ResourceNotFoundException` to be handled.
     * @return A `ResponseEntity` with a not found status and the exception message.
     */
    @ExceptionHandler({ResourceNotFoundException.class})
    ResponseEntity<String> ResourceNotFoundExceptionHandler(ResourceNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    /**
     * Exception handler for handling `DuplicateObjectException`. It returns a conflict response along with
     * the exception message.
     *
     * @param e The `DuplicateObjectException` to be handled.
     * @return A `ResponseEntity` with a conflict status and the exception message.
     */
    @ExceptionHandler({DuplicateObjectException.class})
    ResponseEntity<String> DuplicateObjectExceptionHandler(DuplicateObjectException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
    }

    @ExceptionHandler({DataIntegrityViolationException.class})
    ResponseEntity<String> DataIntegrityViolationExceptionHandler(DataIntegrityViolationException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

    @ExceptionHandler({InvalidCredentialsException.class})
    ResponseEntity<String> InvalidCredentialsExceptionHandler(InvalidCredentialsException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }


}
