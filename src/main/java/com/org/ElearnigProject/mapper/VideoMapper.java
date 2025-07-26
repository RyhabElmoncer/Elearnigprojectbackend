package com.org.ElearnigProject.mapper;

import com.org.ElearnigProject.Model.User;
import com.org.ElearnigProject.Model.Video;
import com.org.ElearnigProject.dto.VideoDTO;
import com.org.ElearnigProject.enums.Role;
import com.org.ElearnigProject.repository.VideoAccessRepository;

public class VideoMapper {

    public static VideoDTO mapToDTO(Video video, User currentUser, VideoAccessRepository videoAccessRepository) {
        if (video == null) {
            return null;
        }
        
        boolean hasAccess = false;
        
        // Check if user has access to the video
        if (currentUser != null) {
            // Admins have access to all videos
            if (currentUser.getRole() == Role.ADMIN) {
                hasAccess = true;
            } 
            // Free videos are accessible to all authenticated users
            else if (!video.isPaid()) {
                hasAccess = true;
            } 
            // For paid videos, check if user has been granted access
            else {
                hasAccess = videoAccessRepository.existsByUserAndVideoAndIsActive(currentUser, video, true);
            }
        }
        
        return VideoDTO.builder()
                .id(video.getId())
                .title(video.getTitle())
                .description(video.getDescription())
                .youtubeLink(hasAccess ? video.getYoutubeLink() : null) // Only include link if user has access
                .thumbnailUrl(video.getThumbnailUrl())
                .isPaid(video.isPaid())
                .isVisible(video.isVisible())
                .createdAt(video.getCreatedAt())
                .updatedAt(video.getUpdatedAt())
                .category(CategoryMapper.mapToDTO(video.getCategory()))
                .hasAccess(hasAccess)
                .build();
    }
    
    public static Video mapToEntity(VideoDTO dto) {
        if (dto == null) {
            return null;
        }
        
        return Video.builder()
                .id(dto.getId())
                .title(dto.getTitle())
                .description(dto.getDescription())
                .youtubeLink(dto.getYoutubeLink())
                .thumbnailUrl(dto.getThumbnailUrl())
                .isPaid(dto.isPaid())
                .isVisible(dto.isVisible())
                .createdAt(dto.getCreatedAt())
                .updatedAt(dto.getUpdatedAt())
                .category(CategoryMapper.mapToEntity(dto.getCategory()))
                .build();
    }
}