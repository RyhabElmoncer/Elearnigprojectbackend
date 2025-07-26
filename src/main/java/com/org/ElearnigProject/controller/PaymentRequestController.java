package com.org.ElearnigProject.controller;

import com.org.ElearnigProject.Model.User;
import com.org.ElearnigProject.dto.PaymentRequestDTO;
import com.org.ElearnigProject.services.PaymentRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/payment-requests")
@RequiredArgsConstructor
public class PaymentRequestController {

    private final PaymentRequestService paymentRequestService;

    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Page<PaymentRequestDTO>> getAllPaymentRequests(Pageable pageable) {
        return ResponseEntity.ok(paymentRequestService.getAllPaymentRequests(pageable));
    }

    @GetMapping("/my-requests")
    public ResponseEntity<Page<PaymentRequestDTO>> getUserPaymentRequests(
            Pageable pageable,
            @AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok(paymentRequestService.getUserPaymentRequests(currentUser, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentRequestDTO> getPaymentRequestById(@PathVariable UUID id) {
        return ResponseEntity.ok(paymentRequestService.getPaymentRequestById(id));
    }

    @PostMapping
    public ResponseEntity<PaymentRequestDTO> createPaymentRequest(
            @RequestParam UUID videoId,
            @RequestParam(required = false) MultipartFile paymentProof,
            @AuthenticationPrincipal User currentUser) {
        return new ResponseEntity<>(
                paymentRequestService.createPaymentRequest(videoId, currentUser, paymentProof),
                HttpStatus.CREATED);
    }

    @PutMapping("/{id}/approve")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<PaymentRequestDTO> approvePaymentRequest(@PathVariable UUID id) {
        return ResponseEntity.ok(paymentRequestService.approvePaymentRequest(id));
    }

    @PutMapping("/{id}/reject")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<PaymentRequestDTO> rejectPaymentRequest(
            @PathVariable UUID id,
            @RequestParam String reason) {
        return ResponseEntity.ok(paymentRequestService.rejectPaymentRequest(id, reason));
    }

    @GetMapping("/check-access")
    public ResponseEntity<Boolean> hasUserAccessToVideo(
            @RequestParam UUID videoId,
            @AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok(paymentRequestService.hasUserAccessToVideo(videoId, currentUser));
    }

    @GetMapping("/check-request")
    public ResponseEntity<Boolean> hasUserRequestedAccess(
            @RequestParam UUID videoId,
            @AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok(paymentRequestService.hasUserRequestedAccess(videoId, currentUser));
    }
}