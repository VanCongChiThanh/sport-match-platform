package com.sportmatch.profileservice.service;

import com.sportmatch.commonlibrary.exception.AlreadyExistsException;
import com.sportmatch.commonlibrary.exception.NotFoundException;
import com.sportmatch.profileservice.constants.MessageConstant;
import com.sportmatch.profileservice.dto.request.ProfileRequest;
import com.sportmatch.profileservice.dto.response.ProfileResponse;
import com.sportmatch.profileservice.model.Profile;
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
                .orElseThrow(() -> new NotFoundException(MessageConstant.PROFILE_NOT_FOUND));
    }

    @Transactional(readOnly = true)
    public ProfileResponse getProfileResponseById(UUID id) {
        Profile profile = getProfileById(id);
        return profileMapper.toResponse(profile);
    }

    @Transactional
    public Profile createProfile(Profile profile) {
        if (profileRepository.existsByUsername(profile.getUsername())) {
            throw new AlreadyExistsException(MessageConstant.PROFILE_ALREADY_EXISTS);
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