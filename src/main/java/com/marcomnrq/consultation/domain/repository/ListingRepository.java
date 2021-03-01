package com.marcomnrq.consultation.domain.repository;

import com.marcomnrq.consultation.domain.model.Listing;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ListingRepository extends JpaRepository<Listing, Long> {
    Page<Listing> findByTitleLike(String title, Pageable pageable);
    Page<Listing> findAllByTitleIsWithin(String title, Pageable pageable);
}
