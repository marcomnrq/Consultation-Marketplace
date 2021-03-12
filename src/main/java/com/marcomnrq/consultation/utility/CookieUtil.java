package com.marcomnrq.consultation.utility;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpCookie;
import org.springframework.http.ResponseCookie;

public class CookieUtil {

    @Value("${jwt.expiration.time}")
    private Long jwtExpirationInMillis;

    public HttpCookie createAccessTokenCookie(String token) {
        String encryptedToken = SecurityCipher.encrypt(token);
        return ResponseCookie.from("accessToken", encryptedToken)
                .maxAge(jwtExpirationInMillis)
                .httpOnly(true)
                .path("/")
                .build();
    }

    public HttpCookie createRefreshTokenCookie(String token) {
        String encryptedToken = SecurityCipher.encrypt(token);
        return ResponseCookie.from("refreshToken", encryptedToken)
                .maxAge(jwtExpirationInMillis)
                .httpOnly(true)
                .path("/")
                .build();
    }

    public HttpCookie deleteAccessTokenCookie() {
        return ResponseCookie.from("accessToken", "").maxAge(0).httpOnly(true).path("/").build();
    }

}