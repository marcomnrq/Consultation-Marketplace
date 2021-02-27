package com.marcomnrq.consultation.controller;

import com.marcomnrq.consultation.domain.model.Listing;
import com.marcomnrq.consultation.domain.model.Review;
import com.marcomnrq.consultation.resource.ListingResource;
import com.marcomnrq.consultation.resource.ReviewResource;
import com.marcomnrq.consultation.resource.SaveListingResource;
import com.marcomnrq.consultation.resource.SaveReviewResource;
import com.marcomnrq.consultation.service.ReviewService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.ocpsoft.prettytime.PrettyTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
@RequestMapping("api/v1/")
public class ReviewController {

    private final ReviewService reviewService;

    private final ModelMapper modelMapper;

    private final PrettyTime prettyTime;

    @PostMapping("listings/{id}/reviews")
    public ReviewResource createReview(@PathVariable(name = "id") Long id,
                                       @RequestBody @Valid SaveReviewResource resource,
                                       Principal principal) {
        return convertToResource(reviewService.createReview(principal.getName(), id, resource));
    }

    @GetMapping("listings/{id}/reviews")
    public Page<ReviewResource> getReviewsByListing(@PathVariable(name = "id") Long id,
                                                    Pageable pageable) {
        Page<Review> page = reviewService.getAllReviewsByListing(id, pageable);
        List<ReviewResource> resources = page.getContent().stream().map(this::convertToResource).collect(Collectors.toList());
        return new PageImpl<>(resources, pageable, resources.size());
    }

    @GetMapping("users/{id}/reviews")
    public Page<ReviewResource> getReviewsByUser(@PathVariable(name = "id") Long id,
                                                    Pageable pageable) {
        Page<Review> page = reviewService.getAllReviewsByUser(id, pageable);
        List<ReviewResource> resources = page.getContent().stream().map(this::convertToResource).collect(Collectors.toList());
        return new PageImpl<>(resources, pageable, resources.size());
    }

    @PutMapping("reviews/{id}")
    public ReviewResource editReview(@PathVariable(name = "id") Long id,
                                       @RequestBody @Valid SaveReviewResource resource,
                                       Principal principal) {
        return convertToResource(reviewService.editReview(principal.getName(), id, resource));
    }

    @DeleteMapping("reviews/{id}")
    public ResponseEntity<?> deleteReview(@PathVariable(name = "id") Long id, Principal principal) {
        return reviewService.deleteReview(principal.getName(), id);
    }

    /*
        MODEL MAPPER & PRETTY TIME
    */
    private Review convertToEntity(SaveReviewResource resource) {
        return modelMapper.map(resource, Review.class);
    }
    private ReviewResource convertToResource(Review entity) {
        ReviewResource resource = modelMapper.map(entity, ReviewResource.class);
        resource.setCreated(prettyTime.format(resource.getCreatedAt()));
        resource.setUpdated(prettyTime.format(resource.getUpdatedAt()));
        return resource;
    }
}
