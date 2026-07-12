package com.microbase.profileservice.kafka.producer;

import com.microbase.commonlibrary.messaging.DomainEvent;
import com.microbase.profileservice.model.Profile;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProfileProducer {

    private static final String SOURCE = "profile-service";
    private static final String AGGREGATE_TYPE = "profile";

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Value("${kafka.topics.profile-created}")
    private String profileCreatedTopic;

    @Value("${kafka.topics.profile-updated}")
    private String profileUpdatedTopic;

    @Value("${kafka.topics.profile-deleted}")
    private String profileDeletedTopic;

    public void sendProfileCreatedEvent(Profile profile) {
        DomainEvent<Map<String, Object>> event = buildEvent("profile.created", profile, buildProfilePayload(profile));
        kafkaTemplate.send(profileCreatedTopic, profile.getId().toString(), event);
        log.info("Published {} event for profile: {}", event.eventType(), profile.getId());
    }

    public void sendProfileUpdatedEvent(Profile profile) {
        DomainEvent<Map<String, Object>> event = buildEvent("profile.updated", profile, buildProfilePayload(profile));
        kafkaTemplate.send(profileUpdatedTopic, profile.getId().toString(), event);
        log.info("Published {} event for profile: {}", event.eventType(), profile.getId());
    }

    public void sendProfileDeletedEvent(Profile profile) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("profileId", profile.getId().toString());
        payload.put("userId", profile.getUserId().toString());

        DomainEvent<Map<String, Object>> event = buildEvent("profile.deleted", profile, payload);
        kafkaTemplate.send(profileDeletedTopic, profile.getId().toString(), event);
        log.info("Published {} event for profile: {}", event.eventType(), profile.getId());
    }

    private DomainEvent<Map<String, Object>> buildEvent(String eventType, Profile profile, Map<String, Object> payload) {
        return DomainEvent.of(eventType, AGGREGATE_TYPE, profile.getId().toString(), SOURCE, payload);
    }

    private Map<String, Object> buildProfilePayload(Profile profile) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("profileId", profile.getId().toString());
        payload.put("userId", profile.getUserId().toString());
        payload.put("username", profile.getUsername());
        payload.put("fullName", profile.getFullName());
        payload.put("location", profile.getLocation());
        return payload;
    }
}