package com.org.ElearnigProject.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VideoDTO {
    private UUID id;
    private String title;
    private String description;
    private String youtubeLink;
    private String thumbnailUrl;
    private boolean isPaid;
    private boolean isVisible;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private CategoryDTO category;
    private boolean hasAccess;
}