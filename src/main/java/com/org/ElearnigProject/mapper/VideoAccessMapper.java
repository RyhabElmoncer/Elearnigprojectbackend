package com.org.ElearnigProject.mapper;

import com.org.ElearnigProject.Model.VideoAccess;
import com.org.ElearnigProject.dto.VideoAccessDTO;

public class VideoAccessMapper {

    public static VideoAccessDTO mapToDTO(VideoAccess videoAccess) {
        if (videoAccess == null) {
            return null;
        }
        
        return VideoAccessDTO.builder()
                .id(videoAccess.getId())
                .userId(videoAccess.getUser().getId())
                .userEmail(videoAccess.getUser().getEmail())
                .videoId(videoAccess.getVideo().getId())
                .videoTitle(videoAccess.getVideo().getTitle())
                .grantedAt(videoAccess.getGrantedAt())
                .paymentRequestId(videoAccess.getPaymentRequest() != null ? 
                        videoAccess.getPaymentRequest().getId() : null)
                .isActive(videoAccess.isActive())
                .build();
    }
}