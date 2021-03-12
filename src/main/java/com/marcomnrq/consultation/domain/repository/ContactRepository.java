package com.marcomnrq.consultation.domain.repository;

import com.marcomnrq.consultation.domain.model.Contact;
import com.marcomnrq.consultation.domain.model.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ContactRepository extends JpaRepository<Contact, Long> {
    Page<Contact> findByListingId(Long listingId, Pageable pageable);
    Optional<Contact> findByIdAndListingId(Long id, Long listingId);
}
