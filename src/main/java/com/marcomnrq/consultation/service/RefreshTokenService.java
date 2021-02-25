package com.marcomnrq.consultation.service;

import com.marcomnrq.consultation.domain.model.RefreshToken;
import com.marcomnrq.consultation.domain.repository.RefreshTokenRepository;
import com.marcomnrq.consultation.exception.CustomException;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
@AllArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    public RefreshToken generateRefreshToken() {
        // Generating a new refresh token
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setCreatedAt(Instant.now());

        // Storing it in the database
        return refreshTokenRepository.save(refreshToken);
    }

    public void validateRefreshToken(String token) {
        refreshTokenRepository.findByToken(token).orElseThrow(() -> new CustomException("Invalid refresh token"));
    }

    public ResponseEntity<?> deleteRefreshToken(String token) {
        return refreshTokenRepository.deleteByToken(token);
    }
}
