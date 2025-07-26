package com.org.ElearnigProject.controller;

import com.org.ElearnigProject.Model.User;
import com.org.ElearnigProject.dto.NotificationDTO;
import com.org.ElearnigProject.services.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping("/my-notifications")
    public ResponseEntity<Page<NotificationDTO>> getUserNotifications(
            Pageable pageable,
            @AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok(notificationService.getUserNotifications(currentUser, pageable));
    }

    @GetMapping("/unsent")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<NotificationDTO>> getUnsentNotifications() {
        return ResponseEntity.ok(notificationService.getUnsentNotifications());
    }

    @PostMapping("/send/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Void> sendNotification(@PathVariable UUID id) {
        notificationService.sendNotification(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/send-all-pending")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Void> sendAllPendingNotifications() {
        notificationService.sendAllPendingNotifications();
        return ResponseEntity.ok().build();
    }
}