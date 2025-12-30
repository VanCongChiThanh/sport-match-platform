package com.sportmatch.identityservice.dto.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.sportmatch.identityservice.common.constant.CommonConstant;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record SignUpRequest(
        @NotBlank
        @Email
        String email,

        @NotBlank
        @Size(min = 1, max = 100)
        String firstname,

        @NotBlank
        @Size(min = 1, max = 100)
        String lastname,

        @NotBlank
        @Pattern(regexp = CommonConstant.RULE_PASSWORD)
        String password,

        @NotBlank
        String passwordConfirmation,

        @NotBlank
        @Pattern(regexp = CommonConstant.RULE_ROLE)
        String role
) {}