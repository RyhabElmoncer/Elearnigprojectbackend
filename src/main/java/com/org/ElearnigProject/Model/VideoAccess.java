package com.org.ElearnigProject.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "video_access", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"user_id", "video_id"})
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VideoAccess {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "video_id", nullable = false)
    private Video video;

    private LocalDateTime grantedAt;

    @ManyToOne
    @JoinColumn(name = "payment_request_id")
    private PaymentRequest paymentRequest;

    private boolean isActive;
}