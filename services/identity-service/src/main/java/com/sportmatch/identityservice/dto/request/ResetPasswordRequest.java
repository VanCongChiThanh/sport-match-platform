package com.sportmatch.identityservice.dto.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.sportmatch.identityservice.common.constant.CommonConstant;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.util.UUID;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record ResetPasswordRequest(
        @NotBlank
        @Email
        String email,

        @NotBlank
        @Pattern(regexp = CommonConstant.RULE_PASSWORD)
        String password,

        @NotBlank
        @Pattern(regexp = CommonConstant.RULE_PASSWORD)
        String passwordConfirmation,

        @NotNull
        UUID resetPasswordToken
) {}