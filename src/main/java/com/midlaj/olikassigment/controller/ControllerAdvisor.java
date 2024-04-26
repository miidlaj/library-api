package com.midlaj.olikassigment.controller;

import com.midlaj.olikassigment.dto.ErrorResponse;
import com.midlaj.olikassigment.exception.AlreadyRentedException;
import com.midlaj.olikassigment.exception.DuplicateEntityException;
import com.midlaj.olikassigment.exception.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class ControllerAdvisor {

    /**
     * For duplicate entity
     *
     * @param ex
     * @return error_name DUPLICATE
     */
    @ExceptionHandler(DuplicateEntityException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleDuplicateEntity(DuplicateEntityException ex) {

        /**
         * log error
         */
        log.warn(ex.getMessage());

        return new ErrorResponse("DUPLICATE", ex.getMessage());
    }

    /**
     * For not found entity
     *
     * @param ex
     * @return error_name NOT_FOUND
     */
    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleNotFoundEntity(EntityNotFoundException ex) {

        /**
         * log error
         */
        log.warn(ex.getMessage());

        return new ErrorResponse("NOT_FOUND", ex.getMessage());
    }

    /**
     * For unavailable book
     *
     * @param ex
     * @return error_name NOT_AVAILABLE
     */
    @ExceptionHandler(AlreadyRentedException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleAlreadyRentedOut(AlreadyRentedException ex) {

        /**
         * log error
         */
        log.warn(ex.getMessage());

        return new ErrorResponse("NOT_AVAILABLE", ex.getMessage());
    }

    /**
     * For validation errors
     *
     * @param ex
     * @return error_name INVALID_DATA
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(
            MethodArgumentNotValidException ex) {

        /**
         * log error
         */
        log.warn(ex.getMessage());

        Map<String, String> errors = new HashMap<>();
        errors.put("error_name", "INVALID_DATA");
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }
}

