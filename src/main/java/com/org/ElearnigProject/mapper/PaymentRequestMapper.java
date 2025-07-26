package com.org.ElearnigProject.mapper;

import com.org.ElearnigProject.Model.PaymentRequest;
import com.org.ElearnigProject.dto.PaymentRequestDTO;

public class PaymentRequestMapper {

    public static PaymentRequestDTO mapToDTO(PaymentRequest paymentRequest) {
        if (paymentRequest == null) {
            return null;
        }
        
        return PaymentRequestDTO.builder()
                .id(paymentRequest.getId())
                .userId(paymentRequest.getUser().getId())
                .userEmail(paymentRequest.getUser().getEmail())
                .userName(paymentRequest.getUser().getFirstName() + " " + paymentRequest.getUser().getLastName())
                .videoId(paymentRequest.getVideo().getId())
                .videoTitle(paymentRequest.getVideo().getTitle())
                .paymentProofUrl(paymentRequest.getPaymentProofUrl())
                .status(paymentRequest.getStatus())
                .rejectionReason(paymentRequest.getRejectionReason())
                .requestDate(paymentRequest.getRequestDate())
                .processedDate(paymentRequest.getProcessedDate())
                .build();
    }
}