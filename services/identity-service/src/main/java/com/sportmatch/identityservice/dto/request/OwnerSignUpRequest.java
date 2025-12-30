package com.sportmatch.identityservice.dto.request;


import com.sportmatch.identityservice.constant.CommonConstant;
import jakarta.validation.constraints.*;

import java.util.UUID;

public record OwnerSignUpRequest(
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
        String role,

        @NotNull
        UUID builderId
) {}