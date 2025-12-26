package com.sportmatch.profileservice.controller;

import com.sportmatch.profileservice.dto.ProfileRequest;
import com.sportmatch.profileservice.dto.ProfileResponse;
import com.sportmatch.profileservice.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/profiles")
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;

    @GetMapping("/{id}")
    public ResponseEntity<ProfileResponse> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(profileService.getProfileResponseById(id));
    }

    @PostMapping
    public ResponseEntity<ProfileResponse> create(@RequestBody ProfileRequest request) {
        ProfileResponse created = profileService.createProfileFromRequest(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PatchMapping("/profiles/{id}")
    public ResponseEntity<ProfileResponse> updateProfile(
            @PathVariable UUID id,
            @RequestBody ProfileRequest request
    ) {
        return ResponseEntity.ok(
                profileService.updateProfileFromRequest(id, request)
        );
    }

}