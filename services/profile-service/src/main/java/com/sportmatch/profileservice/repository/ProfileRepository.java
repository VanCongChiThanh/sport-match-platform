package com.sportmatch.profileservice.repository;

import com.sportmatch.profileservice.entity.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, UUID> {
    boolean existsByUsername(String username);
}