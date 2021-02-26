package com.marcomnrq.consultation.exception;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class CustomException extends RuntimeException {
    
    private Integer errorCode;

    private HttpStatus httpStatus;
    
    public CustomException(String message) {
        super(message);
        this.errorCode = 9999;
        this.httpStatus = HttpStatus.BAD_REQUEST;
    }

    public CustomException(Integer errorCode, String message){
        super(message);
        this.errorCode = errorCode;
        this.httpStatus = HttpStatus.BAD_REQUEST;
    }

    public CustomException(String message, Throwable throwable){
        super(message, throwable);
        this.httpStatus = HttpStatus.BAD_REQUEST;
    }

    public CustomException(Integer errorCode, String message, Throwable throwable){
        super(message, throwable);
        this.errorCode = errorCode;
        this.httpStatus = HttpStatus.BAD_REQUEST;
    }

    public CustomException(HttpStatus status, Integer errorCode, String message){
        super(message);
        this.errorCode = errorCode;
        this.httpStatus = status;
    }

    // throw new CustomException(1003, "User not found", HttpStatus.HttpStatus.NOT_FOUND);


}
