package com.org.ElearnigProject.services.impl;

import com.org.ElearnigProject.Model.Category;
import com.org.ElearnigProject.Model.User;
import com.org.ElearnigProject.Model.Video;
import com.org.ElearnigProject.Model.VideoAccess;
import com.org.ElearnigProject.dto.CategoryDTO;
import com.org.ElearnigProject.dto.VideoDTO;
import com.org.ElearnigProject.mapper.CategoryMapper;
import com.org.ElearnigProject.mapper.VideoMapper;
import com.org.ElearnigProject.dto.request.CreateVideoRequest;
import com.org.ElearnigProject.dto.request.UpdateVideoRequest;
import com.org.ElearnigProject.enums.Role;
import com.org.ElearnigProject.repository.CategoryRepository;
import com.org.ElearnigProject.repository.VideoAccessRepository;
import com.org.ElearnigProject.repository.VideoRepository;
import com.org.ElearnigProject.services.VideoService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VideoServiceImpl implements VideoService {

    private final VideoRepository videoRepository;
    private final CategoryRepository categoryRepository;
    private final VideoAccessRepository videoAccessRepository;

    @Override
    public Page<VideoDTO> getAllVideos(Pageable pageable, User currentUser) {
        Page<Video> videos;
        
        if (currentUser != null && currentUser.getRole() == Role.ADMIN) {
            // Admins can see all videos
            videos = videoRepository.findAll(pageable);
        } else {
            // Non-admins can only see visible videos
            videos = videoRepository.findByIsVisible(true, pageable);
        }
        
        return videos.map(video -> mapToVideoDTO(video, currentUser));
    }

    @Override
    public Page<VideoDTO> getVideosByCategory(UUID categoryId, Pageable pageable, User currentUser) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + categoryId));
        
        Page<Video> videos;
        
        if (currentUser != null && currentUser.getRole() == Role.ADMIN) {
            // Admins can see all videos in the category
            videos = videoRepository.findByCategoryAndIsVisible(category, true, pageable);
        } else {
            // Non-admins can only see visible videos in the category
            videos = videoRepository.findByCategoryAndIsVisible(category, true, pageable);
        }
        
        return videos.map(video -> mapToVideoDTO(video, currentUser));
    }

    @Override
    public Page<VideoDTO> searchVideos(String keyword, Pageable pageable, User currentUser) {
        Page<Video> videos = videoRepository.searchVideos(keyword, pageable);
        return videos.map(video -> mapToVideoDTO(video, currentUser));
    }

    @Override
    public VideoDTO getVideoById(UUID id, User currentUser) {
        Video video = videoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Video not found with id: " + id));
        
        // Check if the video is visible or if the user is an admin
        if (!video.isVisible() && (currentUser == null || currentUser.getRole() != Role.ADMIN)) {
            throw new RuntimeException("Video not available");
        }
        
        return mapToVideoDTO(video, currentUser);
    }

    @Override
    @Transactional
    public VideoDTO createVideo(CreateVideoRequest request) {
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + request.getCategoryId()));
        
        Video video = Video.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .youtubeLink(request.getYoutubeLink())
                .thumbnailUrl(request.getThumbnailUrl())
                .isPaid(request.getIsPaid())
                .isVisible(request.getIsVisible())
                .category(category)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        
        Video savedVideo = videoRepository.save(video);
        return mapToVideoDTO(savedVideo, null);
    }

    @Override
    @Transactional
    public VideoDTO updateVideo(UUID id, UpdateVideoRequest request) {
        Video video = videoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Video not found with id: " + id));
        
        if (request.getTitle() != null) {
            video.setTitle(request.getTitle());
        }
        
        if (request.getDescription() != null) {
            video.setDescription(request.getDescription());
        }
        
        if (request.getYoutubeLink() != null) {
            video.setYoutubeLink(request.getYoutubeLink());
        }
        
        if (request.getThumbnailUrl() != null) {
            video.setThumbnailUrl(request.getThumbnailUrl());
        }
        
        if (request.getIsPaid() != null) {
            video.setPaid(request.getIsPaid());
        }
        
        if (request.getIsVisible() != null) {
            video.setVisible(request.getIsVisible());
        }
        
        if (request.getCategoryId() != null) {
            Category category = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new RuntimeException("Category not found with id: " + request.getCategoryId()));
            video.setCategory(category);
        }
        
        video.setUpdatedAt(LocalDateTime.now());
        
        Video updatedVideo = videoRepository.save(video);
        return mapToVideoDTO(updatedVideo, null);
    }

    @Override
    @Transactional
    public void deleteVideo(UUID id) {
        if (!videoRepository.existsById(id)) {
            throw new RuntimeException("Video not found with id: " + id);
        }
        videoRepository.deleteById(id);
    }

    @Override
    public Page<VideoDTO> getFreeVideos(Pageable pageable, User currentUser) {
        Page<Video> videos = videoRepository.findByIsPaidAndVisible(false, pageable);
        return videos.map(video -> mapToVideoDTO(video, currentUser));
    }

    @Override
    public Page<VideoDTO> getPaidVideos(Pageable pageable, User currentUser) {
        Page<Video> videos = videoRepository.findByIsPaidAndVisible(true, pageable);
        return videos.map(video -> mapToVideoDTO(video, currentUser));
    }

    @Override
    @Transactional
    public void toggleVideoVisibility(UUID id) {
        Video video = videoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Video not found with id: " + id));
        
        video.setVisible(!video.isVisible());
        video.setUpdatedAt(LocalDateTime.now());
        
        videoRepository.save(video);
    }
    
    private VideoDTO mapToVideoDTO(Video video, User currentUser) {
        return VideoMapper.mapToDTO(video, currentUser, videoAccessRepository);
    }
    
    private CategoryDTO mapToCategoryDTO(Category category) {
        return CategoryMapper.mapToDTO(category);
    }
}