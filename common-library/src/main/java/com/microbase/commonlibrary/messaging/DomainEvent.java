package com.microbase.commonlibrary.messaging;

import com.microbase.commonlibrary.observability.CorrelationIdHolder;

import java.time.Instant;
import java.util.UUID;

public record DomainEvent<T>(
        String eventId,
        String eventType,
        String aggregateType,
        String aggregateId,
        String source,
        Instant occurredAt,
        String correlationId,
        T payload
) {
    public static <T> DomainEvent<T> of(
            String eventType,
            String aggregateType,
            String aggregateId,
            String source,
            T payload) {
        return new DomainEvent<>(
                UUID.randomUUID().toString(),
                eventType,
                aggregateType,
                aggregateId,
                source,
                Instant.now(),
                CorrelationIdHolder.get(),
                payload
        );
    }
}