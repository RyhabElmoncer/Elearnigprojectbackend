package com.org.ElearnigProject.dto;

import com.org.ElearnigProject.enums.PaymentStatus;
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
public class PaymentRequestDTO {
    private UUID id;
    private UUID userId;
    private String userEmail;
    private String userName;
    private UUID videoId;
    private String videoTitle;
    private String paymentProofUrl;
    private PaymentStatus status;
    private String rejectionReason;
    private LocalDateTime requestDate;
    private LocalDateTime processedDate;
}