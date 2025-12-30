package com.sportmatch.identityservice.dto.request;


import com.sportmatch.identityservice.constant.CommonConstant;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;


public record ChangePasswordRequest(
        @NotBlank
        String oldPassword,

        @NotBlank
        @Pattern(regexp = CommonConstant.RULE_PASSWORD)
        String newPassword,

        @NotBlank
        @Pattern(regexp = CommonConstant.RULE_PASSWORD)
        String confirmNewPassword
) {}