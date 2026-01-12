package com.sportmatch.profileservice.kafka.consumer;

import com.sportmatch.profileservice.model.Profile;
import com.sportmatch.profileservice.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.UUID;

/**
 * Kafka consumer that listens for user registration events
 * and automatically creates profiles for new users
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserSyncDataConsumer {

    private final ProfileRepository profileRepository;

    @KafkaListener(topics = "${kafka.topics.user-registered}", groupId = "profile-service")
    @Transactional
    public void handleUserRegisteredEvent(Map<String, Object> message) {
        log.info("Received UserRegisteredEvent: {}", message);

        UUID userId = UUID.fromString((String) message.get("userId"));
        String username = (String) message.get("username");
        String fullName = (String) message.get("fullName");

        // Check if profile already exists
        if (profileRepository.findByUserId(userId).isPresent()) {
            log.warn("Profile already exists for user: {}", userId);
            return;
        }

        // Create new profile
        Profile profile = new Profile();
        profile.setUserId(userId);
        profile.setUsername(username);
        profile.setFullName(fullName != null ? fullName : username);

        Profile savedProfile = profileRepository.save(profile);
        log.info("Created profile {} for user: {}", savedProfile.getId(), userId);
    }
}
