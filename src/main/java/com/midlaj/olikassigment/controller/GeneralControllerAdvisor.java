package com.midlaj.olikassigment.controller;

import com.midlaj.olikassigment.dto.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

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

        if (ex instanceof BadRequestException) {
            status = HttpStatus.BAD_REQUEST;
        } else if (ex instanceof ChangeSetPersister.NotFoundException){
            status = HttpStatus.NOT_FOUND;
        }

        String errorMessage = "An unexpected error occurred.";
        ErrorResponse errorResponse = new ErrorResponse("SERVER_ERROR", errorMessage);

        return new ResponseEntity<>(errorResponse, status);
    }
}