package com.org.ElearnigProject.services.impl;

import com.org.ElearnigProject.Model.Notification;
import com.org.ElearnigProject.Model.PaymentRequest;
import com.org.ElearnigProject.Model.User;
import com.org.ElearnigProject.dto.NotificationDTO;
import com.org.ElearnigProject.mapper.NotificationMapper;
import com.org.ElearnigProject.enums.Role;
import com.org.ElearnigProject.repository.NotificationRepository;
import com.org.ElearnigProject.repository.UserRepository;
import com.org.ElearnigProject.services.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public NotificationDTO createNotification(User user, String subject, String content, PaymentRequest relatedPaymentRequest) {
        Notification notification = Notification.builder()
                .user(user)
                .subject(subject)
                .content(content)
                .sent(false)
                .createdAt(LocalDateTime.now())
                .relatedPaymentRequest(relatedPaymentRequest)
                .build();
        
        Notification savedNotification = notificationRepository.save(notification);
        return mapToNotificationDTO(savedNotification);
    }

    @Override
    @Transactional
    public void sendNotification(UUID notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Notification not found with id: " + notificationId));
        
        if (notification.isSent()) {
            return; // Already sent
        }
        
        // In a real application, this would send an actual email
        // For now, we'll just mark it as sent
        System.out.println("Sending notification to " + notification.getUser().getEmail() + 
                ": " + notification.getSubject());
        
        // Update notification status
        notification.setSent(true);
        notification.setSentAt(LocalDateTime.now());
        notificationRepository.save(notification);
    }

    @Override
    public void sendAllPendingNotifications() {
        List<Notification> pendingNotifications = notificationRepository.findBySent(false);
        
        for (Notification notification : pendingNotifications) {
            try {
                // In a real application, this would send an actual email
                System.out.println("Sending notification to " + notification.getUser().getEmail() + 
                        ": " + notification.getSubject());
                
                // Update notification status
                notification.setSent(true);
                notification.setSentAt(LocalDateTime.now());
                notificationRepository.save(notification);
            } catch (Exception e) {
                // Log error but continue with next notification
                System.err.println("Failed to send notification: " + e.getMessage());
            }
        }
    }

    @Override
    public Page<NotificationDTO> getUserNotifications(User user, Pageable pageable) {
        return notificationRepository.findByUser(user, pageable)
                .map(this::mapToNotificationDTO);
    }

    @Override
    public List<NotificationDTO> getUnsentNotifications() {
        return notificationRepository.findBySent(false).stream()
                .map(this::mapToNotificationDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void notifyAdminAboutNewPaymentRequest(PaymentRequest paymentRequest) {
        // In a real application, we would find all admin users
        // For now, we'll just log a message
        System.out.println("New payment request from " + paymentRequest.getUser().getEmail() + 
                " for video: " + paymentRequest.getVideo().getTitle());
        
        // We would normally create notifications for all admins here
    }

    @Override
    @Transactional
    public void notifyUserAboutApprovedPaymentRequest(PaymentRequest paymentRequest) {
        String subject = "Payment Request Approved";
        String content = String.format(
                "Your payment request for video '%s' has been approved. You can now access the video.",
                paymentRequest.getVideo().getTitle()
        );
        
        NotificationDTO notification = createNotification(paymentRequest.getUser(), subject, content, paymentRequest);
        sendNotification(notification.getId());
    }

    @Override
    @Transactional
    public void notifyUserAboutRejectedPaymentRequest(PaymentRequest paymentRequest) {
        String subject = "Payment Request Rejected";
        String content = String.format(
                "Your payment request for video '%s' has been rejected. Reason: %s",
                paymentRequest.getVideo().getTitle(),
                paymentRequest.getRejectionReason() != null ? paymentRequest.getRejectionReason() : "No reason provided"
        );
        
        NotificationDTO notification = createNotification(paymentRequest.getUser(), subject, content, paymentRequest);
        sendNotification(notification.getId());
    }
    
    private NotificationDTO mapToNotificationDTO(Notification notification) {
        return NotificationMapper.mapToDTO(notification);
    }
}