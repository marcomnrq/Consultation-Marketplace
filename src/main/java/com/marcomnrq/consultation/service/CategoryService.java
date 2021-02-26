package com.marcomnrq.consultation.service;

import com.marcomnrq.consultation.domain.model.Category;
import com.marcomnrq.consultation.domain.repository.CategoryRepository;
import com.marcomnrq.consultation.exception.CustomException;
import com.marcomnrq.consultation.resource.SaveCategoryResource;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public Category createCategory(SaveCategoryResource resource){
        Category category = new Category();
        Category parent = categoryRepository.findById(resource.getParentId())
                .orElseThrow(()->new CustomException("Invalid parent"));
        category.setActive(resource.getActive());
        category.setName(resource.getName());
        category.setParent(parent);
        return categoryRepository.save(category);
    }
    public Category editCategory(Integer id, SaveCategoryResource resource){
        Category category = categoryRepository.findById(id)
                .orElseThrow(()->new CustomException("Category not found"));
        Category parent = categoryRepository.findById(resource.getParentId())
                .orElseThrow(()->new CustomException("Invalid parent"));
        category.setActive(resource.getActive());
        category.setName(resource.getName());
        category.setParent(parent);
        return categoryRepository.save(category);
    }
    public Category editSubCategories(Integer id, List<SaveCategoryResource> list){
        Category category = categoryRepository.findById(id)
                .orElseThrow(()->new CustomException("Id invalid, category not found"));
        category.getSubCategories().clear();
        for(SaveCategoryResource resource : list){
            Category subCategory = categoryRepository.findByName(resource.getName())
                    .orElseThrow(()->new CustomException("Sub-category not found"));
            subCategory.setParent(category);
            category.getSubCategories().add(subCategory);
            return categoryRepository.save(subCategory);
        }
        return categoryRepository.save(category);
    }

    public ResponseEntity<?> deleteCategory(Integer id){
        Category category = categoryRepository.findById(id)
                .orElseThrow(()->new CustomException("Invalid parent"));;
        categoryRepository.delete(category);
        return ResponseEntity.ok().build();
    }
}
