package com.marcomnrq.consultation.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.ZonedDateTime;

@Data
@AllArgsConstructor
public class ExceptionResource {
    private HttpStatus httpStatus;
    private Integer statusCode;
    private Integer errorCode;
    private String message;
    private ZonedDateTime timestamp;
}
