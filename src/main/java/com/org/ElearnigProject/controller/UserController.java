package com.org.ElearnigProject.controller;

import com.org.ElearnigProject.Model.User;
import com.org.ElearnigProject.dto.VideoAccessDTO;
import com.org.ElearnigProject.dto.userdto;
import com.org.ElearnigProject.services.VideoAccessService;
import com.org.ElearnigProject.services.userservices;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final userservices userService;
    private final VideoAccessService videoAccessService;

    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<userdto>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN') or #id == authentication.principal.id")
    public ResponseEntity<userdto> getUserById(@PathVariable UUID id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @GetMapping("/profile")
    public ResponseEntity<userdto> getCurrentUserProfile(@AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok(userService.getUserById(currentUser.getId()));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN') or #id == authentication.principal.id")
    public ResponseEntity<userdto> updateUser(
            @PathVariable UUID id,
            @RequestBody userdto userDto) {
        return ResponseEntity.ok(userService.updateUser(id, userDto));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN') or #id == authentication.principal.id")
    public ResponseEntity<Void> deleteUser(@PathVariable UUID id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/block")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<userdto> blockUser(@PathVariable UUID id) {
        return ResponseEntity.ok(userService.blockUser(id));
    }

    @PutMapping("/{id}/unblock")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<userdto> unblockUser(@PathVariable UUID id) {
        return ResponseEntity.ok(userService.unblockUser(id));
    }

    @GetMapping("/my-video-access")
    public ResponseEntity<List<VideoAccessDTO>> getUserVideoAccess(@AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok(videoAccessService.getUserVideoAccess(currentUser));
    }
}