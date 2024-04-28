package com.midlaj.olikassigment.controller;

import com.midlaj.olikassigment.dto.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

/**
 * General Controller advisor for handling and unhandled exceptions
 */
@RestControllerAdvice
@Slf4j
public class GeneralControllerAdvisor {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception ex) {

        /**
         * log error
         */
        log.error(ex.getMessage());

        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        String error_name = "SERVER_ERROR";

        if (ex instanceof BadRequestException) {
            status = HttpStatus.BAD_REQUEST;
            error_name = "BAD_REQUEST";
        } else if (ex instanceof NoHandlerFoundException) {
            status = HttpStatus.NOT_FOUND;
            error_name = "NOT_ROUTE_FOUND";
        }

        String errorMessage = "An unexpected error occurred.";
        ErrorResponse errorResponse = new ErrorResponse(error_name, errorMessage);

        return new ResponseEntity<>(errorResponse, status);
    }
}