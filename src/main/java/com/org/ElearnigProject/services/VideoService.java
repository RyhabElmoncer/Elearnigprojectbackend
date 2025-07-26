package com.org.ElearnigProject.services;

import com.org.ElearnigProject.Model.User;
import com.org.ElearnigProject.dto.VideoDTO;
import com.org.ElearnigProject.dto.request.CreateVideoRequest;
import com.org.ElearnigProject.dto.request.UpdateVideoRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface VideoService {
    Page<VideoDTO> getAllVideos(Pageable pageable, User currentUser);
    
    Page<VideoDTO> getVideosByCategory(UUID categoryId, Pageable pageable, User currentUser);
    
    Page<VideoDTO> searchVideos(String keyword, Pageable pageable, User currentUser);
    
    VideoDTO getVideoById(UUID id, User currentUser);
    
    VideoDTO createVideo(CreateVideoRequest request);
    
    VideoDTO updateVideo(UUID id, UpdateVideoRequest request);
    
    void deleteVideo(UUID id);
    
    Page<VideoDTO> getFreeVideos(Pageable pageable, User currentUser);
    
    Page<VideoDTO> getPaidVideos(Pageable pageable, User currentUser);
    
    void toggleVideoVisibility(UUID id);
}