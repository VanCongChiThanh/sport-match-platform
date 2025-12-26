package com.sportmatch.profileservice.service;

import com.sportmatch.profileservice.dto.ProfileRequest;
import com.sportmatch.profileservice.dto.ProfileResponse;
import com.sportmatch.profileservice.entity.Profile;
import com.sportmatch.profileservice.exception.ProfileAlreadyExistsException;
import com.sportmatch.profileservice.exception.ProfileNotFoundException;
import com.sportmatch.profileservice.mapper.ProfileMapper;
import com.sportmatch.profileservice.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProfileService {
    private final ProfileRepository profileRepository;
    private final ProfileMapper profileMapper;

    @Transactional(readOnly = true)
    public Profile getProfileById(UUID id) {
        return profileRepository.findById(id)
                .orElseThrow(() -> new ProfileNotFoundException("Profile not found with id: " + id));
    }

    @Transactional(readOnly = true)
    public ProfileResponse getProfileResponseById(UUID id) {
        Profile profile = getProfileById(id);
        return profileMapper.toResponse(profile);
    }

    @Transactional
    public Profile createProfile(Profile profile) {
        if (profileRepository.existsByUsername(profile.getUsername())) {
            throw new ProfileAlreadyExistsException("Username already taken: " + profile.getUsername());
        }
        // send kafka event to match service to create a new profile

        return profileRepository.save(profile);
    }
    @Transactional
    public ProfileResponse updateProfileFromRequest(UUID id, ProfileRequest request) {
        Profile existingProfile = getProfileById(id);
        profileMapper.updateProfileFromEntity(
                profileMapper.toEntity(request),
                existingProfile
        );
        return profileMapper.toResponse(existingProfile);
    }
    @Transactional
    public ProfileResponse createProfileFromRequest(ProfileRequest profileRequest) {
        Profile profile = profileMapper.toEntity(profileRequest);
        Profile createdProfile = createProfile(profile);
        return profileMapper.toResponse(createdProfile);
    }

}