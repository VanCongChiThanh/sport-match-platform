package com.sportmatch.identityservice.service.impl;


import com.sportmatch.identityservice.entity.User;
import com.sportmatch.identityservice.entity.UserProvider;
import com.sportmatch.identityservice.entity.enums.AuthProvider;
import com.sportmatch.identityservice.repository.UserProviderRepository;
import com.sportmatch.identityservice.service.UserProviderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserProviderServiceImpl implements UserProviderService {
  private final UserProviderRepository userProviderRepository;

  @Override
  public void create(AuthProvider provider, String providerId, String email, User user) {
    UserProvider userProvider = new UserProvider();
    userProvider.setProvider(provider);
    userProvider.setProviderId(providerId);
    userProvider.setEmail(email);
    userProvider.setUser(user);
    userProviderRepository.save(userProvider);
  }



  @Override
  public Optional<UserProvider> findByProvider(
      String providerId, AuthProvider authProvider, String email) {
    return userProviderRepository.findByProviderIdAndProviderAndEmail(
        providerId, authProvider, email);
  }

  @Override
  public Optional<UserProvider> findByEmail(String email) {
    return userProviderRepository.findFirstByEmailOrderByCreatedAtDesc(email);
  }

  @Override
  public Optional<UserProvider> findByProviderIdAndProvider(String googleId, AuthProvider authProvider) {
    return userProviderRepository.findByProviderIdAndProvider(googleId, authProvider);
  }
}