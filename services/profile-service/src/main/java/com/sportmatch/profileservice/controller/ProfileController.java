package com.sportmatch.profileservice.controller;

import com.sportmatch.profileservice.dto.request.ProfileRequest;
import com.sportmatch.profileservice.dto.response.ProfileResponse;
import com.sportmatch.profileservice.dto.general.ResponseDataAPI;
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
    public ResponseEntity<ResponseDataAPI> getById(@PathVariable UUID id) {
        ProfileResponse profileResponse = profileService.getProfileResponseById(id);
        return ResponseEntity.ok(
                ResponseDataAPI.successWithoutMeta(profileResponse)
        );
    }

    @PostMapping
    public ResponseEntity<ResponseDataAPI> create(@RequestBody ProfileRequest request) {
        ProfileResponse created = profileService.createProfileFromRequest(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ResponseDataAPI.successWithoutMeta(created));
    }

    @PatchMapping("/profiles/{id}")
    public ResponseEntity<ResponseDataAPI> updateProfile(
            @PathVariable UUID id,
            @RequestBody ProfileRequest request
    ) {
        ProfileResponse updated = profileService.updateProfileFromRequest(id, request);
        return ResponseEntity.ok(
                ResponseDataAPI.successWithoutMeta(updated)
        );
    }

}