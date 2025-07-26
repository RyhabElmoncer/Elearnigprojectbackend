package com.org.ElearnigProject.mapper;

import com.org.ElearnigProject.Model.Notification;
import com.org.ElearnigProject.dto.NotificationDTO;

public class NotificationMapper {

    public static NotificationDTO mapToDTO(Notification notification) {
        if (notification == null) {
            return null;
        }
        
        return NotificationDTO.builder()
                .id(notification.getId())
                .userId(notification.getUser().getId())
                .userEmail(notification.getUser().getEmail())
                .subject(notification.getSubject())
                .content(notification.getContent())
                .sent(notification.isSent())
                .createdAt(notification.getCreatedAt())
                .sentAt(notification.getSentAt())
                .relatedPaymentRequestId(notification.getRelatedPaymentRequest() != null ? 
                        notification.getRelatedPaymentRequest().getId() : null)
                .build();
    }
}