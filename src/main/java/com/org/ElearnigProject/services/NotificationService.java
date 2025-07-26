package com.org.ElearnigProject.services;

import com.org.ElearnigProject.Model.PaymentRequest;
import com.org.ElearnigProject.Model.User;
import com.org.ElearnigProject.dto.NotificationDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface NotificationService {
    NotificationDTO createNotification(User user, String subject, String content, PaymentRequest relatedPaymentRequest);
    
    void sendNotification(UUID notificationId);
    
    void sendAllPendingNotifications();
    
    Page<NotificationDTO> getUserNotifications(User user, Pageable pageable);
    
    List<NotificationDTO> getUnsentNotifications();
    
    void notifyAdminAboutNewPaymentRequest(PaymentRequest paymentRequest);
    
    void notifyUserAboutApprovedPaymentRequest(PaymentRequest paymentRequest);
    
    void notifyUserAboutRejectedPaymentRequest(PaymentRequest paymentRequest);
}