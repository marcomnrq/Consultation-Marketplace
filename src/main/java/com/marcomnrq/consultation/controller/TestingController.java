package com.marcomnrq.consultation.controller;

import com.marcomnrq.consultation.exception.CustomException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1")
public class TestingController {
    @GetMapping("testing")
    public ResponseEntity<?> testingGetRequest(){
        throw new CustomException(1001, "Invalid message");
    }
}
