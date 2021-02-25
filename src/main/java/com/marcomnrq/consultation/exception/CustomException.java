package com.marcomnrq.consultation.exception;

import lombok.Data;

@Data
public class CustomException extends RuntimeException {
    
    private Integer errorCode;
    
    public CustomException(String message) {
        super(message);
        this.errorCode = 0000;
    }

    public CustomException(Integer errorCode, String message){
        super(message);
        this.errorCode = errorCode;
    }

    public CustomException(String message, Throwable throwable){
        super(message, throwable);
    }
    public CustomException(Integer errorCode, String message, Throwable throwable){
        super(message, throwable);
        this.errorCode = errorCode;
    }


}
