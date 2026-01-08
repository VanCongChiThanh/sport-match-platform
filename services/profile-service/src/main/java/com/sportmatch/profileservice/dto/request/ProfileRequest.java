package com.sportmatch.profileservice.dto.request;


import jakarta.validation.constraints.NotBlank;

public record ProfileRequest(
        @NotBlank String username ,
        String fullName,
        String phoneNumber,
        String bio,
        String profilePictureUrl,
        String coverPictureUrl,
        String location
) {
}