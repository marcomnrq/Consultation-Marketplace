package com.marcomnrq.consultation.service;

import com.marcomnrq.consultation.domain.model.Category;
import com.marcomnrq.consultation.domain.model.Currency;
import com.marcomnrq.consultation.domain.model.Listing;
import com.marcomnrq.consultation.domain.model.User;
import com.marcomnrq.consultation.domain.repository.CategoryRepository;
import com.marcomnrq.consultation.domain.repository.ListingRepository;
import com.marcomnrq.consultation.domain.repository.UserRepository;
import com.marcomnrq.consultation.exception.CustomException;
import com.marcomnrq.consultation.resource.SaveListingResource;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
@AllArgsConstructor
public class ListingService {

    private final ListingRepository listingRepository;

    private final UserRepository userRepository;

    private final CategoryRepository categoryRepository;

    public Listing createListing(String email, SaveListingResource listingResource){
        // Verifying user and category
        User user = userRepository.findByEmail(email)
                .orElseThrow(()->new CustomException(1000, "Invalid username, no user match with email value"));
        Category category = categoryRepository.findById(listingResource.getCategoryId())
                .orElseThrow(()->new CustomException(1001, "Invalid category, there is no such category"));

        // Creating new listing
        Listing listing = new Listing();
        listing.setUser(user);
        listing.setVisible(listingResource.getVisible());
        listing.setTitle(listingResource.getTitle());
        listing.setDescription(listingResource.getDescription());
        listing.setFeatured(false);
        listing.setRating(0.00);
        listing.setRatingsCount(0);
        listing.setCurrency(Currency.PEN);
        listing.setCategory(category);
        return listingRepository.save(listing);
    }

    public Page<Listing> findAll(Pageable pageable){
        return listingRepository.findAll(pageable);
    }
}
