package com.sportmatch.profileservice.repository;

import com.sportmatch.profileservice.model.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, UUID> {
    boolean existsByUsername(String username);

    Optional<Profile> findByUserId(UUID id);
}