package com.sportmatch.profileservice.kafka.producer;

import com.sportmatch.profileservice.model.Profile;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Kafka producer for publishing profile events
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ProfileProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Value("${kafka.topics.profile-created}")
    private String profileCreatedTopic;

    @Value("${kafka.topics.profile-updated}")
    private String profileUpdatedTopic;

    @Value("${kafka.topics.profile-deleted}")
    private String profileDeletedTopic;

    public void sendProfileCreatedEvent(Profile profile) {
        Map<String, Object> event = buildProfileEvent(profile);
        event.put("eventType", "PROFILE_CREATED");
        event.put("createdAt", LocalDateTime.now().toString());

        kafkaTemplate.send(profileCreatedTopic, profile.getId().toString(), event);
        log.info("Published ProfileCreatedEvent for profile: {}", profile.getId());
    }

    public void sendProfileUpdatedEvent(Profile profile) {
        Map<String, Object> event = buildProfileEvent(profile);
        event.put("eventType", "PROFILE_UPDATED");
        event.put("updatedAt", LocalDateTime.now().toString());

        kafkaTemplate.send(profileUpdatedTopic, profile.getId().toString(), event);
        log.info("Published ProfileUpdatedEvent for profile: {}", profile.getId());
    }

    public void sendProfileDeletedEvent(Profile profile) {
        Map<String, Object> event = new HashMap<>();
        event.put("profileId", profile.getId().toString());
        event.put("userId", profile.getUserId().toString());
        event.put("eventType", "PROFILE_DELETED");
        event.put("deletedAt", LocalDateTime.now().toString());

        kafkaTemplate.send(profileDeletedTopic, profile.getId().toString(), event);
        log.info("Published ProfileDeletedEvent for profile: {}", profile.getId());
    }

    private Map<String, Object> buildProfileEvent(Profile profile) {
        Map<String, Object> event = new HashMap<>();
        event.put("profileId", profile.getId().toString());
        event.put("userId", profile.getUserId().toString());
        event.put("username", profile.getUsername());
        event.put("fullName", profile.getFullName());
        event.put("location", profile.getLocation());
        return event;
    }
}
