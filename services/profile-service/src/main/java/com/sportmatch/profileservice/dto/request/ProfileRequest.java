package com.sportmatch.profileservice.dto.request;

public record ProfileRequest(
        String username,
        String fullName,
        String phoneNumber,
        String bio,
        String profilePictureUrl,
        String coverPictureUrl,
        String location
) {
}