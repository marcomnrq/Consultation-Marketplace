package com.marcomnrq.consultation.domain.repository;

import com.marcomnrq.consultation.domain.model.Plan;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlanRepository extends JpaRepository<Plan, Integer>{

}
