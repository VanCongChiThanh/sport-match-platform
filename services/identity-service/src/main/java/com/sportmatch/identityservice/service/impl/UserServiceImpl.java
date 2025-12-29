package com.sportmatch.identityservice.service.impl;



import com.sportmatch.identityservice.common.CommonFunction;
import com.sportmatch.identityservice.constant.MessageConstant;
import com.sportmatch.identityservice.entity.User;
import com.sportmatch.identityservice.entity.enums.ActiveStatus;
import com.sportmatch.identityservice.entity.enums.AuthProvider;
import com.sportmatch.identityservice.entity.enums.Role;
import com.sportmatch.identityservice.exception.*;
import com.sportmatch.identityservice.payload.general.ResponseDataAPI;
import com.sportmatch.identityservice.payload.request.ChangePasswordRequest;
import com.sportmatch.identityservice.payload.request.ForgotPasswordRequest;
import com.sportmatch.identityservice.payload.request.ResetPasswordRequest;
import com.sportmatch.identityservice.repository.UserRepository;
import com.sportmatch.identityservice.service.UserProviderService;
import com.sportmatch.identityservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
@RequiredArgsConstructor
//@EnableConfigurationProperties(DomainProperties.class)
public class UserServiceImpl implements UserService {
  private static final String EMAIL_PREFIX = "@";

  private static final String LANGUAGE_CODE = "vi";

  private final UserRepository userRepository;

  private final PasswordEncoder passwordEncoder;

//  private final EmailService emailService;

  private final UserProviderService userProviderService;

//  private final UserInfoService userInfoService;

//  private final DomainProperties domainProperties;

  @Override
  @Transactional(readOnly = true)
  public Optional<User> findByEmail(String email) {
    return userRepository.findByEmail(email);
  }

  @Override
  @Transactional(readOnly = true)
  public User findById(UUID id) {
    return userRepository
        .findById(id)
        .orElseThrow(() -> new NotFoundException(MessageConstant.USER_NOT_FOUND));
  }

  @Override
  @Transactional(readOnly = true)
  public Set<User> findByIds(Set<UUID> ids) {
    return userRepository.findAllByIdIn(ids);
  }

  @Override
  public List<UUID> findAllByRole(Role role) {
    List<User> users = userRepository.findAllByRole(role);
    List<UUID> uuids = new ArrayList<>();
    for (User user : users) {
      uuids.add(user.getId());
    }
    return uuids;
  }

  @Override
  public ResponseDataAPI changePassword(
          UUID userId, String oldPasswordHashed, ChangePasswordRequest req) {
    if (oldPasswordHashed == null || !BCrypt.checkpw(req.getOldPassword(), oldPasswordHashed)) {
      throw new BadRequestException(MessageConstant.CHANGE_CREDENTIAL_NOT_CORRECT);
    }
    if (!Objects.equals(req.getConfirmNewPassword(), req.getNewPassword())) {
      throw new BadRequestException(MessageConstant.PASSWORD_FIELDS_MUST_MATCH);
    }
    if (Objects.equals(req.getOldPassword(), req.getNewPassword())) {
      throw new BadRequestException(MessageConstant.NEW_PASSWORD_NOT_SAME_OLD_PASSWORD);
    }
    User user = findById(userId);
    try {
      user.setPassword(passwordEncoder.encode(req.getNewPassword()));
      userRepository.save(user);
    } catch (RuntimeException ex) {
      throw new InternalServerException(MessageConstant.CHANGE_CREDENTIAL_FAIL);
    }
    return ResponseDataAPI.success(null, null);
  }


  @Override
  public User registerUser(
      String firstname, String lastname, String email, String password, Role role) {
    if(role.equals(Role.ROLE_ADMIN)) {
      throw new ForbiddenException(MessageConstant.FORBIDDEN_ERROR);
    }
    if (userRepository.existsByEmail(email.toLowerCase())) {
      throw new BadRequestException(MessageConstant.REGISTER_EMAIL_ALREADY_IN_USE);
    }
    User user = this.toUserEntity(email, password);
    user.setRole(role);
    User result = userRepository.save(user);
    userProviderService.create(AuthProvider.LOCAL, null, email.toLowerCase(), result);
//    userInfoService.createUserInfo(result.getId(), firstname, lastname,null);
//    emailService.sendMailConfirmRegister(
//        firstname, lastname, result.getEmail(), result.getConfirmationToken(), LANGUAGE_CODE);
    return result;
  }
  @Override
  public User registerUserOauth2(
      String firstname, String lastname, String email, String avatar, AuthProvider provider,String providerId) {
    Optional<User> existingUser = userRepository.findByEmail(email.toLowerCase());
    if (existingUser.isPresent()) {
        return existingUser.get();
    }
    User user = this.toUserEntity(email, null);
    user.setRole(Role.ROLE_USER);
    user.setEnabled(true);
    User result = userRepository.save(user);
    userProviderService.create(provider, providerId, email.toLowerCase(), result);
//    userInfoService.createUserInfo(result.getId(), firstname, lastname,avatar);
    return result;
  }
  private User toUserEntity(String email, String password) {
    User user = new User();
    user.setEmail(email.toLowerCase());
    if (password != null) {
      user.setPassword(passwordEncoder.encode(password));
    }
    user.setConfirmationToken(UUID.randomUUID());
    user.setActiveStatus(ActiveStatus.ACCEPT);
    return user;
  }

