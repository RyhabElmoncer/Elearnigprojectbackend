package com.org.ElearnigProject.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateVideoRequest {
    private String title;
    private String description;
    private String youtubeLink;
    private String thumbnailUrl;
    private Boolean isPaid;
    private Boolean isVisible;
    private UUID categoryId;
}