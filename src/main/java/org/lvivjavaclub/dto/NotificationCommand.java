package org.lvivjavaclub.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;

import java.net.URI;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.Optional;

public record NotificationCommand(
        @NotBlank String templateCode,
        URI callbackUri,
        Optional<String> correlationId,
        @Future OffsetDateTime scheduledFor,
        Duration retryDelay,
        Channel channel
) {
    public enum Channel {
        EMAIL,
        SMS,
        WEBHOOK
    }
}
