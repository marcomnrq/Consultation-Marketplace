package com.marcomnrq.consultation.domain.repository;

import com.marcomnrq.consultation.domain.model.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    Page<Review> findByListingId(Long listingId, Pageable pageable);
    Page<Review> findByUserId(Long userId, Pageable pageable);
}
