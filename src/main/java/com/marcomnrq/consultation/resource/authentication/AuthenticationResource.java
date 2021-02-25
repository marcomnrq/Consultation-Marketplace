package com.marcomnrq.consultation.resource.authentication;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Instant;

@Data
@AllArgsConstructor
public class AuthenticationResource {
    private String authenticationToken;
    private String refreshToken;
    private Instant expiresAt;
    private String email;
    private Boolean using2fa;
}