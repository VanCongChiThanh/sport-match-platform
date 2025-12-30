package com.sportmatch.identityservice.dto.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;


@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record SignOutAllRequest(
        @NotBlank
        @Email
        String email
) {}