package com.sportmatch.profileservice.service;

import com.sportmatch.commonlibrary.exception.AlreadyExistsException;
import com.sportmatch.commonlibrary.exception.NotFoundException;
import com.sportmatch.commonlibrary.utils.AuthenticationUtils;
import com.sportmatch.profileservice.constants.MessageConstant;
import com.sportmatch.profileservice.dto.request.ProfileRequest;
import com.sportmatch.profileservice.dto.request.ProfileUpdateRequest;
import com.sportmatch.profileservice.dto.response.ProfileResponse;
import com.sportmatch.profileservice.kafka.producer.ProfileProducer;
import com.sportmatch.profileservice.mapper.ProfileMapper;
import com.sportmatch.profileservice.model.Profile;
import com.sportmatch.profileservice.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProfileService {
    private final ProfileRepository profileRepository;
    private final ProfileMapper profileMapper;
    private final ProfileProducer profileProducer;

    // ==================== READ OPERATIONS ====================

    @Transactional(readOnly = true)
    public Profile getProfileById(UUID id) {
        return profileRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(MessageConstant.PROFILE_NOT_FOUND));
    }

    @Transactional(readOnly = true)
    public Profile getProfileByUserId(UUID userId) {
        return profileRepository.findByUserId(userId)
                .orElseThrow(() -> new NotFoundException(MessageConstant.PROFILE_NOT_FOUND));
    }

    @Transactional(readOnly = true)
    public ProfileResponse getProfileResponseById(UUID id) {
        Profile profile = getProfileById(id);
        return profileMapper.toResponse(profile);
    }

    @Transactional(readOnly = true)
    public ProfileResponse getProfileResponseByUserId(UUID userId) {
        Profile profile = getProfileByUserId(userId);
        return profileMapper.toResponse(profile);
    }

    @Transactional(readOnly = true)
    public ProfileResponse getMyProfile() {
        UUID userId = UUID.fromString(AuthenticationUtils.extractUserId());
        return getProfileResponseByUserId(userId);
    }

    @Transactional(readOnly = true)
    public Page<ProfileResponse> getAllProfiles(Pageable pageable) {
        return profileRepository.findAll(pageable)
                .map(profileMapper::toResponse);
    }

    @Transactional(readOnly = true)
    public Page<ProfileResponse> searchByUsername(String username, Pageable pageable) {
        return profileRepository.findByUsernameContainingIgnoreCase(username, pageable)
                .map(profileMapper::toResponse);
    }

    // ==================== CREATE OPERATIONS ====================

    @Transactional
    public Profile createProfile(Profile profile) {
        if (profileRepository.existsByUsername(profile.getUsername())) {
            throw new AlreadyExistsException(MessageConstant.PROFILE_ALREADY_EXISTS);
        }

        Profile savedProfile = profileRepository.save(profile);
        log.info("Created profile: {} for user: {}", savedProfile.getId(), savedProfile.getUserId());

        // Publish Kafka event
        profileProducer.sendProfileCreatedEvent(savedProfile);

        return savedProfile;
    }

    @Transactional
    public ProfileResponse createProfileFromRequest(ProfileRequest request) {
        UUID userId = UUID.fromString(AuthenticationUtils.extractUserId());

        // Check if profile already exists for this user
        if (profileRepository.findByUserId(userId).isPresent()) {
            throw new AlreadyExistsException(MessageConstant.PROFILE_ALREADY_EXISTS);
        }

        Profile profile = profileMapper.toEntity(request);
        profile.setUserId(userId);

        Profile createdProfile = createProfile(profile);
        return profileMapper.toResponse(createdProfile);
    }

    // ==================== UPDATE OPERATIONS ====================

    @Transactional
    public ProfileResponse updateMyProfile(ProfileUpdateRequest request) {
        UUID userId = UUID.fromString(AuthenticationUtils.extractUserId());
        Profile existingProfile = getProfileByUserId(userId);

        profileMapper.updateProfileFromRequest(request, existingProfile);
        Profile savedProfile = profileRepository.save(existingProfile);

        log.info("Updated profile: {} for user: {}", savedProfile.getId(), userId);

        // Publish Kafka event
        profileProducer.sendProfileUpdatedEvent(savedProfile);

        return profileMapper.toResponse(savedProfile);
    }

    @Transactional
    public ProfileResponse updateProfileById(UUID id, ProfileUpdateRequest request) {
        Profile existingProfile = getProfileById(id);

        profileMapper.updateProfileFromRequest(request, existingProfile);
        Profile savedProfile = profileRepository.save(existingProfile);

        log.info("Admin updated profile: {}", savedProfile.getId());

        // Publish Kafka event
        profileProducer.sendProfileUpdatedEvent(savedProfile);

        return profileMapper.toResponse(savedProfile);
    }

    // ==================== DELETE OPERATIONS ====================

    @Transactional
    public void deleteProfile(UUID id) {
        Profile profile = getProfileById(id);

        // Publish Kafka event before deletion
        profileProducer.sendProfileDeletedEvent(profile);

        profileRepository.delete(profile);
        log.info("Deleted profile: {} for user: {}", id, profile.getUserId());
    }
}
