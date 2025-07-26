package com.org.ElearnigProject.services;

import com.org.ElearnigProject.Model.User;
import com.org.ElearnigProject.dto.VideoAccessDTO;

import java.util.List;
import java.util.UUID;

public interface VideoAccessService {
    List<VideoAccessDTO> getUserVideoAccess(User user);
    
    VideoAccessDTO getVideoAccessById(UUID id);
    
    boolean revokeAccess(UUID id);
    
    boolean grantAccess(UUID userId, UUID videoId);
}