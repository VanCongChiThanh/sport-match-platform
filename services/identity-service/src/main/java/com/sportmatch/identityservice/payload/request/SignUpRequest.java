package com.sportmatch.identityservice.payload.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.pbl.elearning.common.constant.CommonConstant;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@Setter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class SignUpRequest {
  @NotBlank @Email private String email;

  @NotBlank
  @Size(min = 1, max = 100)
  private String firstname;

  @NotBlank
  @Size(min = 1, max = 100)
  private String lastname;

  @NotBlank
  @Pattern(regexp = CommonConstant.RULE_PASSWORD)
  private String password;

  @NotBlank private String passwordConfirmation;

  @NotBlank
  @Pattern(regexp = CommonConstant.RULE_ROLE)
  private String role;
}