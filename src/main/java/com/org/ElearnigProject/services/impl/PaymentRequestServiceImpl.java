package com.org.ElearnigProject.services.impl;

import com.org.ElearnigProject.Model.PaymentRequest;
import com.org.ElearnigProject.Model.User;
import com.org.ElearnigProject.Model.Video;
import com.org.ElearnigProject.Model.VideoAccess;
import com.org.ElearnigProject.dto.PaymentRequestDTO;
import com.org.ElearnigProject.mapper.PaymentRequestMapper;
import com.org.ElearnigProject.enums.PaymentStatus;
import com.org.ElearnigProject.repository.PaymentRequestRepository;
import com.org.ElearnigProject.repository.VideoAccessRepository;
import com.org.ElearnigProject.repository.VideoRepository;
import com.org.ElearnigProject.services.NotificationService;
import com.org.ElearnigProject.services.PaymentRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentRequestServiceImpl implements PaymentRequestService {

    private final PaymentRequestRepository paymentRequestRepository;
    private final VideoRepository videoRepository;
    private final VideoAccessRepository videoAccessRepository;
    private final NotificationService notificationService;
    
    private final String UPLOAD_DIR = "payment-proofs";

    @Override
    public Page<PaymentRequestDTO> getAllPaymentRequests(Pageable pageable) {
        return paymentRequestRepository.findAll(pageable)
                .map(this::mapToPaymentRequestDTO);
    }

    @Override
    public Page<PaymentRequestDTO> getUserPaymentRequests(User user, Pageable pageable) {
        return paymentRequestRepository.findByUser(user, pageable)
                .map(this::mapToPaymentRequestDTO);
    }

    @Override
    public PaymentRequestDTO getPaymentRequestById(UUID id) {
        PaymentRequest paymentRequest = paymentRequestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Payment request not found with id: " + id));
        return mapToPaymentRequestDTO(paymentRequest);
    }

    @Override
    @Transactional
    public PaymentRequestDTO createPaymentRequest(UUID videoId, User user, MultipartFile paymentProof) {
        Video video = videoRepository.findById(videoId)
                .orElseThrow(() -> new RuntimeException("Video not found with id: " + videoId));
        
        // Check if video is paid
        if (!video.isPaid()) {
            throw new RuntimeException("Cannot request access to a free video");
        }
        
        // Check if user already has access
        if (videoAccessRepository.existsByUserAndVideoAndIsActive(user, video, true)) {
            throw new RuntimeException("You already have access to this video");
        }
        
        // Check if there's a pending request
        if (paymentRequestRepository.existsByUserAndVideoAndStatus(user, video, PaymentStatus.PENDING)) {
            throw new RuntimeException("You already have a pending request for this video");
        }
        
        // Save payment proof file
        String paymentProofUrl = null;
        if (paymentProof != null && !paymentProof.isEmpty()) {
            try {
                // Create directory if it doesn't exist
                Path uploadPath = Paths.get(UPLOAD_DIR);
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }
                
                // Generate unique filename
                String filename = UUID.randomUUID() + "_" + paymentProof.getOriginalFilename();
                Path filePath = uploadPath.resolve(filename);
                
                // Save file
                Files.copy(paymentProof.getInputStream(), filePath);
                paymentProofUrl = filePath.toString();
            } catch (IOException e) {
                throw new RuntimeException("Failed to store payment proof", e);
            }
        }
        
        // Create payment request
        PaymentRequest paymentRequest = PaymentRequest.builder()
                .user(user)
                .video(video)
                .paymentProofUrl(paymentProofUrl)
                .status(PaymentStatus.PENDING)
                .requestDate(LocalDateTime.now())
                .build();
        
        PaymentRequest savedRequest = paymentRequestRepository.save(paymentRequest);
        
        // Notify admin about new payment request
        notificationService.notifyAdminAboutNewPaymentRequest(savedRequest);
        
        return mapToPaymentRequestDTO(savedRequest);
    }

    @Override
    @Transactional
    public PaymentRequestDTO approvePaymentRequest(UUID id) {
        PaymentRequest paymentRequest = paymentRequestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Payment request not found with id: " + id));
        
        if (paymentRequest.getStatus() != PaymentStatus.PENDING) {
            throw new RuntimeException("Cannot approve a request that is not pending");
        }
        
        // Update payment request status
        paymentRequest.setStatus(PaymentStatus.APPROVED);
        paymentRequest.setProcessedDate(LocalDateTime.now());
        
        // Grant access to the video
        VideoAccess videoAccess = VideoAccess.builder()
                .user(paymentRequest.getUser())
                .video(paymentRequest.getVideo())
                .grantedAt(LocalDateTime.now())
                .paymentRequest(paymentRequest)
                .isActive(true)
                .build();
        
        videoAccessRepository.save(videoAccess);
        
        // Save updated payment request
        PaymentRequest updatedRequest = paymentRequestRepository.save(paymentRequest);
        
        // Notify user about approved request
        notificationService.notifyUserAboutApprovedPaymentRequest(updatedRequest);
        
        return mapToPaymentRequestDTO(updatedRequest);
    }

    @Override
    @Transactional
    public PaymentRequestDTO rejectPaymentRequest(UUID id, String reason) {
        PaymentRequest paymentRequest = paymentRequestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Payment request not found with id: " + id));
        
        if (paymentRequest.getStatus() != PaymentStatus.PENDING) {
            throw new RuntimeException("Cannot reject a request that is not pending");
        }
        
        // Update payment request status
        paymentRequest.setStatus(PaymentStatus.REJECTED);
        paymentRequest.setRejectionReason(reason);
        paymentRequest.setProcessedDate(LocalDateTime.now());
        
        // Save updated payment request
        PaymentRequest updatedRequest = paymentRequestRepository.save(paymentRequest);
        
        // Notify user about rejected request
        notificationService.notifyUserAboutRejectedPaymentRequest(updatedRequest);
        
        return mapToPaymentRequestDTO(updatedRequest);
    }

    @Override
    public boolean hasUserRequestedAccess(UUID videoId, User user) {
        Video video = videoRepository.findById(videoId)
                .orElseThrow(() -> new RuntimeException("Video not found with id: " + videoId));
        
        return paymentRequestRepository.existsByUserAndVideoAndStatus(user, video, PaymentStatus.PENDING) ||
               paymentRequestRepository.existsByUserAndVideoAndStatus(user, video, PaymentStatus.APPROVED);
    }

    @Override
    public boolean hasUserAccessToVideo(UUID videoId, User user) {
        Video video = videoRepository.findById(videoId)
                .orElseThrow(() -> new RuntimeException("Video not found with id: " + videoId));
        
        // Free videos are accessible to all authenticated users
        if (!video.isPaid()) {
            return true;
        }
        
        // For paid videos, check if user has been granted access
        return videoAccessRepository.existsByUserAndVideoAndIsActive(user, video, true);
    }
    
    private PaymentRequestDTO mapToPaymentRequestDTO(PaymentRequest paymentRequest) {
        return PaymentRequestMapper.mapToDTO(paymentRequest);
    }
}