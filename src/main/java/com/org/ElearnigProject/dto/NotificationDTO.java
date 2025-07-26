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
public class NotificationDTO {
    private UUID id;
    private UUID userId;
    private String userEmail;
    private String subject;
    private String content;
    private boolean sent;
    private LocalDateTime createdAt;
    private LocalDateTime sentAt;
    private UUID relatedPaymentRequestId;
}