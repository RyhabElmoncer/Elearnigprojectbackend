package com.org.ElearnigProject.mapper;

import com.org.ElearnigProject.Model.Category;
import com.org.ElearnigProject.dto.CategoryDTO;

public class CategoryMapper {

    public static CategoryDTO mapToDTO(Category category) {
        if (category == null) {
            return null;
        }
        
        return CategoryDTO.builder()
                .id(category.getId())
                .name(category.getName())
                .description(category.getDescription())
                .videoCount(category.getVideos() != null ? category.getVideos().size() : 0)
                .build();
    }
    
    public static Category mapToEntity(CategoryDTO dto) {
        if (dto == null) {
            return null;
        }
        
        return Category.builder()
                .id(dto.getId())
                .name(dto.getName())
                .description(dto.getDescription())
                .build();
    }
}