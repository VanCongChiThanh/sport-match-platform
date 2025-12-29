package com.sportmatch.identityservice.service;


import com.sportmatch.identityservice.entity.User;
import com.sportmatch.identityservice.entity.UserProvider;
import com.sportmatch.identityservice.entity.enums.AuthProvider;

import java.util.Optional;

public interface UserProviderService {
  void create(AuthProvider provider, String providerId, String email, User user);

  Optional<UserProvider> findByProvider(String providerId, AuthProvider authProvider, String email);

  Optional<UserProvider> findByEmail(String email);

  Optional<UserProvider> findByProviderIdAndProvider(String googleId, AuthProvider authProvider);
}