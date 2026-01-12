package com.sportmatch.profileservice.controller;

import com.sportmatch.profileservice.dto.request.ProfileRequest;
import com.sportmatch.profileservice.dto.request.ProfileUpdateRequest;
import com.sportmatch.profileservice.dto.response.ProfileResponse;
import com.sportmatch.profileservice.service.ProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/profiles")
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;

    // ==================== USER ENDPOINTS (Authenticated) ====================

    /**
     * Get current user's profile
     */
    @GetMapping("/me")
    public ResponseEntity<ProfileResponse> getMyProfile() {
        return ResponseEntity.ok(profileService.getMyProfile());
    }

    /**
     * Update current user's profile
     */
    @PatchMapping("/me")
    public ResponseEntity<ProfileResponse> updateMyProfile(
            @Valid @RequestBody ProfileUpdateRequest request
    ) {
        return ResponseEntity.ok(profileService.updateMyProfile(request));
    }

    /**
     * Search profiles by username (partial match)
     */
    @GetMapping("/search")
    public ResponseEntity<Page<ProfileResponse>> searchProfiles(
            @RequestParam String username,
            @PageableDefault(size = 20) Pageable pageable
    ) {
        return ResponseEntity.ok(profileService.searchByUsername(username, pageable));
    }

    // ==================== ADMIN ENDPOINTS ====================

    /**
     * Get profile by ID (ADMIN only via security config)
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProfileResponse> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(profileService.getProfileResponseById(id));
    }

    /**
     * Get all profiles with pagination (ADMIN only via security config)
     */
    @GetMapping
    public ResponseEntity<Page<ProfileResponse>> getAllProfiles(
            @PageableDefault(size = 20) Pageable pageable
    ) {
        return ResponseEntity.ok(profileService.getAllProfiles(pageable));
    }

    /**
     * Create a new profile (ADMIN only via security config)
     */
    @PostMapping
    public ResponseEntity<ProfileResponse> create(@Valid @RequestBody ProfileRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(profileService.createProfileFromRequest(request));
    }

    /**
     * Delete a profile by ID (ADMIN only via security config)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProfile(@PathVariable UUID id) {
        profileService.deleteProfile(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Update any profile by ID (ADMIN only via security config)
     */
    @PatchMapping("/{id}")
    public ResponseEntity<ProfileResponse> updateProfileById(
            @PathVariable UUID id,
            @Valid @RequestBody ProfileUpdateRequest request
    ) {
        return ResponseEntity.ok(profileService.updateProfileById(id, request));
    }

    // ==================== INTERNAL ENDPOINTS ====================

    /**
     * Internal endpoint for service-to-service communication
     */
    @GetMapping("/internal/user/{userId}")
    public ResponseEntity<ProfileResponse> getProfileByUserId(@PathVariable UUID userId) {
        return ResponseEntity.ok(profileService.getProfileResponseByUserId(userId));
    }
}