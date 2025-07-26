package com.org.ElearnigProject.services;

import com.org.ElearnigProject.dto.CategoryDTO;
import com.org.ElearnigProject.dto.request.CreateCategoryRequest;

import java.util.List;
import java.util.UUID;

public interface CategoryService {
    List<CategoryDTO> getAllCategories();
    CategoryDTO getCategoryById(UUID id);
    CategoryDTO createCategory(CreateCategoryRequest request);
    CategoryDTO updateCategory(UUID id, CreateCategoryRequest request);
    void deleteCategory(UUID id);
}