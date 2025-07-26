package com.org.ElearnigProject.repository;

import com.org.ElearnigProject.Model.PaymentRequest;
import com.org.ElearnigProject.Model.User;
import com.org.ElearnigProject.Model.Video;
import com.org.ElearnigProject.enums.PaymentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PaymentRequestRepository extends JpaRepository<PaymentRequest, UUID> {
    List<PaymentRequest> findByUser(User user);
    
    Page<PaymentRequest> findByUser(User user, Pageable pageable);
    
    List<PaymentRequest> findByStatus(PaymentStatus status);
    
    Page<PaymentRequest> findByStatus(PaymentStatus status, Pageable pageable);
    
    Optional<PaymentRequest> findByUserAndVideoAndStatus(User user, Video video, PaymentStatus status);
    
    boolean existsByUserAndVideoAndStatus(User user, Video video, PaymentStatus status);
    
    List<PaymentRequest> findByUserAndVideo(User user, Video video);
}