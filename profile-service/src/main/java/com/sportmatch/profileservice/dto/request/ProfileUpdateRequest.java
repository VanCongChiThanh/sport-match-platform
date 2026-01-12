package com.sportmatch.profileservice.dto.request;

import jakarta.validation.constraints.Size;

/**
 * DTO for updating profile information.
 * All fields are optional since this is a PATCH operation.
 */
public record ProfileUpdateRequest(
        @Size(max = 255) String fullName,
        @Size(max = 50) String phoneNumber,
        @Size(max = 1000) String bio,
        @Size(max = 500) String profilePictureUrl,
        @Size(max = 500) String coverPictureUrl,
        @Size(max = 255) String location
) {
}
