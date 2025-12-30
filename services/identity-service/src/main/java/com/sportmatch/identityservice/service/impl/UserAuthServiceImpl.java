package com.sportmatch.identityservice.service.impl;

import com.sportmatch.identityservice.common.constant.MessageConstant;
import com.sportmatch.identityservice.entity.User;
import com.sportmatch.identityservice.exception.NotFoundException;
import com.sportmatch.identityservice.repository.UserRepository;
import com.sportmatch.identityservice.service.UserAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class UserAuthServiceImpl implements UserAuthService {

  private final UserRepository userRepository;

  @Override
  @Transactional(readOnly = true)
  public Optional<User> findByEmail(String email) {
    return userRepository.findByEmail(email);
  }

  @Override
  @Transactional(readOnly = true)
  public User findById(UUID id) {
    Optional<User> user = userRepository.findById(id);
    return user.orElseThrow(() -> new NotFoundException(MessageConstant.USER_NOT_FOUND));
  }
}