package com.marcomnrq.consultation.service;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.marcomnrq.consultation.exception.CustomException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@Service
public class SentinelService {
    // TODO: security check and implement more security features

    private final LoadingCache<String, Integer> attemptsCache;

    public SentinelService(){
        super();
        attemptsCache = CacheBuilder.newBuilder().
                expireAfterWrite(24, TimeUnit.HOURS).build(new CacheLoader<String, Integer>() {
            public Integer load(String remoteAddress) {
                return 0;
            }
        });
    }

    // Event called everytime an authentication fails
    public void loginFailed(String remoteAddress) {
        bruteForceAttempt(remoteAddress);
        throw new CustomException("Sentinel Service: Bad Credentials", HttpStatus.FORBIDDEN );
    }

    // Event called a user authenticates
    public void loginSucceeded(String remoteAddress) {
        bruteForceInvalidate(remoteAddress);
        System.out.println("Login succeeded: " + remoteAddress);

    }

    /*
        BRUTE FORCE DETECTION
    */
    public void bruteForceAttempt(String remoteAddress){
        int attempts = 0;
        try {
            attempts = attemptsCache.get(remoteAddress);
        } catch (ExecutionException e) {
            throw new CustomException("Sentinel Service: Unexpected error", HttpStatus.FORBIDDEN);
        }
        attempts++;
        attemptsCache.put(remoteAddress, attempts);
    }
    public void bruteForceInvalidate(String remoteAddress){
        attemptsCache.invalidate(remoteAddress);
    }
    public void bruteForceCheck(String clientIp) {
        try {
            int MAX_ATTEMPT = 10;
            if (attemptsCache.get(clientIp) >= MAX_ATTEMPT){
                throw new CustomException("Sentinel Service: Client blocked", HttpStatus.FORBIDDEN);
            }
        } catch (ExecutionException e) {
            throw new CustomException("Sentinel Service: Unexpected error", HttpStatus.FORBIDDEN);
        }
    }

    /*
        UTILITY FUNCTIONS
    */

    // TODO: Add utility functions

}
