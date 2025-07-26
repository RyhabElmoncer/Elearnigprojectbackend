package com.org.ElearnigProject.services;

import com.org.ElearnigProject.Model.User;
import com.org.ElearnigProject.dto.PaymentRequestDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public interface PaymentRequestService {
    Page<PaymentRequestDTO> getAllPaymentRequests(Pageable pageable);
    
    Page<PaymentRequestDTO> getUserPaymentRequests(User user, Pageable pageable);
    
    PaymentRequestDTO getPaymentRequestById(UUID id);
    
    PaymentRequestDTO createPaymentRequest(UUID videoId, User user, MultipartFile paymentProof);
    
    PaymentRequestDTO approvePaymentRequest(UUID id);
    
    PaymentRequestDTO rejectPaymentRequest(UUID id, String reason);
    
    boolean hasUserRequestedAccess(UUID videoId, User user);
    
    boolean hasUserAccessToVideo(UUID videoId, User user);
}