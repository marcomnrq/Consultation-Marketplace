package com.marcomnrq.consultation.domain.repository;

import com.marcomnrq.consultation.domain.model.Plan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PlanRepository extends JpaRepository<Plan, Integer>{
    Optional<Plan> findByName(String name);
}
