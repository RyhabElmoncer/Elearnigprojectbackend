package com.org.ElearnigProject.repository;

import com.org.ElearnigProject.Model.User;
import com.org.ElearnigProject.Model.Video;
import com.org.ElearnigProject.Model.VideoAccess;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface VideoAccessRepository extends JpaRepository<VideoAccess, UUID> {
    List<VideoAccess> findByUser(User user);
    
    List<VideoAccess> findByVideo(Video video);
    
    Optional<VideoAccess> findByUserAndVideo(User user, Video video);
    
    boolean existsByUserAndVideoAndIsActive(User user, Video video, boolean isActive);
    
    List<VideoAccess> findByUserAndIsActive(User user, boolean isActive);
}