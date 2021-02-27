package com.marcomnrq.consultation.controller;

import com.marcomnrq.consultation.domain.model.Category;
import com.marcomnrq.consultation.domain.model.Listing;
import com.marcomnrq.consultation.resource.CategoryResource;
import com.marcomnrq.consultation.resource.ListingResource;
import com.marcomnrq.consultation.resource.SaveCategoryResource;
import com.marcomnrq.consultation.resource.SaveListingResource;
import com.marcomnrq.consultation.service.CategoryService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("api/v1/")
public class CategoryController {

    private final CategoryService categoryService;

    private final ModelMapper modelMapper;

    @PostMapping("categories")
    public CategoryResource createCategory(@RequestBody SaveCategoryResource resource){
        return convertToResource(categoryService.createCategory(resource));
    }

    private Category convertToEntity(SaveCategoryResource resource) {
        return modelMapper.map(resource, Category.class);
    }

    private CategoryResource convertToResource(Category entity) {
        return modelMapper.map(entity, CategoryResource.class);
    }

}