  @Override
  public ResponseEntity<ResponseDataAPI> confirmEmailRegister(String confirmationToken) {
    User user =
        userRepository
            .findByConfirmationToken(UUID.fromString(confirmationToken))
            .orElseThrow(
                () -> new ForbiddenException(MessageConstant.INCORRECT_CONFIRMATION_TOKEN));
    if (user.getConfirmedAt() != null) {
      throw new ForbiddenException(MessageConstant.USER_IS_ENABLED);
    }
    user.setEnabled(true);
    user.setConfirmedAt(CommonFunction.getCurrentDateTime());
    userRepository.save(user);

    return ResponseEntity.ok(
        ResponseDataAPI.success(user.getRole().toString().replace("ROLE_", ""), null));
  }

  @Override
  public ResponseDataAPI forgotPassword(ForgotPasswordRequest forgotPasswordRequest) {
    User user =
        userRepository
            .findByEmail(forgotPasswordRequest.getEmail().toLowerCase())
            .orElseThrow(() -> new NotFoundException(MessageConstant.USER_NOT_FOUND));

    user.setResetPasswordToken(UUID.randomUUID());
    user.setResetPasswordSentAt(CommonFunction.getCurrentDateTime());
    User result = userRepository.save(user);
//    emailService.sendMailForgetPassword(
//        result.getEmail(), result.getResetPasswordToken(), LANGUAGE_CODE, user.getRole());
    return ResponseDataAPI.success(null, null);
  }

  @Override
  public ResponseDataAPI resetPassword(ResetPasswordRequest resetPasswordRequest) {
    if (!resetPasswordRequest
        .getPassword()
        .equals(resetPasswordRequest.getPasswordConfirmation())) {
      throw new BadRequestException(MessageConstant.PASSWORD_FIELDS_MUST_MATCH);
    }
    User user =
        userRepository
            .findByEmail(resetPasswordRequest.getEmail())
            .orElseThrow(() -> new NotFoundException(MessageConstant.USER_NOT_FOUND));
    if (user.getResetPasswordToken() == null
        || !user.getResetPasswordToken().equals(resetPasswordRequest.getResetPasswordToken())) {
      throw new UnauthorizedException(MessageConstant.RESET_CREDENTIAL_FAIL);
    }
    user.setPassword(passwordEncoder.encode(resetPasswordRequest.getPassword()));
    user.setResetPasswordToken(null);
    userRepository.save(user);
    return ResponseDataAPI.success(null, null);
  }

  @Override
  public void setActiveUser(User user) {
    user.setEnabled(true);
    user.setConfirmedAt(CommonFunction.getCurrentDateTime());
    userRepository.save(user);
  }

  @Override
  public void deactivateUser(String reason, User user) {
    user.setEnabled(false);
    userRepository.save(user);
    userRepository.revokeAll(user.getId());
    //    emailService.sendMailDeactivateUserByAdmin(reason, user);
  }

  @Override
  public void resendMailActiveRequest(User user, String firstname, String lastname) {

    user.setConfirmationToken(UUID.randomUUID());

    if (user.isEnabled() && user.getConfirmedAt() != null) {
      throw new ForbiddenException(MessageConstant.USER_IS_ENABLED);
    }
    User result = userRepository.save(user);
//    emailService.sendMailConfirmRegister(
//        firstname, lastname, result.getEmail(), result.getConfirmationToken(), LANGUAGE_CODE);
  }


  @Override
  public User registerUser(
      UUID userId, String firstname, String lastname, String email, String password, Role role) {
    if (userRepository.existsByEmail(email.toLowerCase())) {
      throw new BadRequestException(MessageConstant.REGISTER_EMAIL_ALREADY_IN_USE);
    }
    User user = this.toUserEntity(email, password);
    user.setId(userId);
    user.setRole(role);
    User result = userRepository.save(user);
    userProviderService.create(AuthProvider.LOCAL, null, email.toLowerCase(), result);
//
//    emailService.sendMailConfirmRegister(
//        firstname, lastname, result.getEmail(), result.getConfirmationToken(), LANGUAGE_CODE);
    return result;
  }

  @Override
  public void updateRoleUser(UUID userId, Role role) {
    User user = this.findById(userId);
    if(user.getRole().equals(Role.ROLE_ADMIN)) {
      throw new ForbiddenException(MessageConstant.FORBIDDEN_ERROR);
    }
    user.setRole(role);
    userRepository.save(user);
  }
}