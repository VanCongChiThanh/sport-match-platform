package com.sportmatch.identityservice.service;



import com.sportmatch.identityservice.common.constant.MessageConstant;
import com.sportmatch.identityservice.entity.User;
import com.sportmatch.identityservice.entity.UserPrincipal;
import com.sportmatch.identityservice.exception.NotFoundException;
import com.sportmatch.identityservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

  private final UserRepository userRepository;

  @Override
  @Transactional
  public UserDetails loadUserByUsername(String email) {
    User user =
        userRepository
            .findByEmail(email)
            .orElseThrow(() -> new NotFoundException(MessageConstant.USER_NOT_FOUND));
    return UserPrincipal.create(user);
  }

  public UserDetails loadUserById(UUID id) {
    User user =
        userRepository
            .findById(id)
            .orElseThrow(() -> new NotFoundException(MessageConstant.USER_NOT_FOUND));
    return UserPrincipal.create(user);
  }

}