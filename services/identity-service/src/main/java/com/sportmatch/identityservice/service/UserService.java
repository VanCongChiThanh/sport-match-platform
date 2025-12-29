package com.sportmatch.identityservice.service;



import com.sportmatch.identityservice.entity.User;
import com.sportmatch.identityservice.entity.enums.AuthProvider;
import com.sportmatch.identityservice.entity.enums.Role;
import com.sportmatch.identityservice.payload.general.ResponseDataAPI;
import com.sportmatch.identityservice.payload.request.ChangePasswordRequest;
import com.sportmatch.identityservice.payload.request.ForgotPasswordRequest;
import com.sportmatch.identityservice.payload.request.ResetPasswordRequest;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface UserService {

  /**
   * Get user by email
   *
   * @param email User's email
   * @return Optional {@link User}
   */
  Optional<User> findByEmail(String email);

  /**
   * Get User by id
   *
   * @param id User's id
   * @return User
   */
  User findById(UUID id);

  /**
   * Get list User by ids
   *
   * @param ids List id
   * @return User
   */
  Set<User> findByIds(Set<UUID> ids);

  List<UUID> findAllByRole(Role role);

  /**
   * Forgot password of user
   *
   * @param forgotPasswordRequest {@link ForgotPasswordRequest}
   * @return ResponseDataAPI, status code 200
   */
  ResponseDataAPI forgotPassword(ForgotPasswordRequest forgotPasswordRequest);

  /**
   * Reset password of user
   *
   * @param resetPasswordRequest {@link ResetPasswordRequest}
   * @return ResponseDataAPI, status code 200
   */
  ResponseDataAPI resetPassword(ResetPasswordRequest resetPasswordRequest);

  /**
   * Change password of user
   *
   * @param userId User's id
   * @param oldPassword Old password
   * @param changePasswordRequest {@link ChangePasswordRequest}
   * @return ResponseDataAPI, status code 200
   */
  ResponseDataAPI changePassword(
      UUID userId, String oldPassword, ChangePasswordRequest changePasswordRequest);

  /**
   * Register new user
   *
   * @param firstname Firstname
   * @param lastname Lastname
   * @param email Email
   * @param password Password
   * @param role Role
   */
  User registerUser(String firstname, String lastname, String email, String password, Role role);
  User registerUserOauth2(
          String firstname, String lastname, String email, String avatar, AuthProvider provider, String providerId);
  /**
   * Confirm email register
   *
   * @param confirmationToken Token to be confirmed to active account
   * @return ResponseEntity {@link ResponseDataAPI}
   */
  ResponseEntity<ResponseDataAPI> confirmEmailRegister(String confirmationToken);

  /**
   * The admin activates the user
   *
   * @param user {@link User}
   */
  void setActiveUser(User user);

  /**
   * The admin deactivates the user
   *
   * @param user {@link User}
   */
  void deactivateUser(String reason, User user);

  /**
   * Resend mail active account
   *
   * @param firstname Firstname
   * @param lastname Lastname
   * @param user {@link User}
   */
  void resendMailActiveRequest(User user, String firstname, String lastname);


  User registerUser(UUID userId,String firstname, String lastname, String email, String password, Role role);
  void updateRoleUser(UUID userId, Role role);
}