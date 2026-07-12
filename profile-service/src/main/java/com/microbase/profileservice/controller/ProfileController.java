package com.microbase.profileservice.controller;

import com.microbase.commonlibrary.dto.PageResponse;
import com.microbase.commonlibrary.security.SecurityExpressions;
import com.microbase.profileservice.dto.request.ProfileRequest;
import com.microbase.profileservice.dto.request.ProfileUpdateRequest;
import com.microbase.profileservice.dto.response.ProfileResponse;
import com.microbase.profileservice.service.ProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/profiles")
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;

    @PreAuthorize(SecurityExpressions.USER_OR_ADMIN)
    @GetMapping("/me")
    public ResponseEntity<ProfileResponse> getMyProfile() {
        return ResponseEntity.ok(profileService.getMyProfile());
    }

    @PreAuthorize(SecurityExpressions.USER_OR_ADMIN)
    @PatchMapping("/me")
    public ResponseEntity<ProfileResponse> updateMyProfile(
            @Valid @RequestBody ProfileUpdateRequest request
    ) {
        return ResponseEntity.ok(profileService.updateMyProfile(request));
    }

    @PreAuthorize(SecurityExpressions.USER_OR_ADMIN)
    @GetMapping("/search")
    public ResponseEntity<PageResponse<ProfileResponse>> searchProfiles(
            @RequestParam String username,
            @PageableDefault(size = 20) Pageable pageable
    ) {
        return ResponseEntity.ok(PageResponse.from(profileService.searchByUsername(username, pageable)));
    }

    @PreAuthorize(SecurityExpressions.IS_ADMIN)
    @GetMapping("/{id}")
    public ResponseEntity<ProfileResponse> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(profileService.getProfileResponseById(id));
    }

    @PreAuthorize(SecurityExpressions.IS_ADMIN)
    @GetMapping
    public ResponseEntity<PageResponse<ProfileResponse>> getAllProfiles(
            @PageableDefault(size = 20) Pageable pageable
    ) {
        return ResponseEntity.ok(PageResponse.from(profileService.getAllProfiles(pageable)));
    }

    @PreAuthorize(SecurityExpressions.USER_OR_ADMIN)
    @PostMapping
    public ResponseEntity<ProfileResponse> create(@Valid @RequestBody ProfileRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(profileService.createProfileFromRequest(request));
    }

    @PreAuthorize(SecurityExpressions.IS_ADMIN)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProfile(@PathVariable UUID id) {
        profileService.deleteProfile(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize(SecurityExpressions.IS_ADMIN)
    @PatchMapping("/{id}")
    public ResponseEntity<ProfileResponse> updateProfileById(
            @PathVariable UUID id,
            @Valid @RequestBody ProfileUpdateRequest request
    ) {
        return ResponseEntity.ok(profileService.updateProfileById(id, request));
    }

    @PreAuthorize(SecurityExpressions.SERVICE_OR_ADMIN)
    @GetMapping("/internal/user/{userId}")
    public ResponseEntity<ProfileResponse> getProfileByUserId(@PathVariable UUID userId) {
        return ResponseEntity.ok(profileService.getProfileResponseByUserId(userId));
    }
}