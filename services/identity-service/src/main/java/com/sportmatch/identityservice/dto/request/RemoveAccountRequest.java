package com.sportmatch.identityservice.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;


public record RemoveAccountRequest(
        @NotBlank
        @Size(min = 6, max = 6)
        String token
) {}