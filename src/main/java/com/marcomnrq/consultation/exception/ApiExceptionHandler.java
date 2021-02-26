package com.marcomnrq.consultation.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.ZoneId;
import java.time.ZonedDateTime;

@ControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(value = {CustomException.class})
    public ResponseEntity<Object> handleException(CustomException customException){
        // Creating a payload with the errors
        ExceptionResource exceptionResource = new ExceptionResource(
                customException.getHttpStatus(),
                customException.getHttpStatus().value(),
                customException.getErrorCode(),
                customException.getMessage(),
                ZonedDateTime.now(ZoneId.of("Z")));
        // Returning the response entity
        return new ResponseEntity<>(exceptionResource, customException.getHttpStatus());
    }

}