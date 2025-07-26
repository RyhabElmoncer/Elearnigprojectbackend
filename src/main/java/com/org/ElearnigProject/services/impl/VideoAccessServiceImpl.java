package com.org.ElearnigProject.services.impl;

import com.org.ElearnigProject.Model.User;
import com.org.ElearnigProject.Model.Video;
import com.org.ElearnigProject.Model.VideoAccess;
import com.org.ElearnigProject.dto.VideoAccessDTO;
import com.org.ElearnigProject.mapper.VideoAccessMapper;
import com.org.ElearnigProject.repository.UserRepository;
import com.org.ElearnigProject.repository.VideoAccessRepository;
import com.org.ElearnigProject.repository.VideoRepository;
import com.org.ElearnigProject.services.VideoAccessService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VideoAccessServiceImpl implements VideoAccessService {

    private final VideoAccessRepository videoAccessRepository;
    private final UserRepository userRepository;
    private final VideoRepository videoRepository;

    @Override
    public List<VideoAccessDTO> getUserVideoAccess(User user) {
        List<VideoAccess> videoAccesses = videoAccessRepository.findByUserAndIsActive(user, true);
        return videoAccesses.stream()
                .map(VideoAccessMapper::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public VideoAccessDTO getVideoAccessById(UUID id) {
        VideoAccess videoAccess = videoAccessRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Video access not found with id: " + id));
        return VideoAccessMapper.mapToDTO(videoAccess);
    }

    @Override
    @Transactional
    public boolean revokeAccess(UUID id) {
        VideoAccess videoAccess = videoAccessRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Video access not found with id: " + id));
        
        videoAccess.setActive(false);
        videoAccessRepository.save(videoAccess);
        return true;
    }

    @Override
    @Transactional
    public boolean grantAccess(UUID userId, UUID videoId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        
        Video video = videoRepository.findById(videoId)
                .orElseThrow(() -> new RuntimeException("Video not found with id: " + videoId));
        
        // Check if access already exists
        if (videoAccessRepository.existsByUserAndVideoAndIsActive(user, video, true)) {
            return false; // Already has access
        }
        
        VideoAccess videoAccess = VideoAccess.builder()
                .user(user)
                .video(video)
                .grantedAt(LocalDateTime.now())
                .isActive(true)
                .build();
        
        videoAccessRepository.save(videoAccess);
        return true;
    }
}