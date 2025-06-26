package com.arka.user_mservice.infra.adapters.out.redis.adapter;

import com.arka.user_mservice.application.ports.out.RefreshTokenStoragePort;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class RedisRefreshTokenAdapter implements RefreshTokenStoragePort {

    private final ReactiveRedisTemplate<String, String> reactiveRedisTemplate;

    private String getKey(UUID userId, String sessionId) {
        return "refresh_token:" + userId + ":" + sessionId;
    }

    @Override
    public Mono<Void> save(UUID userId, String sessionId, String refreshToken, Duration duration) {
        return reactiveRedisTemplate
                .opsForValue()
                .set(getKey(userId, sessionId), refreshToken, duration)
                .then();
    }

    @Override
    public Mono<String> get(UUID userId, String sessionId) {
        return reactiveRedisTemplate.opsForValue().get(getKey(userId, sessionId));
    }

    @Override
    public Mono<Void> delete(UUID userId, String sessionId) {
        String key = "refresh_token:" + userId + ":" + sessionId;
        return reactiveRedisTemplate.delete(key).then();
    }

    @Override
    public Mono<Void> deleteAllByUserId(UUID userId) {
        String pattern = "refresh_token:" + userId + ":*";

        return reactiveRedisTemplate.scan()
                .filter(key -> key.startsWith("refresh_token:" + userId.toString() + ":"))
                .collectList()
                .flatMapMany(Flux::fromIterable)
                .flatMap(reactiveRedisTemplate::delete)
                .then();
    }
}
