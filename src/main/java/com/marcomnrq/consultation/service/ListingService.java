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
import com.sun.mail.iap.Response;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
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
        listing.setPrice(listingResource.getPrice());
        listing.setCategory(category);
        listing.setFriendlyUrl(listing.getId().toString()+"-"+listing.getTitle().toLowerCase().replaceAll(" ", "-"));
        return listingRepository.save(listing);
    }

    public Listing getListingById(Long id){
        return listingRepository.findById(id)
                .orElseThrow(()->new CustomException(1001, "Invalid id, there is no such listing"));
    }

    public Page<Listing> findAll(Pageable pageable){
        return listingRepository.findAll(pageable);
    }

    public Listing editListing(String email, Long listingId, SaveListingResource resource){
        // Verifying user and category
        User user = userRepository.findByEmail(email)
                .orElseThrow(()->new CustomException(1000, "Invalid username, no user match with email value"));
        Category category = categoryRepository.findById(resource.getCategoryId())
                .orElseThrow(()->new CustomException(1001, "Invalid category, there is no such category"));
        Listing existingListing = listingRepository.findById(listingId)
                .orElseThrow(()->new CustomException(1001, "Invalid id, there is no such listing"));
        if(existingListing.getUser() == user){
            existingListing.setVisible(resource.getVisible());
            existingListing.setTitle(resource.getTitle());
            existingListing.setDescription(resource.getDescription());
            existingListing.setFeatured(false);
            existingListing.setRating(0.00);
            existingListing.setRatingsCount(0);
            existingListing.setCurrency(Currency.PEN);
            existingListing.setPrice(resource.getPrice());
            existingListing.setCategory(category);
            existingListing.setFriendlyUrl(existingListing.getId().toString()+"-"+existingListing.getTitle().toLowerCase().replaceAll(" ", "-"));
            return listingRepository.save(existingListing);
        }else{
            throw new CustomException("you can't do that");
        }

    }

    public ResponseEntity<?> deleteListing(String email, Long listingId){
        User user = userRepository.findByEmail(email)
                .orElseThrow(()->new CustomException(1000, "Invalid username, no user match with email value"));
        Listing existingListing = listingRepository.findById(listingId)
                .orElseThrow(()->new CustomException(1001, "Invalid id, there is no such listing"));
        if(existingListing.getUser() == user) {
            listingRepository.delete(existingListing);
            return ResponseEntity.ok().build();
        } else{
            throw new CustomException("you can't do that");
        }
    }
}
