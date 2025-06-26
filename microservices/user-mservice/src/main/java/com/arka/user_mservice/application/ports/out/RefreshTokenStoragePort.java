package com.arka.user_mservice.application.ports.out;

import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.UUID;

public interface RefreshTokenStoragePort {
    Mono<Void> save(UUID userId, String sessionId, String refreshToken, Duration duration);
    Mono<String> get(UUID userId, String sessionId);
    Mono<Void> delete(UUID userId, String sessionId);
}
