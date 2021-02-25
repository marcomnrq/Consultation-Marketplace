package com.marcomnrq.consultation.domain.repository;

import com.marcomnrq.consultation.domain.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
    Optional<Category> findByName(String name);
}
