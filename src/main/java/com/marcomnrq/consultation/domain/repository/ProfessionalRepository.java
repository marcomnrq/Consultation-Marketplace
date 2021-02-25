package com.marcomnrq.consultation.domain.repository;

import com.marcomnrq.consultation.domain.model.Professional;
import com.marcomnrq.consultation.domain.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProfessionalRepository extends JpaRepository<Professional, Long> {
    Optional<Professional> findByShortName(String shortName);
    Optional<Professional> findByUser(User user);
    Boolean existsByShortName(String shortName);
}
