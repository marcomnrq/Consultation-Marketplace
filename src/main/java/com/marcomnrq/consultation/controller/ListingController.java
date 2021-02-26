package com.marcomnrq.consultation.controller;

import com.marcomnrq.consultation.domain.model.Listing;
import com.marcomnrq.consultation.domain.model.Professional;
import com.marcomnrq.consultation.resource.ListingResource;
import com.marcomnrq.consultation.resource.ProfessionalResource;
import com.marcomnrq.consultation.resource.SaveListingResource;
import com.marcomnrq.consultation.resource.SaveProfessionalResource;
import com.marcomnrq.consultation.service.ListingService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
@RequestMapping("api/v1")
public class ListingController {

    private final ListingService listingService;

    private final ModelMapper modelMapper;

    @PostMapping("listings")
    public ListingResource createListing(@RequestBody SaveListingResource resource, Principal principal){
        return convertToResource(listingService.createListing(principal.getName(), resource));
    }

    @GetMapping("listings")
    public Page<ListingResource> getAllListings(Pageable pageable){
        Page<Listing> listings = listingService.findAll(pageable);
        List<ListingResource> resources = listings.getContent().stream().map(this::convertToResource).collect(Collectors.toList());
        return new PageImpl<>(resources, pageable, resources.size());
    }

    private Listing convertToEntity(SaveListingResource resource) {
        return modelMapper.map(resource, Listing.class);
    }

    private ListingResource convertToResource(Listing entity) {
        return modelMapper.map(entity, ListingResource.class);
    }

}
