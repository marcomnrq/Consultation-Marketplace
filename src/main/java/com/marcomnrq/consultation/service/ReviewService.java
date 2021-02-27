package com.marcomnrq.consultation.service;

import com.marcomnrq.consultation.domain.model.Listing;
import com.marcomnrq.consultation.domain.model.Review;
import com.marcomnrq.consultation.domain.model.User;
import com.marcomnrq.consultation.domain.repository.ListingRepository;
import com.marcomnrq.consultation.domain.repository.ReviewRepository;
import com.marcomnrq.consultation.domain.repository.UserRepository;
import com.marcomnrq.consultation.exception.CustomException;
import com.marcomnrq.consultation.resource.SaveReviewResource;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;

    private final ListingRepository listingRepository;

    private final UserRepository userRepository;

    public Review createReview(String email, Long listingId, SaveReviewResource resource){
        // Check user
        User user = userRepository.findByEmail(email)
                .orElseThrow(()-> new CustomException("User not found"));
        // Check the listing
        Listing listing = listingRepository.findById(listingId)
                .orElseThrow(()-> new CustomException("Listing not found"));
        if(listing.getUser() != user){
            // TODO: set rating on the listing
            // User is not the author
            Review review = new Review();
            review.setContent(resource.getContent());
            review.setRating(resource.getRating());
            review.setUser(user);
            review.setListing(listing);
            return reviewRepository.save(review);
        }else{
            throw new CustomException("You can't do that");
        }
    }

    public Page<Review> getAllReviewsByListing(Long listingId, Pageable pageable){
        return reviewRepository.findByListingId(listingId, pageable);
    }
    public Page<Review> getAllReviewsByUser(Long userId, Pageable pageable){
        return reviewRepository.findByUserId(userId, pageable);
    }

    public Review editReview(String email, Long reviewId, SaveReviewResource resource){
        // Check user
        User user = userRepository.findByEmail(email)
                .orElseThrow(()-> new CustomException("User not found"));
        // Check the listing
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(()-> new CustomException("Review not found"));
        if(review.getUser() == user){
            // TODO: update rating on the listing
            // User is the author
            review.setContent(resource.getContent());
            review.setRating(resource.getRating());
            return reviewRepository.save(review);
        }else{
            throw new CustomException("You can't do that");
        }
    }

    public ResponseEntity<?> deleteReview(String email, Long reviewId){
        // Check user
        User user = userRepository.findByEmail(email)
                .orElseThrow(()-> new CustomException("User not found"));
        // Check the listing
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(()-> new CustomException("Review not found"));
        if(review.getUser() == user){
            // User is the author
            reviewRepository.delete(review);
        }else{
            throw new CustomException("You can't do that");
        }

        return ResponseEntity.ok().build();
    }
}
