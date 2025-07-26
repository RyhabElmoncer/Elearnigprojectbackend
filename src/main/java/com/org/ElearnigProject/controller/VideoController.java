package com.org.ElearnigProject.controller;

import com.org.ElearnigProject.Model.User;
import com.org.ElearnigProject.dto.VideoDTO;
import com.org.ElearnigProject.dto.request.CreateVideoRequest;
import com.org.ElearnigProject.dto.request.UpdateVideoRequest;
import com.org.ElearnigProject.services.VideoService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/videos")
@RequiredArgsConstructor
public class VideoController {

    private final VideoService videoService;

    @GetMapping
    public ResponseEntity<Page<VideoDTO>> getAllVideos(
            Pageable pageable,
            @AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok(videoService.getAllVideos(pageable, currentUser));
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<Page<VideoDTO>> getVideosByCategory(
            @PathVariable UUID categoryId,
            Pageable pageable,
            @AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok(videoService.getVideosByCategory(categoryId, pageable, currentUser));
    }

    @GetMapping("/search")
    public ResponseEntity<Page<VideoDTO>> searchVideos(
            @RequestParam String keyword,
            Pageable pageable,
            @AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok(videoService.searchVideos(keyword, pageable, currentUser));
    }

    @GetMapping("/free")
    public ResponseEntity<Page<VideoDTO>> getFreeVideos(
            Pageable pageable,
            @AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok(videoService.getFreeVideos(pageable, currentUser));
    }

    @GetMapping("/paid")
    public ResponseEntity<Page<VideoDTO>> getPaidVideos(
            Pageable pageable,
            @AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok(videoService.getPaidVideos(pageable, currentUser));
    }

    @GetMapping("/{id}")
    public ResponseEntity<VideoDTO> getVideoById(
            @PathVariable UUID id,
            @AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok(videoService.getVideoById(id, currentUser));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<VideoDTO> createVideo(@RequestBody CreateVideoRequest request) {
        return new ResponseEntity<>(videoService.createVideo(request), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<VideoDTO> updateVideo(
            @PathVariable UUID id,
            @RequestBody UpdateVideoRequest request) {
        return ResponseEntity.ok(videoService.updateVideo(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Void> deleteVideo(@PathVariable UUID id) {
        videoService.deleteVideo(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/toggle-visibility")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Void> toggleVideoVisibility(@PathVariable UUID id) {
        videoService.toggleVideoVisibility(id);
        return ResponseEntity.ok().build();
    }
}