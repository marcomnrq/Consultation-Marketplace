package com.marcomnrq.consultation.domain.repository;

import com.marcomnrq.consultation.domain.model.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String token);
    ResponseEntity<?> deleteByToken(String token);
}
