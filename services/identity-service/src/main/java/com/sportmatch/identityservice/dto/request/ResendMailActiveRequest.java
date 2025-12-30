package com.sportmatch.identityservice.dto.request;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record ResendMailActiveRequest(
        @NotBlank
        @Email
        String email
) {}