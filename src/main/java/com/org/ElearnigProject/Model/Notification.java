package com.org.ElearnigProject.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "notifications")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Notification {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String subject;

    @Column(nullable = false, length = 2000)
    private String content;

    @Column(nullable = false)
    private boolean sent;

    private LocalDateTime createdAt;

    private LocalDateTime sentAt;

    @ManyToOne
    @JoinColumn(name = "payment_request_id")
    private PaymentRequest relatedPaymentRequest;
}