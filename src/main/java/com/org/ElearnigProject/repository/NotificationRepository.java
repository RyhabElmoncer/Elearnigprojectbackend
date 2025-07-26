package com.org.ElearnigProject.repository;

import com.org.ElearnigProject.Model.Notification;
import com.org.ElearnigProject.Model.PaymentRequest;
import com.org.ElearnigProject.Model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, UUID> {
    List<Notification> findByUser(User user);
    
    Page<Notification> findByUser(User user, Pageable pageable);
    
    List<Notification> findByRelatedPaymentRequest(PaymentRequest paymentRequest);
    
    List<Notification> findBySent(boolean sent);
}